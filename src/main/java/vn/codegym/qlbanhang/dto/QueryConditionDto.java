package vn.codegym.qlbanhang.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QueryConditionDto {
    public static class AppendLogic {
        public static final String AND = "AND";
        public static final String OR = "OR";
    }
    private String columnName;
    private String operator;
    private Object value;
    private String appendLogic;

    public static QueryConditionDto newAndCondition(String columnName, String operator, Object value) {
        QueryConditionDto queryConditionDto = new QueryConditionDto();
        queryConditionDto.setColumnName(columnName);
        queryConditionDto.setOperator(operator);
        queryConditionDto.setValue(value);
        queryConditionDto.setAppendLogic(AppendLogic.AND);
        return queryConditionDto;
    }

    public static QueryConditionDto newOrCondition(String columnName, String operator, Object value) {
        QueryConditionDto queryConditionDto = new QueryConditionDto();
        queryConditionDto.setColumnName(columnName);
        queryConditionDto.setOperator(operator);
        queryConditionDto.setValue(value);
        queryConditionDto.setAppendLogic(AppendLogic.OR);
        return queryConditionDto;
    }
}
