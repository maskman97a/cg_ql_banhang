package vn.codegym.qlbanhang.controller;

import vn.codegym.qlbanhang.service.LoginService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet({"/logout"})
public class LogoutController extends BaseController {
    protected Logger log = Logger.getLogger(LogoutController.class.getName());
    private LoginService loginService;

    public void init() {
        this.loginService = new LoginService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("token") != null) {
            req.getSession().setAttribute("token", null);
        }
        resp.sendRedirect(req.getContextPath());
    }
}
