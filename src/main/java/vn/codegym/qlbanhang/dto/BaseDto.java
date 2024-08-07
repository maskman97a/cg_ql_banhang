package vn.codegym.qlbanhang.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseDto {
    protected Integer id;
    protected Integer status;
    protected String createdBy;
    protected LocalDateTime createdDate;
    protected String updatedBy;
    protected LocalDateTime updatedDate;
}
