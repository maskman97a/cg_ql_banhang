package vn.codegym.qlbanhang.service;

import com.google.gson.Gson;
import vn.codegym.qlbanhang.model.UserModel;
import vn.codegym.qlbanhang.utils.ShaUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginService {
    private final UserModel userModel;
    private final Gson gson;

    public LoginService() {
        this.userModel = new UserModel();
        this.gson = new Gson();
    }
    public void login(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if (userModel.verifyUser(username, password)) {
            HttpSession httpSession = req.getSession();
            httpSession.setAttribute("token", generateToken(username));
            httpSession.setAttribute("userInfo", gson.toJson(userModel.findUserByUsername(username)));
            resp.sendRedirect(req.getContextPath() + "/admin");
        } else {
            req.setAttribute("loginMessage", "Đăng nhập không thành công");
            req.getRequestDispatcher("/views/login/login.jsp").forward(req, resp);
        }
    }

    public String generateToken(String username) {
        return ShaUtils.encryptSHA1(username.getBytes());
    }
}
