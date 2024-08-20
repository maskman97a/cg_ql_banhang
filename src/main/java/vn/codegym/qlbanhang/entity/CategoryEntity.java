package vn.codegym.qlbanhang.entity;

import lombok.Getter;
import lombok.Setter;
import vn.codegym.qlbanhang.annotation.Column;
import vn.codegym.qlbanhang.annotation.Table;


@Getter
@Setter
@Table(name = "category")
public class CategoryEntity extends BaseEntity {
    private int index;
    @Column(name = "name")
    private String name;
    @Column(name = "sort")
    private int sort;

}
