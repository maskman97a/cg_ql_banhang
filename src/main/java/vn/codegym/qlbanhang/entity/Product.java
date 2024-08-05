package vn.codegym.qlbanhang.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product extends BaseEntity {
    private int index;
    private String productCode;
    private String productName;
    private String imageUrl;
    private String note;
    private Long price;
    private Long quantity;

    private static final String TABLE_NAME = "product";

    public static String getTableName() {
        return TABLE_NAME;
    }
}
