package vn.codegym.qlbanhang.constants;

public class Const {
    public static final class OrderStatus {
        public static final int NEW = 0;
        public static final int COMPLETED = 1;
        public static final int CANCELED = 2;
        public static final int ACCEPTED = 3;
        public static final int REFUND = 4;
        public static final int IMPORT = 9;
    }

    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_UNACTIVE = 0;

    public static String getUrlPathName(String path) {
        switch (path) {
            case "admin":
                return "Trang Quản trị";
            case "product":
                return "Quản lý Sản phẩm";
            case "category":
                return "Quản lý Loại sản phẩm";
            case "transaction":
                return "Quản lý Đơn hàng";
            case "stock":
                return "Quản lý Tồn kho";
        }
        return "";
    }
}
