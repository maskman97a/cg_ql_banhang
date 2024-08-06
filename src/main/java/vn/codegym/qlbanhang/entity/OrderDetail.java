package vn.codegym.qlbanhang.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetail extends BaseEntity {
    private static final String TABLE_NAME = "order_detail";
    private int index;
    private int orderId;
    private int productId;
    private int quantity;
    private int unitPrice;
    private int amount;

    public static String getTableName() {
        return TABLE_NAME;
    }


    public OrderDetail() {

    }

    public int getAmount() {
        return quantity * unitPrice;
    }
}
