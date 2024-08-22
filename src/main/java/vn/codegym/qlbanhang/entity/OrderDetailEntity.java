package vn.codegym.qlbanhang.entity;

import lombok.Getter;
import lombok.Setter;
import vn.codegym.qlbanhang.annotation.Column;
import vn.codegym.qlbanhang.annotation.Table;


@Getter
@Setter
@Table(name = "order_detail")
public class OrderDetailEntity extends BaseEntity {
    private int index;
    @Column(name = "order_id")
    private int orderId;
    @Column(name = "product_id")
    private int productId;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "unit_price")
    private Long unitPrice;
    @Column(name = "amount")
    private Long amount;

    private ProductEntity productEntity;

    public OrderDetailEntity() {

    }

    public Long getAmount() {
        return quantity * unitPrice;
    }
}
