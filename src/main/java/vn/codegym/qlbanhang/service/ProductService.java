package vn.codegym.qlbanhang.service;

import com.google.gson.Gson;
import vn.codegym.qlbanhang.dto.Cart;
import vn.codegym.qlbanhang.dto.CartProductDto;
import vn.codegym.qlbanhang.dto.CategoryDto;
import vn.codegym.qlbanhang.dto.ProductDto;
import vn.codegym.qlbanhang.dto.response.BaseResponse;
import vn.codegym.qlbanhang.dto.response.CartResponse;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

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
            getAllCategory(req);
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
            getAllCategory(req);
            getAllProductSortType(req);
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

            getPaging(req, resp, baseData.getTotalRow(), size, page);


        } catch (Exception ex) {
            renderErrorPage(req, resp);
        }
    }

    public void getAllProductSortType(HttpServletRequest req) {
        req.setAttribute("sortList", ProductSort.getAllSort());
    }

    public void searchProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        executeSearch(req, resp);
        renderPage(req, resp);
    }

    public void addToCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productId = req.getParameter("productId");
        String newQuantityStr = req.getParameter("quantity");
        if (!DataUtil.isNullObject(productId)) {
            Integer newQuantity = null;
            if (newQuantityStr != null) {
                newQuantity = Integer.parseInt(newQuantityStr);
            }
            updateCart(req, resp, Integer.parseInt(productId), newQuantity);
        }
    }

    public void getCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Object cartProductJson = session.getAttribute("cartProductJson");
        List<CartProductDto> cartProductDtoList = new ArrayList<>();
        if (!DataUtil.isNullObject(cartProductJson)) {
            cartProductDtoList = gson.fromJson((String) cartProductJson, Cart.class).getCartProductList();
        }
        CartResponse cartResponse = new CartResponse(cartProductDtoList);
        BaseResponse<CartResponse> response = new BaseResponse<>();
        response.setAdditionalData(cartResponse);
        resp.getWriter().write(gson.toJson(response));
        resp.setContentType("application/json");
        resp.getWriter().close();
    }

    public void removeProductFromCart(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String productIdStr = req.getParameter("productId");
            if (productIdStr != null && !productIdStr.isEmpty()) {
                Integer productId = Integer.parseInt(productIdStr);
                HttpSession session = req.getSession();
                List<CartProductDto> cartProductDtoList;
                Object cartProductJson = session.getAttribute("cartProductJson");
                if (!DataUtil.isNullObject(cartProductJson)) {
                    cartProductDtoList = gson.fromJson((String) cartProductJson, Cart.class).getCartProductList();
                    Optional<CartProductDto> optional = cartProductDtoList.stream().filter(x -> x.getProduct().getId().equals(productId)).findFirst();
                    if (optional.isPresent()) {
                        cartProductDtoList.remove(optional.get());
                        cartProductJson = gson.toJson(new Cart(cartProductDtoList));
                        session.setAttribute("cartProductJson", cartProductJson);
                    }
                }
                getCart(req, resp);

            }
        } catch (Exception ex) {
            log.log(Level.WARNING, ex.getMessage());
        }
    }

    public void updateCart(HttpServletRequest req, HttpServletResponse resp, Integer id, Integer newQuantity) {
        try {
            BaseResponse<CartResponse> baseResponse = new BaseResponse();
            HttpSession session = req.getSession();
            List<CartProductDto> cartProductDtoList;
            Object cartProductJson = session.getAttribute("cartProductJson");
            if (DataUtil.isNullObject(cartProductJson)) {
                cartProductDtoList = new ArrayList<>();
            } else {
                cartProductDtoList = gson.fromJson((String) cartProductJson, Cart.class).getCartProductList();
            }
            Product product = productModel.findProductById(id);
            ProductDto productDto = modelMapper.map(product, ProductDto.class);
            if (cartProductDtoList.stream().anyMatch(x -> x.getProduct().getId().equals(id))) {
                CartProductDto cartProductDto = cartProductDtoList.stream().filter(x -> x.getProduct().getId().equals(id)).findFirst().get();
                if (newQuantity != null) {
                    cartProductDto.setQuantity(newQuantity);
                } else {
                    cartProductDto.setQuantity(cartProductDto.getQuantity() + 1);
                }
                cartProductDto.setAmount(cartProductDto.getQuantity() * productDto.getPrice().intValue());
            } else {
                CartProductDto newCartProductDto = new CartProductDto(cartProductDtoList.size() + 1, productDto, 1);
                newCartProductDto.setAmount(productDto.getPrice().intValue());
                cartProductDtoList.add(newCartProductDto);
            }
            cartProductJson = gson.toJson(new Cart(cartProductDtoList));
            session.setAttribute("cartProductJson", cartProductJson);
            req.setAttribute("cartProductList", cartProductDtoList);
            resp.setContentType("application/json");
            CartResponse cartResponse = new CartResponse();
            cartResponse.setCartCount(cartProductDtoList.size());
            cartResponse.setCartProductList(cartProductDtoList);
            baseResponse.setAdditionalData(cartResponse);
            resp.getWriter().write(new Gson().toJson(baseResponse));
            resp.getWriter().close();
        } catch (Exception ex) {
            log.log(Level.WARNING, ex.getMessage());
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
}
