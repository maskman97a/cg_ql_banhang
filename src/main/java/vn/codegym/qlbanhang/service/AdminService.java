package vn.codegym.qlbanhang.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminService extends BaseService {
    public AdminService() {
        super(null);
    }


    public void renderAdmin(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.getRequestDispatcher("/views/admin/admin.jsp").forward(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp);
        }
    }

}
