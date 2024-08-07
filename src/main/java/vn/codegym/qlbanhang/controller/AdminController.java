package vn.codegym.qlbanhang.controller;


import vn.codegym.qlbanhang.service.AdminService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/*")
@MultipartConfig
public class AdminController extends BaseController {
    private AdminService adminService;

    public void init() {
        this.adminService = new AdminService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo() == null) {
            adminService.renderAdminFistTab(req, resp);
            return;
        }
        System.out.println(req.getPathInfo());
        switch (req.getPathInfo()) {
            case "/search":
                adminService.renderAdminFistTab(req, resp);
                break;
            case "/product/product-create":
                adminService.renderCreateProductForm(req, resp);
                break;
            case "/product/update":
                adminService.renderUpdateProductForm(req, resp);
                break;
            case "/product/delete":
                adminService.cancelProduct(req, resp);
                break;
            case "/transaction":
//                adminService.renderUpdateNoteForm(req, resp);
            case "/category":
                adminService.renderAdminFistTab(req, resp);
                break;
            default:
                adminService.renderAdminFistTab(req, resp);
                break;
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo() == null) {
            adminService.renderAdmin(req, resp);
        }
        System.out.println(req.getPathInfo());
        switch (req.getPathInfo()) {
            case "/search":
                break;
            case "/product/product-create":
                adminService.createNewProduct(req, resp);
                break;
            case "/product/update":
                adminService.updateProduct(req, resp);
                break;
            case "/product/delete":
                adminService.cancelProduct(req, resp);
                break;
        }
    }
}
