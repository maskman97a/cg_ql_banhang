package vn.codegym.qlbanhang.service;


import com.google.gson.Gson;
import vn.codegym.qlbanhang.dto.Cart;
import vn.codegym.qlbanhang.dto.CartProductDto;
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

    public BaseModel getBaseModel() {
        return super.baseModel;
    }

    public Gson getGson() {
        return super.gson;
    }


    public void renderHomePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("-----start-----");
        try {
            productService.executeSearch(req, resp);
            renderPage(req, resp);
        } catch (Exception ex) {
            ex.printStackTrace();
            renderErrorPage(req, resp);
        }
        log.info("-----end-----");
    }

    protected void renderPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Object cartProductJson = session.getAttribute("cartProductJson");
        Cart cart;
        if (DataUtil.isNullObject(cartProductJson)) {
            cart = new Cart(new ArrayList<>());
        } else {
            cart = gson.fromJson((String) cartProductJson, Cart.class);
        }
        List<CartProductDto> cartProductDtoList = cart.getCartProductList();

        req.setAttribute("cartCount", cartProductDtoList.size());
        if (DataUtil.safeEqual(req.getAttribute("renderAdmin"), "true")) {
            req.getRequestDispatcher(req.getContextPath() + "/views/admin/admin.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher(req.getContextPath() + "/views/home/home.jsp").forward(req, resp);
        }
    }
}
