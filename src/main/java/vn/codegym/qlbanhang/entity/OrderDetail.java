package vn.codegym.qlbanhang.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class OrderDetail extends BaseEntity {
    public static final String TABLE_NAME = "order_detail";
    private int index;
    @Column(name = "order_id")
    private int orderId;
    @Column(name = "product_id")
    private int productId;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "unit_price")
    private int unitPrice;
    @Column(name = "amount")
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
