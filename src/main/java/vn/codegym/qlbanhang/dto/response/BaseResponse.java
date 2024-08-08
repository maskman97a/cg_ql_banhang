package vn.codegym.qlbanhang.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import vn.codegym.qlbanhang.enums.ErrorType;

@Data
@AllArgsConstructor
public class BaseResponse<T> {
    private int errorCode;
    private String errorMessage;
    private T additionalData;

    public BaseResponse() {
        setError(ErrorType.SUCCESS);
    }

    public void setError(ErrorType errorType) {
        this.errorCode = errorType.getErrorCode();
        this.errorMessage = errorType.getErrorMessage();
    }
}
