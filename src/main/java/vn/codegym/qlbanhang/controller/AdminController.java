package vn.codegym.qlbanhang.controller;


import vn.codegym.qlbanhang.service.AdminService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/admin/*")
public class AdminController extends BaseController {
    private AdminService adminService;

    public void init() {
        this.adminService = new AdminService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        adminService.renderAdmin(req, resp);
    }
}
