package vn.codegym.ql_banhang.service;


import vn.codegym.ql_banhang.model.BaseModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeService extends BaseService {
    private ProductService productService;

    public HomeService() {
        super(null);
        this.productService = new ProductService();
    }

    public HomeService(BaseModel baseModel) {
        super(baseModel);
    }


    public void renderHomePage(HttpServletRequest req, HttpServletResponse resp) {
        try {
            productService.findListProduct(req, resp);
            req.getRequestDispatcher("views/home/home.jsp").forward(req, resp);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
