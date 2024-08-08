package vn.codegym.qlbanhang.model;

import vn.codegym.qlbanhang.database.DatabaseConnection;
import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.Condition;
import vn.codegym.qlbanhang.dto.JoinCondition;
import vn.codegym.qlbanhang.dto.OrderByCondition;
import vn.codegym.qlbanhang.entity.BaseEntity;
import vn.codegym.qlbanhang.utils.ClassUtils;
import vn.codegym.qlbanhang.utils.DataUtil;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaseModel {
    private final String tableName;

    public BaseModel(String tableName) {
        this.tableName = tableName;
    }

    public List<BaseEntity> search(BaseSearchDto baseSearchDto) throws SQLException {
        List<BaseEntity> baseEntities = new ArrayList<>();
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            String sql = getSelectSQL(baseSearchDto);
            if (!DataUtil.isNullObject(baseSearchDto.getPage()) && !DataUtil.isNullObject(baseSearchDto.getSize())) {
                sql += " limit ? offset ?";
            }
            System.out.println("Search SQL: " + sql);
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            int index = 1;
            if (baseSearchDto.getConditions() != null && !baseSearchDto.getConditions().isEmpty()) {
                for (Condition condition : baseSearchDto.getConditions()) {
                    preparedStatement.setObject(index++, condition.getValue());
                }
            }
            if (!DataUtil.isNullObject(baseSearchDto.getPage()) && !DataUtil.isNullObject(baseSearchDto.getSize())) {
                preparedStatement.setInt(index++, baseSearchDto.getSize());
                preparedStatement.setInt(index, (baseSearchDto.getPage() - 1) * baseSearchDto.getSize());
            }

            baseEntities = executeSelect(preparedStatement);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return baseEntities;
    }

    public Integer count(BaseSearchDto baseSearchDto) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            String countSQL = "SELECT COUNT(1) FROM (" + getSelectSQL(baseSearchDto) + ") a";
            PreparedStatement preparedStatement = con.prepareStatement(countSQL);
            int index = 1;
            if (baseSearchDto.getConditions() != null && !baseSearchDto.getConditions().isEmpty()) {
                for (Condition condition : baseSearchDto.getConditions()) {
                    preparedStatement.setObject(index++, condition.getValue());
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
            if (con != null) {
                con.close();
            }
        }
        return 0;
    }

    public String getSelectSQL(BaseSearchDto baseSearchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ").append(tableName).append(".* ");
        sb.append(" FROM ").append(tableName);
        if (baseSearchDto != null) {
            if (baseSearchDto.getJoinConditions() != null && !baseSearchDto.getJoinConditions().isEmpty()) {
                for (JoinCondition joinCondition : baseSearchDto.getJoinConditions()) {
                    sb.append(joinCondition.getJoinType()).append(joinCondition.getTableName());
                    sb.append(" ON ");
                    sb.append(tableName + "." + joinCondition.getColumn1());
                    sb.append(" =  ");
                    sb.append(joinCondition.getTableName() + "." + joinCondition.getColumn2());
                }
            }
            int index = 0;
            if (baseSearchDto.getConditions() != null && !baseSearchDto.getConditions().isEmpty()) {
                sb.append(" WHERE 1 = 1 AND ");
                for (Condition condition : baseSearchDto.getConditions()) {
                    if (index > 0) {
                        if (DataUtil.isNullOrEmpty(condition.getAppendLogic())) {
                            sb.append(" AND ");
                        } else {
                            sb.append(" ").append(condition.getAppendLogic()).append(" ");
                        }
                    }
                    index++;
                    sb.append(condition.getColumnName());
                    sb.append(" ").append(condition.getOperator());
                    sb.append(" ? ");
                }
            }
            if (!DataUtil.isNullOrEmpty(baseSearchDto.getOrderByConditions())) {
                sb.append(" ORDER BY ");
                index = 0;
                for (OrderByCondition orderByCondition : baseSearchDto.getOrderByConditions()) {
                    if (index > 0) {
                        sb.append(", ");
                    }
                    index++;
                    sb.append(orderByCondition.getColumnName());
                    if (!DataUtil.isNullOrEmpty(orderByCondition.getOrderType())) {
                        sb.append(" ").append(orderByCondition.getOrderType()).append(" ");
                    }

                }
            }
        }

        return sb.toString();
    }

    public List<BaseEntity> findAll() throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(getSelectSQL(null));
            return executeSelect(preparedStatement);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return new ArrayList<>();
    }

    public BaseEntity findById(int id) throws SQLException {
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        Condition condition = new Condition();
        condition.setColumnName("id");
        condition.setOperator("=");
        condition.setValue(id);
        baseSearchDto.getConditions().add(condition);
        return findOne(baseSearchDto);
    }

    public BaseEntity findOne(BaseSearchDto baseSearchDto) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            String sql = getSelectSQL(baseSearchDto);
            System.out.println("Execute sql: " + sql);
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            int index = 1;
            if (baseSearchDto.getConditions() != null && !baseSearchDto.getConditions().isEmpty()) {
                for (Condition condition : baseSearchDto.getConditions()) {
                    preparedStatement.setObject(index++, condition.getValue());
                }
            }
            List<BaseEntity> baseEntities = executeSelect(preparedStatement);
            if (baseEntities != null && !baseEntities.isEmpty()) {
                return baseEntities.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.close();
            }
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

    public int save(BaseEntity baseEntity) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            List<String> lstColName = ClassUtils.getAllColumnName(baseEntity);
            boolean exists = false;
            if (!DataUtil.isNullObject(baseEntity.getId())) {
                BaseEntity baseEntityDB = findById(baseEntity.getId());
                if (baseEntityDB != null) {
                    exists = true;
                }
            }
            int index = 0;
            StringBuilder sb = new StringBuilder();
            if (!exists) {
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
                for (int i = 0; i < lstColName.size(); i++) {
                    if (index > 0) {
                        sb.append(",");
                    }
                    index++;
                    sb.append("?");
                }
                sb.append(")");
            } else {
                sb.append(" UPDATE ");
                sb.append(tableName);
                sb.append(" SET ");
                for (String colName : lstColName) {
                    if (index > 0) {
                        sb.append(",");
                    }
                    index++;
                    sb.append(colName).append(" = ").append(" ? ");
                }
            }
            PreparedStatement preparedStatement = con.prepareStatement(sb.toString());
            index = 1;
            for (String columnName : lstColName) {
                preparedStatement.setObject(index++, ClassUtils.getValueFromColumnAnnotation(baseEntity, columnName));
            }
            return preparedStatement.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return 0;
    }
}
