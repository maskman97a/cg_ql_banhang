package vn.codegym.qlbanhang.dto.response;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import vn.codegym.qlbanhang.enums.ErrorType;
import vn.codegym.qlbanhang.exception.BusinessException;

@Data
@AllArgsConstructor
public class BaseResponse<T> {
    @SerializedName("errorCode")
    private int errorCode;
    @SerializedName("errorMessage")
    private String errorMessage;
    @SerializedName("additionalData")
    private T additionalData;

    public BaseResponse() {
        setError(ErrorType.SUCCESS);
    }

    public void setError(ErrorType errorType) {
        this.additionalData = null;
        this.errorCode = errorType.getErrorCode();
        this.errorMessage = errorType.getErrorMessage();
    }

    public void setError(ErrorType errorType, String errorMessage) {
        this.errorCode = errorType.getErrorCode();
        this.errorMessage = errorMessage;
    }

    public void setError(BusinessException ex) {
        this.errorCode = ex.getErrorCode();
        this.errorMessage = ex.getErrorMessage();
    }
}
