package vn.codegym.qlbanhang.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStockDto {
    private int productId;
    private int quantity;
}
