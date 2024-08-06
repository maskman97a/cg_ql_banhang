package vn.codegym.qlbanhang.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Customer extends BaseEntity {
    private static final String TABLE_NAME = "customer";
    private static final String SEARCH_COLUMN = "name";
    private int index;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getSearchColumn() {
        return SEARCH_COLUMN;
    }

    public Customer() {

    }
}
