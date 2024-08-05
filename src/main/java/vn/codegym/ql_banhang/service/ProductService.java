package vn.codegym.ql_banhang.service;

import vn.codegym.ql_banhang.model.ProductModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProductService extends BaseService {
    private ProductModel productModel;

    public ProductService() {
        super(new ProductModel());
        this.productModel = (ProductModel) super.baseModel;
    }

    public void findListProduct(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("lstProduct", productModel.findProduct());

    }
}
