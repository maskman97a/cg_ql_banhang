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
    private Long price;
    private String imageUrl;
    private String productCode;
    private String productName;
    private String description;
    private Integer quantity;
    private Integer unitPrice;
    private Integer categoryId;
}
