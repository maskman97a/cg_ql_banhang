package vn.codegym.qlbanhang.model;

import lombok.Getter;
import lombok.Setter;
import vn.codegym.qlbanhang.annotation.Column;
import vn.codegym.qlbanhang.annotation.Table;
import vn.codegym.qlbanhang.database.DatabaseConnection;
import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.JoinConditionDto;
import vn.codegym.qlbanhang.dto.OrderByConditionDto;
import vn.codegym.qlbanhang.dto.QueryConditionDto;
import vn.codegym.qlbanhang.entity.BaseEntity;
import vn.codegym.qlbanhang.utils.ClassUtils;
import vn.codegym.qlbanhang.utils.DataUtil;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Getter
@Setter
public class BaseModel {
    protected final Logger log = Logger.getLogger("System Log");
    private DatabaseConnection databaseConnection;
    private Connection connection;
    private Class entityClass;
    private String tableName;

    protected BaseModel(Class<?> cl) {
        Table tableAnnotation = cl.getDeclaredAnnotation(Table.class);
        if (!DataUtil.isNullObject(tableAnnotation) && !DataUtil.isNullObject(tableAnnotation.name())) {
            this.tableName = tableAnnotation.name();
            this.databaseConnection = DatabaseConnection.getInstance();
            this.connection = databaseConnection.getConnection();
            this.entityClass = cl;
        }
    }


    public static BaseModel getInstance(Class<?> cl) {
        Map<Class<?>, BaseModel> mapInstance = new HashMap<>();
        List<Class<?>> entityClassList = ClassUtils.getClasses("vn.codegym.qlbanhang.entity");
        for (Class<?> entityClass : entityClassList) {
            mapInstance.put(entityClass, new BaseModel(entityClass));
        }
        return mapInstance.get(cl);
    }


