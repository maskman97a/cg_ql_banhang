package vn.codegym.qlbanhang.dto.response;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import vn.codegym.qlbanhang.enums.ErrorType;

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
        this.errorCode = errorType.getErrorCode();
        this.errorMessage = errorType.getErrorMessage();
    }
}
