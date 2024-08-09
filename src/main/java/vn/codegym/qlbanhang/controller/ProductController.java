package vn.codegym.qlbanhang.controller;

import vn.codegym.qlbanhang.service.HomeService;
import vn.codegym.qlbanhang.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@WebServlet({"/product/*"})
public class ProductController extends HttpServlet {
    private ProductService productService;
    private HomeService homeService;

    @Override
    public void init() {
        this.productService = new ProductService();
        this.homeService = new HomeService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if (req.getPathInfo() == null) {
            homeService.renderHomePage(req, resp);
            return;
        }
        switch (req.getPathInfo()) {
            case "/search":
                productService.searchProduct(req, resp);
                break;
            case "/detail":
                productService.renderProductDetailPage(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        if (request.getPathInfo() == null) {

        }
        switch (request.getPathInfo()) {
            case "/search":
                break;
            case "/create-note":
                break;
            case "/update-note":
                break;
        }
    }
}
