package vn.codegym.qlbanhang.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginService {
    public void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if (username.equals("admin") && password.equals("admin")) {
            HttpSession httpSession = req.getSession();
            httpSession.setAttribute("token", "151515");
            resp.sendRedirect(req.getContextPath() + "/admin");
        } else {
            req.setAttribute("loginMessage", "Đăng nhập không thành công");
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}
