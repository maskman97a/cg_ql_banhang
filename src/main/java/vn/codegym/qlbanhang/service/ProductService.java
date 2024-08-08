package vn.codegym.qlbanhang.service;

import vn.codegym.qlbanhang.dto.CategoryDto;
import vn.codegym.qlbanhang.dto.ProductDto;
import vn.codegym.qlbanhang.entity.BaseData;
import vn.codegym.qlbanhang.entity.BaseEntity;
import vn.codegym.qlbanhang.entity.Product;
import vn.codegym.qlbanhang.enums.ProductSort;
import vn.codegym.qlbanhang.model.CategoryModel;
import vn.codegym.qlbanhang.model.ProductModel;
import vn.codegym.qlbanhang.utils.DataUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductService extends HomeService {
    private final ProductModel productModel;
    private final CategoryModel categoryModel;

    public ProductService() {
        super(new ProductModel());
        this.productModel = (ProductModel) super.baseModel;
        this.categoryModel = new CategoryModel();
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

    public void renderProductDetailPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

    public void executeSearch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String keyword = req.getParameter("keyword");
            Integer categoryId = null;
            if (!DataUtil.isNullOrEmpty(req.getParameter("categoryId"))) {
                categoryId = Integer.parseInt(req.getParameter("categoryId"));
            }
            Integer page = 1;
            String pageStr = req.getParameter("page");
            if (!DataUtil.isNullOrEmpty(pageStr)) {
                page = Integer.parseInt(pageStr);
            }
            Integer size = 8;
            String sizeStr = req.getParameter("size");
            if (!DataUtil.isNullOrEmpty(sizeStr)) {
                size = Integer.parseInt(sizeStr);
            }

            String sortCol = req.getParameter("sortCol");
            if (!DataUtil.isNullOrEmpty(sortCol)) {
                req.setAttribute("sortCol", sortCol);
            }
            String sortType = req.getParameter("sortType");
            if (!DataUtil.isNullOrEmpty(sortType)) {
                req.setAttribute("sortType", sortType);
            }
            if (!DataUtil.isNullOrEmpty(sortCol) && !DataUtil.isNullOrEmpty(sortType)) {
                ProductSort productSort = ProductSort.getProductSort(sortCol, sortType);
                assert productSort != null;
                req.setAttribute("selectedSort", productSort.getName());
            }
            BaseData baseData = productModel.findProductByKeywordAndCategoryId(keyword, categoryId, sortCol, sortType, page, size);
            List<ProductDto> productDtoList = new ArrayList<>();
            for (BaseEntity baseEntity : baseData.getLstData()) {
                productDtoList.add(modelMapper.map(baseEntity, ProductDto.class));
            }
            req.setAttribute("lstProduct", productDtoList);
            req.setAttribute("showListProduct", true);
            req.setAttribute("keyword", keyword);
            req.setAttribute("categoryId", categoryId);

            getAllCategory(req);

            getPaging(req, resp, baseData.getTotalRow(), size, page);

            getAllProductSortType(req);
        } catch (Exception ex) {
            renderErrorPage(req, resp);
        }
    }

    public void getAllCategory(HttpServletRequest req) {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        try {
            List<BaseEntity> baseEntities = categoryModel.findAll();
            for (BaseEntity baseEntity : baseEntities) {
                categoryDtoList.add(modelMapper.map(baseEntity, CategoryDto.class));
            }
            req.setAttribute("lstCategory", categoryDtoList);
            String selectedCategoryId = req.getParameter("categoryId");
            if (!DataUtil.isNullOrEmpty(selectedCategoryId)) {
                req.setAttribute("selectedCategoryId", Integer.parseInt(selectedCategoryId));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getAllProductSortType(HttpServletRequest req) {
        req.setAttribute("sortList", ProductSort.getAllSort());
    }

    public void searchProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        executeSearch(req, resp);
        renderPage(req, resp);
    }
}
