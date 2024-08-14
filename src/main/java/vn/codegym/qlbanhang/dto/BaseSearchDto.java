package vn.codegym.qlbanhang.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BaseSearchDto {
    private String keyword;
    private Integer status;
    private Integer page;
    private Integer size;
    private List<Condition> conditions;
    private List<JoinCondition> joinConditions;
    private List<OrderByCondition> orderByConditions;

    public BaseSearchDto() {
        this.conditions = new ArrayList<>();
        this.joinConditions = new ArrayList<>();
        this.orderByConditions = new ArrayList<>();
    }
}
