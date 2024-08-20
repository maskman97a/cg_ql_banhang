package vn.codegym.qlbanhang.controller;

import vn.codegym.qlbanhang.service.HomeService;
import vn.codegym.qlbanhang.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet({"/order/*"})
public class OrderController extends HttpServlet {
    private OrderService orderService;
    private HomeService homeService;

    @Override
    public void init() {
        this.orderService = new OrderService();
        this.homeService = HomeService.getInstance();
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
                try {
                    orderService.renderOrderSuccessPage(req, resp);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/error":
                orderService.renderOrderErrorPage(req, resp);
                break;
            case "/lookup":
                orderService.renderLookupOrderPage(req, resp);
                break;
            case "/lookup-by-code":
                try {
                    orderService.executeLookupOrder(req, resp);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        switch (request.getPathInfo()) {
            case "/create":
                orderService.executeCreateOrderSingle(request, response);
                return;
            case "/create-order-batch":
                orderService.executeCreateOrderBatch(request, response);
                return;
            case "/cancel":
                orderService.executeCancelOrder(request, response);
                return;
        }
    }
}
