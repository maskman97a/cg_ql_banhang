package vn.codegym.qlbanhang.entity;

import lombok.Getter;
import lombok.Setter;
import vn.codegym.qlbanhang.annotation.Column;
import vn.codegym.qlbanhang.annotation.Table;
import vn.codegym.qlbanhang.utils.DataUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@Table(name = "orders")
public class OrderEntity extends BaseEntity {
    private int index;
    @Column(name = "code")
    private String code;
    @Column(name = "customer_id")
    private int customerId;
    @Column(name = "address")
    private String address;
    @Column(name = "order_date")
    private LocalDateTime orderDate;
    private Long totalAmount;
    private String orderStatusName;

    private String orderDateStr;

    private List<OrderDetailEntity> orderDetailEntityList;

    public OrderEntity() {

    }

    public String getOrderDateStr() {
        if (!DataUtil.isNullObject(orderDate)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            this.orderDateStr = orderDate.format(formatter);
        }
        return this.orderDateStr;
    }
}
