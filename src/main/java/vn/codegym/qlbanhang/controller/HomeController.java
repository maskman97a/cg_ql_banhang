package vn.codegym.qlbanhang.controller;

import vn.codegym.qlbanhang.service.HomeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet({"/home/*"})
public class HomeController extends BaseController {
    protected final Logger log = Logger.getLogger(HomeController.class.getName());
    private HomeService homeService;
    public void init() {
        homeService = HomeService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo() == null) {
            homeService.renderHomePage(req, resp);
        }
    }
}
