package vn.codegym.qlbanhang.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto extends BaseDto {
    @SerializedName("index")
    private int index;
    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private Long price;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("product_code")
    private String productCode;
    @SerializedName("productName")
    private String productName;
    @SerializedName("description")
    private String description;
    @SerializedName("quantity")
    private Integer quantity;
    @SerializedName("unitPrice")
    private Integer unitPrice;
    @SerializedName("categoryId")
    private Integer categoryId;
    @SerializedName("categoryName")
    private String categoryName;
    private String strPrice;
    private String strQuantity;
    private Integer availableQuantity;

}
