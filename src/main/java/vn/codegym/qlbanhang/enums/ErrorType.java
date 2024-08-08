package vn.codegym.qlbanhang.enums;

import lombok.Getter;

@Getter
public enum ErrorType {
    SUCCESS(0, "Thành công"),
    UNKNOWN(-1, "Lỗi không xác định"),
    INTERNAL_SERVER_ERROR(99, "Lỗi hệ thống");

    final int errorCode;
    final String errorMessage;

    ErrorType(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static String getMessage(int errorCode) {
        for (ErrorType errorType : ErrorType.values()) {
            if (errorType.errorCode == errorCode) {
                return errorType.errorMessage;
            }
        }
        return ErrorType.UNKNOWN.errorMessage;
    }


}
