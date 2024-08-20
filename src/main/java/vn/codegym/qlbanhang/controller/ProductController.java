package vn.codegym.qlbanhang.controller;

import lombok.SneakyThrows;
import vn.codegym.qlbanhang.service.HomeService;
import vn.codegym.qlbanhang.service.ProductService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@WebServlet({"/product/*"})
public class ProductController extends HomeController {
    private ProductService productService;
    private HomeService homeService;

    @Override
    public void init() {
        this.productService = new ProductService();
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        if (req.getPathInfo() == null) {
            homeService.renderHomePage(req, resp);
            return;
        }
        log.info(req.getPathInfo());
        switch (req.getPathInfo()) {
            case "/search":
                productService.searchProduct(req, resp);
                break;
            case "/detail":
                productService.renderProductDetailPage(req, resp);
                break;
            case "/add-to-cart":
            case "/update-cart":
                productService.addToCart(req, resp);
                break;
            case "/remove-from-cart":
                productService.removeProductFromCart(req, resp);
                break;
            case "/get-cart":
                productService.getCart(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        if (request.getPathInfo() == null) {

        }
        switch (request.getPathInfo()) {
            case "/search":
                break;
        }
    }
}
