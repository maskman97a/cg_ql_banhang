package vn.codegym.qlbanhang.exception;

import lombok.Getter;
import lombok.Setter;
import vn.codegym.qlbanhang.enums.ErrorType;

@Getter
@Setter
public class BusinessException extends Exception {
    private int errorCode;
    private String errorMessage;

    public BusinessException(ErrorType errorType) {
        super(errorType.getErrorMessage());
        this.errorCode = errorType.getErrorCode();
        this.errorMessage = errorType.getErrorMessage();
    }

    public BusinessException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorMessage = message;
    }

    public BusinessException(String message, String... args) {
        super(String.format(message, (Object) args));
    }

}
