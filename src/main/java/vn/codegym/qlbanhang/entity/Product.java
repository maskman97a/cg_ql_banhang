package vn.codegym.qlbanhang.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product extends BaseEntity {
    private static final String TABLE_NAME = "product";
    private String name;
    private String imageUrl;

    public static String getTableName() {
        return TABLE_NAME;
    }
}
