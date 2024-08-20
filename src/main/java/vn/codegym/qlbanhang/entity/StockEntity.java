package vn.codegym.qlbanhang.entity;

import lombok.Getter;
import lombok.Setter;
import vn.codegym.qlbanhang.annotation.Column;
import vn.codegym.qlbanhang.annotation.Table;

@Getter
@Setter
@Table(name = "stock")
public class StockEntity extends BaseEntity {
    @Column(name = "product_id")
    private int productId;
    @Column(name = "availableQuantity")
    private int availableQuantity;
    @Column(name = "pending_quantity")
    private int pendingQuantity;
    @Column(name = "totalQuantity")
    private int totalQuantity;

}
