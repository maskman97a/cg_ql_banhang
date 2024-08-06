package vn.codegym.qlbanhang.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import vn.codegym.qlbanhang.constants.ErrorType;

@Data
@AllArgsConstructor
public class BaseResponse<T> {
    private int errorCode;
    private String errorMessage;
    private T additionalData;

    public BaseResponse() {
        this.errorCode = ErrorType.SUCCESS;
        this.errorMessage = ErrorType.getMessage(ErrorType.SUCCESS);
    }
}
