package vn.codegym.qlbanhang.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderByCondition {
    private String columnName;
    private String orderType;
}
