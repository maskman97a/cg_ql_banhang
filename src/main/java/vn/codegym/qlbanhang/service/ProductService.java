package vn.codegym.qlbanhang.service;

import com.google.gson.Gson;
import vn.codegym.qlbanhang.dto.*;
import vn.codegym.qlbanhang.dto.response.BaseResponse;
import vn.codegym.qlbanhang.dto.response.CartResponse;
import vn.codegym.qlbanhang.entity.BaseData;
import vn.codegym.qlbanhang.entity.BaseEntity;
import vn.codegym.qlbanhang.entity.ProductEntity;
import vn.codegym.qlbanhang.entity.StockEntity;
import vn.codegym.qlbanhang.enums.ProductSort;
import vn.codegym.qlbanhang.model.CategoryModel;
import vn.codegym.qlbanhang.model.ProductModel;
import vn.codegym.qlbanhang.model.StockModel;
import vn.codegym.qlbanhang.utils.DataUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductService extends BaseService {
    private final ProductModel productModel;
    private final CategoryModel categoryModel;
    private final StockModel stockModel;

    public ProductService() {
        super(ProductModel.getInstance());
        this.productModel = ProductModel.getInstance();
        this.categoryModel = CategoryModel.getInstance();
        this.stockModel = StockModel.getInstance();
    }

    public void findListProduct(HttpServletRequest req, HttpServletResponse resp) {
        List<ProductEntity> productEntityList = productModel.findProduct();
        List<ProductDto> productDtoList = new ArrayList<>();
        for (ProductEntity productEntity : productEntityList) {
            productDtoList.add(modelMapper.map(productEntity, ProductDto.class));
        }
        req.setAttribute("lstProduct", productDtoList);
        req.setAttribute("showListProduct", true);
    }

    public void renderProductDetailPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {

        getAllCategory(req);
        Integer id = Integer.parseInt(req.getParameter("id"));
        ProductEntity productEntity = (ProductEntity) productModel.findById(id);
        ProductDto productDto = modelMapper.map(productEntity, ProductDto.class);
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        baseSearchDto.getQueryConditionDtos().add(QueryConditionDto.newAndCondition("product_id", "=", productEntity.getId()));
        StockEntity stockEntity = (StockEntity) stockModel.findOne(baseSearchDto);
        if (!DataUtil.isNullObject(stockEntity)) {
            productDto.setAvailableQuantity(stockEntity.getAvailableQuantity());
        }
        req.setAttribute("product", productDto);
        req.setAttribute("showProductDetail", true);
        renderPage(req, resp);

    }

    public void executeSearch(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        log.info("-----start-----");
        List<CategoryDto> categoryDtoList = getAllCategory(req);
        getAllProductSortType(req);
        String keyword = req.getParameter("keyword");
        Integer categoryId;
        if (!DataUtil.isNullOrEmpty(req.getParameter("categoryId"))) {
            categoryId = Integer.parseInt(req.getParameter("categoryId"));
        } else {
            categoryId = null;
        }
        Integer page = 1;
        String pageStr = req.getParameter("page");
        if (!DataUtil.isNullOrEmpty(pageStr)) {
            page = Integer.parseInt(pageStr);
        }
        Integer size = 16;
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
        List<ProductListPerCategoryDto> productListPerCategoryDtos = new ArrayList<>();
        if (DataUtil.isNullObject(categoryId)) {
            for (CategoryDto categoryDto : categoryDtoList) {
                ProductListPerCategoryDto productListPerCategoryDto = new ProductListPerCategoryDto();
                productListPerCategoryDto.setCategoryId(categoryDto.getId());
                productListPerCategoryDto.setCategoryName(categoryDto.getName());
                BaseData baseData = productModel.findProductByKeywordAndCategoryId(keyword, categoryDto.getId(), sortCol, sortType, page, size);
                if (DataUtil.isNullOrEmpty(baseData.getLstData())) {
                    continue;
                }
                ProductPagingDto productPagingDto = new ProductPagingDto();
                int productPage = 1;
                int countProductInPage = 1;
                for (BaseEntity baseEntity : baseData.getLstData()) {
                    if (countProductInPage == 1) {
                        productPagingDto = new ProductPagingDto();
                        productListPerCategoryDto.getProductPagingList().add(productPagingDto);
                    }


                    ProductDto productDto = modelMapper.map(baseEntity, ProductDto.class);

                    productPagingDto.getProductList().add(productDto);
                    productPagingDto.setPage(productPage++);
                    if (countProductInPage == 4) {
                        countProductInPage = 0;
                        productPage++;
                    }
                    countProductInPage++;
                }

                productListPerCategoryDtos.add(productListPerCategoryDto);
            }
        } else {
            CategoryDto categoryDto = categoryDtoList.stream().filter(x -> x.getId().equals(categoryId)).findFirst().get();
            ProductListPerCategoryDto productListPerCategoryDto = new ProductListPerCategoryDto();
            productListPerCategoryDto.setCategoryName(categoryDto.getName());
            productListPerCategoryDto.setCategoryId(categoryId);
            BaseData baseData = productModel.findProductByKeywordAndCategoryId(keyword, categoryDto.getId(), sortCol, sortType, page, size);
            ProductPagingDto productPagingDto = new ProductPagingDto();
            int productPage = 1;
            int countProductInPage = 1;
            for (BaseEntity baseEntity : baseData.getLstData()) {
                if (countProductInPage == 1) {
                    productPagingDto = new ProductPagingDto();
                    productListPerCategoryDto.getProductPagingList().add(productPagingDto);
                }
                productPagingDto.getProductList().add(modelMapper.map(baseEntity, ProductDto.class));
                productPagingDto.setPage(productPage);

                if (countProductInPage == 4) {
                    countProductInPage = 0;
                    productPage++;
                }
                countProductInPage++;

            }

            productListPerCategoryDto.setPaging(getPaging(req, resp, baseData.getTotalRow(), size, page));
            productListPerCategoryDtos.add(productListPerCategoryDto);
        }

        req.setAttribute("productPerCategoryList", productListPerCategoryDtos);
        req.setAttribute("showListProduct", true);
        req.setAttribute("keyword", keyword);
        req.setAttribute("categoryId", categoryId);
        log.info("-----end-----");
    }

    public void getAllProductSortType(HttpServletRequest req) {
        req.setAttribute("sortList", ProductSort.getAllSort());
    }

    public void searchProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        executeSearch(req, resp);
        renderPage(req, resp);

    }

    public void addToCart(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {

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

    public void getCart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("-----start-----");
        HttpSession session = req.getSession();
        Object cartProductJson = session.getAttribute("cartProductJson");
        List<CartProductDto> cartProductDtoList = new ArrayList<>();
        if (!DataUtil.isNullObject(cartProductJson)) {
            cartProductDtoList = gson.fromJson((String) cartProductJson, Cart.class).getCartProductList();
        }
        CartResponse cartResponse = new CartResponse(cartProductDtoList);
        BaseResponse<CartResponse> response = new BaseResponse<>();
        response.setAdditionalData(cartResponse);
        resp.setContentType("application/json; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(response));
        resp.getWriter().close();
        log.info("-----end-----");
    }

    public void removeProductFromCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("-----start-----");
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
        log.info("-----end-----");
    }

    public void updateCart(HttpServletRequest req, HttpServletResponse resp, Integer id, Integer newQuantity) throws SQLException, IOException {
        log.info("-----start-----");
        BaseResponse<CartResponse> baseResponse = new BaseResponse<>();
        HttpSession session = req.getSession();
        List<CartProductDto> cartProductDtoList;
        Object cartProductJson = session.getAttribute("cartProductJson");
        if (DataUtil.isNullObject(cartProductJson)) {
            cartProductDtoList = new ArrayList<>();
        } else {
            cartProductDtoList = gson.fromJson((String) cartProductJson, Cart.class).getCartProductList();
        }
        ProductEntity productEntity = productModel.findProductById(id);
        ProductDto productDto = modelMapper.map(productEntity, ProductDto.class);
        if (cartProductDtoList.stream().anyMatch(x -> x.getProduct().getId().equals(id))) {
            CartProductDto cartProductDto = cartProductDtoList.stream().filter(x -> x.getProduct().getId().equals(id)).findFirst().get();
            if (newQuantity != null) {
                cartProductDto.setQuantity(newQuantity);
            } else {
                cartProductDto.setQuantity(cartProductDto.getQuantity() + 1);
            }
            cartProductDto.setAmount(cartProductDto.getQuantity() * productDto.getPrice());
        } else {
            CartProductDto newCartProductDto = new CartProductDto(cartProductDtoList.size() + 1, productDto, 1);
            newCartProductDto.setAmount(productDto.getPrice());
            cartProductDtoList.add(newCartProductDto);
        }
        cartProductJson = gson.toJson(new Cart(cartProductDtoList));
        session.setAttribute("cartProductJson", cartProductJson);
        req.setAttribute("cartProductList", cartProductDtoList);
        CartResponse cartResponse = new CartResponse();
        cartResponse.setCartCount(cartProductDtoList.size());
        cartResponse.setCartProductList(cartProductDtoList);
        baseResponse.setAdditionalData(cartResponse);
        resp.getWriter().write(new Gson().toJson(baseResponse));
        resp.getWriter().close();
        log.info("-----end-----");
    }

    public List<CategoryDto> getAllCategory(HttpServletRequest req) throws SQLException {
        log.info("-----start-----");
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        List<BaseEntity> baseEntities = categoryModel.findAllActiveWithSort("sort", "ASC");
        for (BaseEntity baseEntity : baseEntities) {
            categoryDtoList.add(modelMapper.map(baseEntity, CategoryDto.class));
        }
        req.setAttribute("lstCategory", categoryDtoList);
        String selectedCategoryId = req.getParameter("categoryId");
        if (!DataUtil.isNullOrEmpty(selectedCategoryId)) {
            req.setAttribute("selectedCategoryId", Integer.parseInt(selectedCategoryId));
        }
        log.info("-----end-----");
        return categoryDtoList;

    }
}
