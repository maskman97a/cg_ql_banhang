package vn.codegym.qlbanhang.entity;

import lombok.Getter;
import lombok.Setter;
import vn.codegym.qlbanhang.annotation.Column;
import vn.codegym.qlbanhang.annotation.Table;


@Getter
@Setter
@Table(name = "customer")
public class CustomerEntity extends BaseEntity {
    public static final String SEARCH_COLUMN = "name";
    private int index;
    @Column(name = "name")
    private String name;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;
    @Column(name = "address")
    private String address;
    
    public CustomerEntity() {

    }
}
