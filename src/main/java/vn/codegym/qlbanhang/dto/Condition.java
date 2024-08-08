package vn.codegym.qlbanhang.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Condition {
    private String columnName;
    private String operator;
    private Object value;


}
