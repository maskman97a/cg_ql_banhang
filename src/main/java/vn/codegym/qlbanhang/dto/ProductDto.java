package vn.codegym.qlbanhang.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto extends BaseDto {
    private int index;
    private String name;
    private Integer price;
    private String imageUrl;
    private String productCode;
    private String productName;
    private String note;
    private Integer quantity;

    public ProductDto(Integer id, String name, Integer price, String imageUrl, String productCode, String productName, String note, Integer quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.productCode = productCode;
        this.productName = productName;
        this.note = note;
        this.quantity = quantity;
    }
}
