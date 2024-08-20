package vn.codegym.qlbanhang.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    NEW(0, "Tạo mới, chờ xác nhận"),
    COMPLETED(1, "Hoàn thành"),
    CANCELED(2, "Đã hủy"),
    ACCEPTED(3, "Đã xác nhận"),
    REFUND(4, "Hoàn trả");

    public final int value;
    public final String description;

    OrderStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static String getDescription(int val) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.value == val) {
                return status.description;
            }
        }
        return "";
    }

}
