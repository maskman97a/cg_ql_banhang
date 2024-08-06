package vn.codegym.qlbanhang.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private String customerName;
    private String customerPhoneNumber;
    private String customerEmail;
    private String customerAddress;

}
