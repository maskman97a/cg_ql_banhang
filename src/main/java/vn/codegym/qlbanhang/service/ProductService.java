package vn.codegym.qlbanhang.service;

import vn.codegym.qlbanhang.dto.ProductDto;
import vn.codegym.qlbanhang.entity.Product;
import vn.codegym.qlbanhang.model.ProductModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProductService extends HomeService {
    private final ProductModel productModel;

    public ProductService() {
        super(new ProductModel());
        this.productModel = (ProductModel) super.baseModel;
    }

    public void findListProduct(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("lstProduct", productModel.findProduct());
        req.setAttribute("showListProduct", true);
    }

    public void renderProductDetailPage(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Integer id = Integer.parseInt(req.getParameter("id"));
            Product product = (Product) productModel.findById(id);
            ProductDto productDto = modelMapper.map(product, ProductDto.class);
            req.setAttribute("product", productDto);
            req.setAttribute("showProductDetail", true);
            renderPage(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp);
        }
    }
}
