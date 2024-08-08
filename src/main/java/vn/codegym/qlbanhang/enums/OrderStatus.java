package vn.codegym.qlbanhang.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    NEW(0, "Tạo mới, chờ xác nhận"),
    COMPLETED(1, "Hoàn thành"),
    CANCELED(2, "Đã hủy"),
    ACCEPTED(3, "Đã xác nhận");

    private final int value;
    private final String description;

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
