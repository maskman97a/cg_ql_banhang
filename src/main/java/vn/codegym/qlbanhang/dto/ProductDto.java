package vn.codegym.qlbanhang.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto extends BaseDto {
    private String name;
    private Integer price;
    private String imageUrl;

    public ProductDto(Integer id, String name, Integer price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}
