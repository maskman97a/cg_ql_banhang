package vn.codegym.qlbanhang.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Cart {
    private List<CartProductDto> cartProductList;

    public Cart(List<CartProductDto> cartProductList) {
        this.cartProductList = cartProductList;
    }
}
