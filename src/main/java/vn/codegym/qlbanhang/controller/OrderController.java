package vn.codegym.qlbanhang.controller;

import vn.codegym.qlbanhang.service.HomeService;
import vn.codegym.qlbanhang.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet({"/order/*"})
public class OrderController extends HttpServlet {
    private OrderService orderService;
    private HomeService homeService;

    @Override
    public void init() {
        this.orderService = new OrderService();
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
            case "/success":
                orderService.renderOrderSuccessPage(req, resp);
                break;
            case "/error":
                orderService.renderOrderErrorPage(req, resp);
                break;
            case "/lookup":
                orderService.renderLookupOrderPage(req, resp);
                break;
            case "/lookup-by-code":
                orderService.executeLookupOrder(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (request.getPathInfo() == null) {
            orderService.renderErrorPage(request, response);
        }
        switch (request.getPathInfo()) {
            case "/create":
                orderService.executeCreateOrder(request, response);
                return;
            case "/cancel":
                orderService.executeCancelOrder(request, response);
                return;
        }
        orderService.renderErrorPage(request, response);
    }
}
