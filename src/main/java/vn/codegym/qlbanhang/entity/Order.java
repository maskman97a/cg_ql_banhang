package vn.codegym.qlbanhang.entity;

import lombok.Getter;
import lombok.Setter;
import vn.codegym.qlbanhang.utils.DataUtil;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
public class Order extends BaseEntity {
    public static final String TABLE_NAME = "orders";
    public static final String SEARCH_COLUMN = "name";
    private int index;
    @Column(name = "code")
    private String code;
    @Column(name = "customer_id")
    private int customerId;
    @Column(name = "address")
    private String address;
    @Column(name = "order_date")
    private LocalDateTime orderDate;
    private Integer totalAmount;
    private String orderStatusName;

    private String orderDateStr;

    private List<OrderDetail> orderDetailList;

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getSearchColumn() {
        return SEARCH_COLUMN;
    }

    public Order() {

    }

    public String getOrderDateStr() {
        if (!DataUtil.isNullObject(orderDate)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            this.orderDateStr = orderDate.format(formatter);
        }
        return this.orderDateStr;
    }
}
