package vn.codegym.qlbanhang.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockDto extends BaseDto {
    private int index;
    private int productId;
    private int availableQuantity;
    private int pendingQuantity;
    private int totalQuantity;
    private String productCode;
    private String productName;

    private String strAvailableQuantity;
    private String strPendingQuantity;
    private String strTotalQuantity;
}
