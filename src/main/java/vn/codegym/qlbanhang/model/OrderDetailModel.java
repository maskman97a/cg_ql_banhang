package vn.codegym.qlbanhang.model;

import vn.codegym.qlbanhang.entity.OrderDetailEntity;

public class OrderDetailModel extends BaseModel {
    private static final OrderDetailModel inst = new OrderDetailModel();

    private OrderDetailModel() {
        super(OrderDetailEntity.class);
    }

    public static OrderDetailModel getInstance() {
        return inst;
    }
}
