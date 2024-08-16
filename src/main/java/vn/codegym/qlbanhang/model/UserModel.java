package vn.codegym.qlbanhang.model;

import vn.codegym.qlbanhang.dto.UserInfoDto;

import java.util.Arrays;
import java.util.List;

public class UserModel {
    public List<UserInfoDto> findAll() {
        UserInfoDto userInfoDto1 = new UserInfoDto("Quản trị viên", "admin", "admin");
        UserInfoDto userInfoDto2 = new UserInfoDto("Nhân viên", "employer", "admin");
        return Arrays.asList(userInfoDto1, userInfoDto2);
    }

    public boolean verifyUser(String username, String password) {
        List<UserInfoDto> userList = findAll();
        for (UserInfoDto userInfoDto : userList) {
            if (userInfoDto.getUsername().equalsIgnoreCase(username) && userInfoDto.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public UserInfoDto findUserByUsername(String username) {
        List<UserInfoDto> userList = findAll();
        for (UserInfoDto userInfoDto : userList) {
            if (userInfoDto.getUsername().equals(username)) {
                userInfoDto.setPassword(null);
                return userInfoDto;
            }
        }
        return null;
    }
}
