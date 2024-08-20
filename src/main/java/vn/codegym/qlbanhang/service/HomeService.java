package vn.codegym.qlbanhang.service;


import com.google.gson.Gson;
import vn.codegym.qlbanhang.model.BaseModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HomeService extends BaseService {
    private static final HomeService homeService = new HomeService();
    private ProductService productService;

    private HomeService() {
        super(null);
        this.productService = new ProductService();
    }

    public static HomeService getInstance() {
        return homeService;
    }

    public void renderHomePage(HttpServletRequest req, HttpServletResponse resp) {
        log.info("-----start-----");
        try {
            productService.executeSearch(req, resp);
            renderPage(req, resp);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        log.info("-----end-----");
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



}
