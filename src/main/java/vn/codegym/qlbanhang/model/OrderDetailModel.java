package vn.codegym.qlbanhang.model;

import vn.codegym.qlbanhang.entity.OrderDetail;

public class OrderDetailModel extends BaseModel {
    private static final OrderDetailModel inst = new OrderDetailModel();

    private OrderDetailModel() {
        super(OrderDetail.class);
    }

    public static OrderDetailModel getInstance() {
        return inst;
    }
}
