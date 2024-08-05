package vn.codegym.qlbanhang.service;


import vn.codegym.qlbanhang.model.BaseModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HomeService extends BaseService {
    private ProductService productService;

    public HomeService() {
        super(null);
        this.productService = new ProductService();
    }

    public HomeService(BaseModel baseModel) {
        super(baseModel);
    }


    public void renderHomePage(HttpServletRequest req, HttpServletResponse resp) {
        try {
            productService.findListProduct(req, resp);
            renderPage(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp);
        }
    }

    protected void renderPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(req.getContextPath() + "/views/home/home.jsp").forward(req, resp);
    }


}
