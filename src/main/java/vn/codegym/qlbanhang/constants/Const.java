package vn.codegym.qlbanhang.constants;

public class Const {
    public static final class OrderStatus {
        public static final int NEW = 0;
        public static final int COMPLETED = 1;
        public static final int CANCELED = 2;
        public static final int ACCEPTED = 3;
    }

    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_UNACTIVE = 0;

    public static final String getUrlPathName(String path) {
        switch (path) {
            case "admin":
                return "Quản trị";
            case "product":
                return "Sản phẩm";
            case "category":
                return "Thể loại";
            case "transaction":
                return "Đơn hàng";
        }
        return "";
    }
}
