package vn.codegym.qlbanhang.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutService {

    public void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getSession().getAttribute("token") != null) {
            req.getSession().setAttribute("token", null);
            req.getSession().setAttribute("userInfo", null);
        }
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
