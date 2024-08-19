package vn.codegym.qlbanhang.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductPagingDto {
    private List<ProductDto> productList;
    private int page;

    public ProductPagingDto() {
        productList = new ArrayList<>();
    }
}
