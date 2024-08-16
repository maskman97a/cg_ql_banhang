package vn.codegym.qlbanhang.model;

import vn.codegym.qlbanhang.dto.UserInfoDto;

import java.util.Arrays;
import java.util.List;

public class UserModel {
    public List<UserInfoDto> findAll() {
        UserInfoDto userInfoDto1 = new UserInfoDto("Quản trị viên", "admin", "d033e22ae348aeb5660fc2140aec35850c4da997");
        UserInfoDto userInfoDto2 = new UserInfoDto("Nhân viên", "employer", "70cafc7ba74a3fc9057e77379a90af15d6578b69");
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
