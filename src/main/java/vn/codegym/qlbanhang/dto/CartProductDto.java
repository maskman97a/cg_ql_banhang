package vn.codegym.qlbanhang.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartProductDto {
    private ProductDto product;
    private int quantity;
}
