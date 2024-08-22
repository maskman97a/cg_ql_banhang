package vn.codegym.qlbanhang.controller;


import com.google.gson.Gson;
import lombok.SneakyThrows;
import vn.codegym.qlbanhang.dto.UserInfoDto;
import vn.codegym.qlbanhang.service.AdminService;
import vn.codegym.qlbanhang.service.StockService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/*")
@MultipartConfig
public class AdminController extends BaseController {
    private AdminService adminService;
    private StockService stockService;

    public void init() {
        this.adminService = AdminService.getInstance();
        this.stockService = StockService.getInstance();
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        boolean valid = validateLogin(req, resp);
        if (valid) {
            adminService.setNavigationBar(req);

            if (req.getPathInfo() == null) {
                req.setAttribute("renderMainAdmin", true);
                req.getRequestDispatcher("/views/admin/admin.jsp").forward(req, resp);
                return;
            }

            if (req.getPathInfo().startsWith("/product")) {
                routerProduct(req, resp, req.getPathInfo());
            }
            if (req.getPathInfo().startsWith("/transaction")) {
                routerTransaction(req, resp, req.getPathInfo());
            }
            if (req.getPathInfo().startsWith("/category")) {
                routerCategory(req, resp, req.getPathInfo());
            }
            if (req.getPathInfo().startsWith("/stock")) {
                routerStock(req, resp, req.getPathInfo());
            }

        }
    }

    public void routerStock(HttpServletRequest req, HttpServletResponse resp, String pathInfo) throws ServletException, SQLException, IOException {
        switch (pathInfo) {
            case "/stock":
            case "/stock/search":
                stockService.searchStock(req, resp);
                break;
            case "/stock/add-product-to-stock":
                stockService.addProductToStock(req, resp);
                break;
        }
    }

    public void routerProduct(HttpServletRequest req, HttpServletResponse resp, String pathInfo) throws ServletException, SQLException, IOException {
        switch (pathInfo) {
            case "/search":
            case "/product/search":
                adminService.renderAdminFistTab(req, resp);
                break;
            case "/product/product-create":
                adminService.renderCreateProductForm(req, resp);
                break;
            case "/product/update":
                adminService.renderUpdateProductForm(req, resp);
                break;
            case "/product/delete":
                req.setAttribute("renderProduct", true);
                adminService.cancelProduct(req, resp);
                break;
        }
    }

    public void routerCategory(HttpServletRequest req, HttpServletResponse resp, String pathInfo) throws ServletException, SQLException, IOException {
        switch (pathInfo) {
            case "/category":
                req.setAttribute("renderCategory", true);
                req.setAttribute("renderProduct", false);
                req.setAttribute("renderOrder", false);
                adminService.renderSearchCategory(req, resp);
                break;
            case "/category/search":
                adminService.renderSearchCategory(req, resp);
                break;
            case "/category/category-create":
                adminService.renderCreateCategoryForm(req, resp);
                break;
            case "/category/update":
                adminService.renderUpdateCategoryForm(req, resp);
                break;
            case "/category/delete":
                adminService.cancelCategory(req, resp);
                break;
            default:
                req.setAttribute("renderProduct", true);
                req.setAttribute("renderCategory", false);
                req.setAttribute("renderOrder", false);
                adminService.renderAdminFistTab(req, resp);
                break;
        }
    }

    public void routerTransaction(HttpServletRequest req, HttpServletResponse resp, String pathInfo) throws ServletException, SQLException, IOException {
        switch (pathInfo) {
            case "/transaction":
                adminService.renderSearchOrder(req, resp);
                break;
            case "/transaction/search":
                adminService.renderSearchOrder(req, resp);
                break;
            case "/transaction/detail":
                adminService.renderDetailOrderForm(req, resp);
                break;
            case "/transaction/confirm":
                adminService.confirmOrder(req, resp, "confirm");
                break;
            case "/transaction/delete":
                adminService.confirmOrder(req, resp, "cancel");
                break;
            case "/transaction/complete":
                adminService.confirmOrder(req, resp, "complete");
                break;
        }
    }


    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        boolean valid = validateLogin(req, resp);
        if (valid) {
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
                case "/category/category-create":
                    adminService.createNewCategory(req, resp);
                    break;
                case "/category/update":
                    adminService.updateCategory(req, resp);
                    break;

            }
        }
    }

    public boolean validateLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession httpSession = req.getSession();
        if (httpSession.getAttribute("token") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return false;
        } else {
            UserInfoDto userInfoDto = new Gson().fromJson((String) httpSession.getAttribute("userInfo"), UserInfoDto.class);
            req.setAttribute("userInfo", userInfoDto);
        }
        return true;
    }
}
