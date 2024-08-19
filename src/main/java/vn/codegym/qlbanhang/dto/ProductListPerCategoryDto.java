package vn.codegym.qlbanhang.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductListPerCategoryDto {
    private List<ProductDto> productList;
    private String categoryName;
    private PagingDto paging;

    public ProductListPerCategoryDto() {
        this.productList = new ArrayList<>();
    }
}
