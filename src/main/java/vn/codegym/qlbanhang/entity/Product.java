package vn.codegym.qlbanhang.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class Product extends BaseEntity {
    @Column(name = "product_code")
    private String productCode;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private Long price;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "category_id")
    private Integer categoryId;

    public static final String TABLE_NAME = "product";

    public static String getTableName() {
        return TABLE_NAME;
    }
}
