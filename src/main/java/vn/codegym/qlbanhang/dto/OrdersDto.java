package vn.codegym.qlbanhang.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class OrdersDto {
    private Integer id;
    private int index;
    private String code;
    private Integer customerId;
    private String address;
    private LocalDateTime orderDate;
    private Integer status;
    private String createdBy;
    private LocalDateTime createdDate;
    private String updatedBy;
    private LocalDateTime updatedDate;

    private String customerName;
    private String email;
    private String phone;
    private String orderDateStr;
    private String statusName;


}
