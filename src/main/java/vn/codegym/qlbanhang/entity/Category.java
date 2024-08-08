package vn.codegym.qlbanhang.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class Category extends BaseEntity {
    private int index;
    @Column(name = "name")
    private String name;

    public static final String TABLE_NAME = "category";

    public static String getTableName() {
        return TABLE_NAME;
    }
}
