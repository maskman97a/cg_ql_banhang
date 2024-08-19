package vn.codegym.qlbanhang.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductListPerCategoryDto {
    private List<ProductPagingDto> productPagingList;
    private Integer categoryId;
    private String categoryName;
    private PagingDto paging;

    public ProductListPerCategoryDto() {
        this.productPagingList = new ArrayList<>();
    }
}
