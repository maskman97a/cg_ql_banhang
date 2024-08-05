package vn.codegym.ql_banhang.controller;

import vn.codegym.ql_banhang.service.HomeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet({"/home/*"})
public class HomeController extends BaseController {
    private HomeService homeService;

    public void init() {
        this.homeService = new HomeService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo() == null) {
            homeService.renderHomePage(req, resp);
        }
    }
}
