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
    private List<QueryConditionDto> queryConditionDtos;
    private List<JoinConditionDto> joinConditionDtos;
    private List<OrderByConditionDto> orderByConditionDtos;

    public BaseSearchDto() {
        this.queryConditionDtos = new ArrayList<>();
        this.joinConditionDtos = new ArrayList<>();
        this.orderByConditionDtos = new ArrayList<>();
    }
}
