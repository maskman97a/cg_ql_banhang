package vn.codegym.qlbanhang.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;

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

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getSearchColumn() {
        return SEARCH_COLUMN;
    }

    public Order() {

    }
}
