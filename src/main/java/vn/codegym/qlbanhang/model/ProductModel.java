package vn.codegym.qlbanhang.model;

import vn.codegym.qlbanhang.dto.ProductDto;
import vn.codegym.qlbanhang.entity.Note;

import java.util.ArrayList;
import java.util.List;

public class ProductModel extends BaseModel {

    public ProductModel() {
        super(Note.getTableName());
    }


    public List<ProductDto> findProduct() {
        List<ProductDto> productDtoList = new ArrayList<>();
        productDtoList.add(new ProductDto(1, "Product 1", 30000, "../../images/logo.png"));
        productDtoList.add(new ProductDto(2, "Product 2", 30000, "../../images/logo.png"));
        productDtoList.add(new ProductDto(3, "Product 3", 2000, "../../images/logo.png"));
        productDtoList.add(new ProductDto(4, "Product 4", 2000, "../../images/logo.png"));
        return productDtoList;
    }
}
