package vn.codegym.qlbanhang.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Condition {
    public static class AppendLogic {
        public static final String AND = "AND";
        public static final String OR = "OR";
    }
    private String columnName;
    private String operator;
    private Object value;
    private String appendLogic;

    public static Condition newAndCondition(String columnName, String operator, Object value) {
        Condition condition = new Condition();
        condition.setColumnName(columnName);
        condition.setOperator(operator);
        condition.setValue(value);
        condition.setAppendLogic(AppendLogic.AND);
        return condition;
    }

    public static Condition newOrCondition(String columnName, String operator, Object value) {
        Condition condition = new Condition();
        condition.setColumnName(columnName);
        condition.setOperator(operator);
        condition.setValue(value);
        condition.setAppendLogic(AppendLogic.OR);
        return condition;
    }
}
