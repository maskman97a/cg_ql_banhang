package vn.codegym.qlbanhang.controller;


import com.google.gson.Gson;
import vn.codegym.qlbanhang.constants.Const;
import vn.codegym.qlbanhang.dto.UrlLevelDto;
import vn.codegym.qlbanhang.dto.UserInfoDto;
import vn.codegym.qlbanhang.service.AdminService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/*")
@MultipartConfig
public class AdminController extends BaseController {
    private AdminService adminService;

    public void init() {
        this.adminService = new AdminService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean valid = validateLogin(req, resp);
        if (valid) {
            List<UrlLevelDto> urlLevelDtos = new ArrayList<>();
            StringBuilder parentPath = new StringBuilder(req.getContextPath());
            int index = 1;
            for (String path : req.getRequestURI().split("/")) {
                if (!path.isEmpty()) {
                    parentPath.append("/").append(path);
                    String urlName = Const.getUrlPathName(path);
                    if (urlName.isEmpty()) {
                        continue;
                    }
                    UrlLevelDto urlLevelDto = new UrlLevelDto();
                    urlLevelDto.setUrl(parentPath.toString());
                    urlLevelDto.setLevel(index++);
                    urlLevelDto.setName(urlName);
                    urlLevelDtos.add(urlLevelDto);
                }
            }
            req.setAttribute("urlLevelList", urlLevelDtos);

            if (req.getPathInfo() == null) {
                req.setAttribute("renderMainAdmin", true);
                req.getRequestDispatcher("/views/admin/admin.jsp").forward(req, resp);
                return;
            }

            System.out.println(req.getPathInfo());
            switch (req.getPathInfo()) {
                case "/search":
                    adminService.renderAdminFistTab(req, resp);
                    break;
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
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
