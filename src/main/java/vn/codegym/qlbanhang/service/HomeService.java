package vn.codegym.qlbanhang.service;


import vn.codegym.qlbanhang.dto.ProductDto;
import vn.codegym.qlbanhang.model.BaseModel;
import vn.codegym.qlbanhang.utils.DataUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeService extends BaseService {
    private ProductService productService;

    public HomeService() {
        super(null);
        this.productService = new ProductService();
    }

    public HomeService(BaseModel baseModel) {
        super(baseModel);
    }

    public BaseModel getBaseModal() {
        return super.baseModel;
    }


    public void renderHomePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            productService.executeSearch(req, resp);
            renderPage(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp);
        }
    }

    protected void renderPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        Object productList = session.getAttribute("cartProductList");
        if (DataUtil.isNullObject(productList)) {
            productList = new ArrayList<ProductDto>();
        }
        req.setAttribute("cartCount", ((List) productList).size());
        if (DataUtil.safeEqual(req.getAttribute("renderAdmin"), "true")){
            req.getRequestDispatcher(req.getContextPath() + "/views/admin/admin.jsp").forward(req, resp);
        }else {
            req.getRequestDispatcher(req.getContextPath() + "/views/home/home.jsp").forward(req, resp);
        }
    }


}
