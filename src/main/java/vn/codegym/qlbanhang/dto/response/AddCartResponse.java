package vn.codegym.qlbanhang.dto.response;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import vn.codegym.qlbanhang.dto.CartProductDto;

import java.util.List;

@Getter
@Setter
public class AddCartResponse {
    @SerializedName("productList")
    private List<CartProductDto> productDtoList;
    @SerializedName("cartCount")
    private int cartCount;
}
