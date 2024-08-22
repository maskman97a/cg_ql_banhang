package vn.codegym.qlbanhang.model;

import vn.codegym.qlbanhang.entity.CancelOrderOtpEntity;

public class CancelOrderOtpModel extends BaseModel {
    private static final CancelOrderOtpModel inst = new CancelOrderOtpModel();

    private CancelOrderOtpModel() {
        super(CancelOrderOtpEntity.class);
    }

    public static CancelOrderOtpModel getInstance() {
        return inst;
    }
}
