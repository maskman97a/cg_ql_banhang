package vn.codegym.ql_banhang.model;

import vn.codegym.ql_banhang.dto.ProductDto;
import vn.codegym.ql_banhang.entity.Note;

import java.util.ArrayList;
import java.util.List;

public class ProductModel extends BaseModel {

    public ProductModel() {
        super(Note.getTableName());
    }


    public List<ProductDto> findProduct() {
        List<ProductDto> productDtoList = new ArrayList<>();
        ProductDto productDto = new ProductDto();
        productDto.setId(1);
        productDto.setName("Product 1");
        productDto.setPrice(1000);
        productDtoList.add(productDto);
        productDtoList.add(new ProductDto(2, "Product", 30000));
        productDtoList.add(new ProductDto(3, "Product 3", 2000));
        productDtoList.add(new ProductDto(4, "Product", 2000));
        return productDtoList;
    }
}
