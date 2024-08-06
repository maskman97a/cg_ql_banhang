package vn.codegym.qlbanhang.service;

import vn.codegym.qlbanhang.dto.ProductDto;
import vn.codegym.qlbanhang.entity.Product;
import vn.codegym.qlbanhang.model.ProductModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class ProductService extends HomeService {
    private final ProductModel productModel;

    public ProductService() {
        super(new ProductModel());
        this.productModel = (ProductModel) super.baseModel;
    }

    public void findListProduct(HttpServletRequest req, HttpServletResponse resp) {
        List<Product> productList = productModel.findProduct();
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product : productList) {
            productDtoList.add(modelMapper.map(product, ProductDto.class));
        }
        req.setAttribute("lstProduct", productDtoList);
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
