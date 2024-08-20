package vn.codegym.qlbanhang.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinConditionDto {
    private String tableName;
    private String joinType;

    private String column1;
    private String column2;

}
