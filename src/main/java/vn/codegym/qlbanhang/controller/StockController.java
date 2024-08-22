package vn.codegym.qlbanhang.controller;


import lombok.SneakyThrows;
import vn.codegym.qlbanhang.service.StockService;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/stock/*"})
@MultipartConfig
public class StockController extends BaseController {
    private StockService stockService;

    public void init() {
        this.stockService = StockService.getInstance();
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

    }


    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

    }
}
