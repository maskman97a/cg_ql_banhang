package vn.codegym.qlbanhang.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Order extends BaseEntity {
    private static final String TABLE_NAME = "orders";
    private static final String SEARCH_COLUMN = "name";
    private int index;
    private String code;
    private int customerId;
    private String address;
    private Date orderDate;

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getSearchColumn() {
        return SEARCH_COLUMN;
    }

    public Order() {

    }
}
