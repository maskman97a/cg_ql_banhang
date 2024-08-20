package vn.codegym.qlbanhang.enums;

public enum StockTransType {
    PENDING("Chờ xuất kho"),
    IMPORT("Nhập kho"),
    EXPORT("Xuất kho");

    public final String description;

    StockTransType(String description) {
        this.description = description;
    }
}
