package vn.codegym.ql_banhang.model;

import vn.codegym.ql_banhang.database.DatabaseConnection;
import vn.codegym.ql_banhang.dto.BaseSearchDto;
import vn.codegym.ql_banhang.dto.Condition;
import vn.codegym.ql_banhang.dto.JoinCondition;
import vn.codegym.ql_banhang.entity.BaseEntity;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BaseModel {
    private Connection con;
    private final String tableName;

    public BaseModel(String tableName) {
        this.con = DatabaseConnection.getConnection();
        this.tableName = tableName;
    }

    public List<BaseEntity> search(BaseSearchDto baseSearchDto) throws SQLException {
        String sql = getSearchSQL(baseSearchDto);
        sql += " order by id desc ";
        sql += " limit ? offset ?";
        PreparedStatement preparedStatement = this.con.prepareStatement(sql);
        int index = 1;
        if (baseSearchDto.getConditions() != null && !baseSearchDto.getConditions().isEmpty()) {
            for (Condition condition : baseSearchDto.getConditions()) {
                preparedStatement.setObject(index++, condition.getValue());
            }
        }
        preparedStatement.setInt(index++, baseSearchDto.getSize());
        preparedStatement.setInt(index, (baseSearchDto.getPage() - 1) * baseSearchDto.getSize());
        return executeSelect(preparedStatement);
    }

    public Integer count(BaseSearchDto baseSearchDto) throws SQLException {
        PreparedStatement preparedStatement = this.con.prepareStatement(getSearchSQL(baseSearchDto));
        int index = 1;
        if (baseSearchDto.getConditions() != null && !baseSearchDto.getConditions().isEmpty()) {
            for (Condition condition : baseSearchDto.getConditions()) {
                preparedStatement.setObject(index++, condition.getValue());
            }
        }
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public String getSearchSQL(BaseSearchDto baseSearchDto) {
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
            if (baseSearchDto.getConditions() != null && !baseSearchDto.getConditions().isEmpty()) {
                sb.append(" WHERE ");
                for (Condition condition : baseSearchDto.getConditions()) {
                    sb.append(condition.getColumnName());
                    sb.append(" ").append(condition.getOperator());
                    sb.append(" ? ");
                }
            }
        }
        return sb.toString();
    }

    public List<BaseEntity> findAll() throws SQLException {
        PreparedStatement preparedStatement = this.con.prepareStatement(getSearchSQL(null));
        return executeSelect(preparedStatement);
    }

    public BaseEntity findById(int id) throws SQLException {
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        Condition condition = new Condition();
        condition.setColumnName("id");
        condition.setOperator("=");
        condition.setValue(id);
        baseSearchDto.setConditions(Collections.singletonList(condition));
        PreparedStatement preparedStatement = this.con.prepareStatement(getSearchSQL(baseSearchDto));
        List<BaseEntity> baseEntities = executeSelect(preparedStatement);
        if (baseEntities != null && !baseEntities.isEmpty()) {
            return baseEntities.get(0);
        }
        return null;
    }

    public List<BaseEntity> executeSelect(PreparedStatement preparedStatement) {
        List<BaseEntity> baseEntities = new ArrayList<>();
        try {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                BaseEntity baseEntity = BaseEntity.getInstance(tableName);
                ResultSetMetaData metaData = rs.getMetaData();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    for (Field field : baseEntity.getClass().getDeclaredFields()) {
                        field.setAccessible(true);
                        if (field.getName().equals(metaData.getColumnName(i))) {
                            try {
                                field.set(baseEntity, rs.getObject(i));
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                baseEntities.add(baseEntity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return baseEntities;
    }

    public int save(BaseEntity baseEntity) {
        try {
            StringBuilder sb = new StringBuilder("INSERT INTO ");
            sb.append(tableName);
            sb.append("(");
            int index = 0;
            for (Map.Entry<String, Object> entry : baseEntity.getMapValue().entrySet()) {
                if (index > 0) {
                    sb.append(",");
                }
                index++;
                sb.append(entry.getKey());
            }
            sb.append(") VALUE (");
            index = 0;
            for (int i = 0; i < baseEntity.getMapValue().size(); i++) {
                if (index > 0) {
                    sb.append(",");
                }
                index++;
                sb.append("?");
            }
            sb.append(")");
            PreparedStatement preparedStatement = con.prepareStatement(sb.toString());
            index = 1;
            for (Map.Entry<String, Object> entry : baseEntity.getMapValue().entrySet()) {
                preparedStatement.setObject(index++, entry.getValue());
            }
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
