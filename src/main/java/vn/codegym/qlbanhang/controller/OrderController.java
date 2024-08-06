package vn.codegym.qlbanhang.controller;

import vn.codegym.qlbanhang.service.HomeService;
import vn.codegym.qlbanhang.service.OrderService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getPathInfo() == null) {
            homeService.renderHomePage(req, resp);
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        if (request.getPathInfo() == null) {
            orderService.renderErrorPage(request, response);
        }
        switch (request.getPathInfo()) {
            case "/create":
                orderService.executeCreateOrder(request, response);
                return;
        }
        orderService.renderErrorPage(request, response);
    }
}
