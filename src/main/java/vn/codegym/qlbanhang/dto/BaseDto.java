package vn.codegym.qlbanhang.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BaseDto {
    protected Integer id;
    protected Integer status;
    protected String createdBy;
    protected Date createdDate;
    protected String updatedBy;
    protected Date updatedDate;
}
