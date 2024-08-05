package vn.codegym.ql_banhang.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet({"/", ""})
public class BaseController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (req.getPathInfo() == null || req.getPathInfo().equals("/")) {
                resp.sendRedirect("home");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