    public List<BaseEntity> search(BaseSearchDto baseSearchDto) {
        List<BaseEntity> baseEntities = new ArrayList<>();
        try {
            String sql = getSelectSQL(baseSearchDto);
            if (!DataUtil.isNullObject(baseSearchDto.getPage()) && !DataUtil.isNullObject(baseSearchDto.getSize())) {
                sql += " limit ? offset ?";
            }
            System.out.println("Search SQL: " + sql);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            int index = 1;
            if (baseSearchDto.getQueryConditionDtos() != null && !baseSearchDto.getQueryConditionDtos().isEmpty()) {
                for (QueryConditionDto queryConditionDto : baseSearchDto.getQueryConditionDtos()) {
                    preparedStatement.setObject(index++, queryConditionDto.getValue());
                }
            }
            if (!DataUtil.isNullObject(baseSearchDto.getPage()) && !DataUtil.isNullObject(baseSearchDto.getSize())) {
                preparedStatement.setInt(index++, baseSearchDto.getSize());
                preparedStatement.setInt(index, (baseSearchDto.getPage() - 1) * baseSearchDto.getSize());
            }

            baseEntities = executeSelect(preparedStatement);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseEntities;
    }

    public Integer count(BaseSearchDto baseSearchDto) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            String countSQL = "SELECT COUNT(1) FROM (" + getSelectSQL(baseSearchDto) + ") a";
            PreparedStatement preparedStatement = conn.prepareStatement(countSQL);
            int index = 1;
            if (baseSearchDto.getQueryConditionDtos() != null && !baseSearchDto.getQueryConditionDtos().isEmpty()) {
                for (QueryConditionDto queryConditionDto : baseSearchDto.getQueryConditionDtos()) {
                    preparedStatement.setObject(index++, queryConditionDto.getValue());
                }
            }
            System.out.println("Count query: " + countSQL);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }
        return 0;
    }

    public String getSelectSQL(BaseSearchDto baseSearchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ").append(tableName).append(".* ");
        sb.append(" FROM ").append(tableName);
        if (baseSearchDto != null) {
            if (baseSearchDto.getJoinConditionDtos() != null && !baseSearchDto.getJoinConditionDtos().isEmpty()) {
                for (JoinConditionDto joinConditionDto : baseSearchDto.getJoinConditionDtos()) {
                    sb.append(joinConditionDto.getJoinType()).append(joinConditionDto.getTableName());
                    sb.append(" ON ");
                    sb.append(tableName + "." + joinConditionDto.getColumn1());
                    sb.append(" =  ");
                    sb.append(joinConditionDto.getTableName() + "." + joinConditionDto.getColumn2());
                }
            }
            int index = 0;
            if (baseSearchDto.getQueryConditionDtos() != null && !baseSearchDto.getQueryConditionDtos().isEmpty()) {
                sb.append(" WHERE 1 = 1 AND ");
                for (QueryConditionDto queryConditionDto : baseSearchDto.getQueryConditionDtos()) {
                    if (index > 0) {
                        if (DataUtil.isNullOrEmpty(queryConditionDto.getAppendLogic())) {
                            sb.append(" AND ");
                        } else {
                            sb.append(" ").append(queryConditionDto.getAppendLogic()).append(" ");
                        }
                    }
                    index++;
                    sb.append(queryConditionDto.getColumnName());
                    sb.append(" ").append(queryConditionDto.getOperator());
                    sb.append(" ? ");
                }
            }
            if (!DataUtil.isNullOrEmpty(baseSearchDto.getOrderByConditionDtos())) {
                sb.append(" ORDER BY ");
                index = 0;
                for (OrderByConditionDto orderByConditionDto : baseSearchDto.getOrderByConditionDtos()) {
                    if (index > 0) {
                        sb.append(", ");
                    }
                    index++;
                    sb.append(orderByConditionDto.getColumnName());
                    if (!DataUtil.isNullOrEmpty(orderByConditionDto.getOrderType())) {
                        sb.append(" ").append(orderByConditionDto.getOrderType()).append(" ");
                    }

                }
            }
        }

        return sb.toString();
    }

    public List<BaseEntity> findAll() {
        try {
            Connection con = DatabaseConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(getSelectSQL(null));
            return executeSelect(preparedStatement);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<BaseEntity> findAllActive() {

        try {
            Connection con = DatabaseConnection.getInstance().getConnection();
            BaseSearchDto baseSearchDto = new BaseSearchDto();
            baseSearchDto.getQueryConditionDtos().add(QueryConditionDto.newAndCondition("status", "=", "1"));
            PreparedStatement preparedStatement = con.prepareStatement(getSelectSQL(baseSearchDto));
            int index = 1;
            if (baseSearchDto.getQueryConditionDtos() != null && !baseSearchDto.getQueryConditionDtos().isEmpty()) {
                for (QueryConditionDto queryConditionDto : baseSearchDto.getQueryConditionDtos()) {
                    preparedStatement.setObject(index++, queryConditionDto.getValue());
                }
            }
            return executeSelect(preparedStatement);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<BaseEntity> findAllActiveWithSort(String sortCol, String sortType) {

        try {
            Connection con = DatabaseConnection.getInstance().getConnection();
            BaseSearchDto baseSearchDto = new BaseSearchDto();
            baseSearchDto.getQueryConditionDtos().add(QueryConditionDto.newAndCondition("status", "=", "1"));
            baseSearchDto.getOrderByConditionDtos().add(new OrderByConditionDto(sortCol, sortType));
            PreparedStatement preparedStatement = con.prepareStatement(getSelectSQL(baseSearchDto));
            int index = 1;
            if (baseSearchDto.getQueryConditionDtos() != null && !baseSearchDto.getQueryConditionDtos().isEmpty()) {
                for (QueryConditionDto queryConditionDto : baseSearchDto.getQueryConditionDtos()) {
                    preparedStatement.setObject(index++, queryConditionDto.getValue());
                }
            }
            return executeSelect(preparedStatement);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    public BaseEntity findById(int id) throws SQLException {
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        QueryConditionDto queryConditionDto = QueryConditionDto.newAndCondition("id", "= ", id);
        baseSearchDto.getQueryConditionDtos().add(queryConditionDto);
        return findOne(baseSearchDto);
    }

    public BaseEntity findOne(BaseSearchDto baseSearchDto) {
        try {
            String sql = getSelectSQL(baseSearchDto);
            System.out.println("Execute sql: " + sql);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            int index = 1;
            if (baseSearchDto.getQueryConditionDtos() != null && !baseSearchDto.getQueryConditionDtos().isEmpty()) {
                for (QueryConditionDto queryConditionDto : baseSearchDto.getQueryConditionDtos()) {
                    preparedStatement.setObject(index++, queryConditionDto.getValue());
                }
            }
            List<BaseEntity> baseEntities = executeSelect(preparedStatement);
            if (baseEntities != null && !baseEntities.isEmpty()) {
                return baseEntities.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<BaseEntity> executeSelect(PreparedStatement preparedStatement) throws SQLException {
        List<BaseEntity> baseEntities = new ArrayList<>();
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            BaseEntity baseEntity = BaseEntity.getInstance(tableName);
            ResultSetMetaData metaData = rs.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                assert baseEntity != null;
                for (Field field : ClassUtils.getAllFields(baseEntity)) {
                    field.setAccessible(true);
                    if (field.getAnnotation(Column.class) == null) {
                        continue;
                    }
                    String colName = field.getAnnotation(Column.class).name();
                    if (colName != null && colName.equals(metaData.getColumnName(i))) {
                        try {
                            Object val = rs.getObject(i);
                            field.set(baseEntity, val);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            baseEntities.add(baseEntity);
        }
        return baseEntities;
    }

    public BaseEntity save(BaseEntity baseEntity) throws SQLException {
        List<String> lstColName = ClassUtils.getAllColumnName(baseEntity);
        boolean exists = false;
        int index = 0;
        StringBuilder sb = new StringBuilder();
        if (!DataUtil.isNullObject(baseEntity.getId())) {
            //Update
            sb.append(" UPDATE ");
            sb.append(tableName);
            sb.append(" SET ");
            for (String colName : lstColName) {
                if (colName.equals("id")) {
                    continue;
                }
                if (index > 0) {
                    sb.append(",");
                }
                index++;
                sb.append(colName).append(" = ").append(" ? ");
            }
            sb.append(" WHERE id = ? ");
        } else {
            Integer nextId = getNextID();
            baseEntity.setId(nextId);
            sb.append("INSERT INTO ");
            sb.append(tableName);
            sb.append("(");
            for (String colName : lstColName) {
                if (index > 0) {
                    sb.append(",");
                }
                index++;
                sb.append(colName);
            }
            sb.append(") VALUE (");
            index = 0;
            for (String colName : lstColName) {
                if (index > 0) {
                    sb.append(",");
                }
                index++;
                sb.append("?");
            }
            sb.append(")");
        }
        PreparedStatement preparedStatement = connection.prepareStatement(sb.toString());
        index = 1;
        for (String columnName : lstColName) {
            preparedStatement.setObject(index++, ClassUtils.getValueFromColumnAnnotation(baseEntity, columnName));
        }
        if (exists) {
            preparedStatement.setObject(index, baseEntity.getId());
        }
        int updateResult = preparedStatement.executeUpdate();
        if (updateResult == 1) {
            return findById(baseEntity.getId());
        } else {
            throw new SQLException("Save failed");
        }
    }

    public Integer getNextID() throws SQLException {
        String sql = "SELECT `AUTO_INCREMENT` " + "FROM  `information_schema`.`TABLES` " + "WHERE `TABLE_SCHEMA` = ? " + "AND   `TABLE_NAME` = ? ";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, databaseConnection.getSchemaName());
        ps.setString(2, tableName);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return null;
    }
}
