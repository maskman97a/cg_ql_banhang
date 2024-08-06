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
        if (req.getPathInfo() == null) {
            adminService.renderAdmin(req, resp);
            return;
        }
        System.out.println(req.getPathInfo());
        switch (req.getPathInfo()) {
            case "/search":
                adminService.renderAdmin(req, resp);
                break;
            case "/product/product-create":
                adminService.renderCreateProductForm(req, resp);
                break;
            case "/transaction":
//                adminService.renderUpdateNoteForm(req, resp);
            default:
                adminService.renderAdmin(req, resp);
                break;
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getPathInfo() == null) {
            adminService.renderAdmin(req, resp);
        }
        System.out.println(req.getPathInfo());
        switch (req.getPathInfo()) {
            case "/search":
                break;
            case "/product-create":
                adminService.createNewProduct(req, resp);
                break;
            case "/update-note":
                break;
        }
    }
}
