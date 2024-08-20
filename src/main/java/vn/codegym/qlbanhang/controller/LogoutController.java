package vn.codegym.qlbanhang.controller;

import vn.codegym.qlbanhang.service.LogoutService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet({"/logout"})
public class LogoutController extends BaseController {
    protected Logger log = Logger.getLogger(LogoutController.class.getName());
    private LogoutService logoutService;

    public void init() {
        this.logoutService = new LogoutService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logoutService.logout(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logoutService.logout(req, resp);
    }
}
