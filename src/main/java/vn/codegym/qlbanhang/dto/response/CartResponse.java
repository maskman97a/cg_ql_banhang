package vn.codegym.qlbanhang.dto.response;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.codegym.qlbanhang.dto.CartProductDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    @SerializedName("productList")
    private List<CartProductDto> cartProductList;
    @SerializedName("cartCount")
    private int cartCount;

    public CartResponse(List<CartProductDto> cartProductList) {
        this.cartProductList = cartProductList;
        if (cartProductList != null) {
            this.cartCount = cartProductList.size();
        }
    }

    public int getCartCount() {
        if (cartProductList != null) {
            this.cartCount = cartProductList.size();
        }
        return this.cartCount;
    }
}
