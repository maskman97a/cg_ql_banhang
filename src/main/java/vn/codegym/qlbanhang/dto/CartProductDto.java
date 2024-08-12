package vn.codegym.qlbanhang.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.codegym.qlbanhang.utils.DataUtil;

@Getter
@Setter
@AllArgsConstructor
public class CartProductDto {
    private int index;
    private ProductDto product;
    private int quantity;
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
