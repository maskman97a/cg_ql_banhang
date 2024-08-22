package vn.codegym.qlbanhang.entity;

import lombok.Getter;
import lombok.Setter;
import vn.codegym.qlbanhang.annotation.Column;
import vn.codegym.qlbanhang.annotation.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "cancel_order_otp")
public class CancelOrderOtpEntity extends BaseEntity {
    @Column(name = "order_id")
    private int orderId;
    @Column(name = "otp")
    private String otp;
    @Column(name = "expired_date")
    private LocalDateTime expiredDate;
}
