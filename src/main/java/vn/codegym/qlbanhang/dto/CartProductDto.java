package vn.codegym.qlbanhang.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.codegym.qlbanhang.utils.DataUtil;

@Getter
@Setter
@AllArgsConstructor
public class CartProductDto {
    @SerializedName("index")
    private int index;
    @SerializedName("product")
    private ProductDto product;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("amount")
    private int amount;

    public CartProductDto(int index, ProductDto product, int quantity) {
        this.index = index;
        this.product = product;
        this.quantity = quantity;
    }

    public int getAmount() {
        if (!DataUtil.isNullObject(product)) {
            return product.getPrice().intValue() * quantity;
        }
        return 0;
    }
}
