package vn.codegym.qlbanhang.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class Product extends BaseEntity {
    private int index;
    @Column(name = "product_code")
    private String productCode;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "note")
    private String note;
    @Column(name = "price")
    private Long price;
    @Column(name = "quantity")
    private Integer quantity;

    public static final String TABLE_NAME = "product";

    public static String getTableName() {
        return TABLE_NAME;
    }
}
