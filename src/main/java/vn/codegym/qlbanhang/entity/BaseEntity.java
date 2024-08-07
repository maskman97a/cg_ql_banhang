package vn.codegym.qlbanhang.entity;

import lombok.Getter;
import lombok.Setter;
import vn.codegym.qlbanhang.constants.Const;

import javax.persistence.Column;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
public abstract class BaseEntity {
    @Column(name = "id")
    protected int id;
    @Column(name = "status")
    protected int status;
    @Column(name = "created_by")
    protected String createdBy;
    @Column(name = "created_date")
    protected Date createdDate;
    @Column(name = "updated_by")
    protected String updatedBy;
    @Column(name = "updated_date")
    protected Date updatedDate;

    private Map<String, Object> mapValue;

    public BaseEntity() {
        this.status = Const.STATUS_ACTIVE;
        this.createdBy = "admin";
        this.createdDate = new Date();
        this.updatedBy = "admin";
        this.updatedDate = new Date();
    }

    public static BaseEntity getInstance(String tableName) {
        switch (tableName) {
            case Product.TABLE_NAME:
                return new Product();
            case Customer.TABLE_NAME:
                return new Customer();
            case Order.TABLE_NAME:
                return new Order();
            case OrderDetail.TABLE_NAME:
                return new OrderDetail();
        }
        return null;
    }

    public Map<String, Object> getMapValue() {
        return mapValue;
    }

    public void setMapValue(Map<String, Object> mapValue) {
        this.mapValue = mapValue;
    }
}
