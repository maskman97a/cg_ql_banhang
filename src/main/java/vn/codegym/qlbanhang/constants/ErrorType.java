package vn.codegym.qlbanhang.constants;

public class ErrorType {
    public static final int SUCCESS = 0;
    public static final int UNKNOWN = -1;

    public static final String getMessage(int errorCode) {
        switch (errorCode) {
            case SUCCESS:
                return "Thành công";
            case UNKNOWN:
            default:
                return "Lỗi chưa xác định";
        }
    }
}
