package vn.codegym.qlbanhang.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserInfoDto {
    private String fullName;
    private String username;
    private String password;
}
