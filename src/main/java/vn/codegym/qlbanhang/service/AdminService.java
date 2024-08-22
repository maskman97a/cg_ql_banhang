package vn.codegym.qlbanhang.service;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.codegym.qlbanhang.constants.Const;
import vn.codegym.qlbanhang.dto.*;
import vn.codegym.qlbanhang.dto.response.BaseResponse;
import vn.codegym.qlbanhang.entity.CategoryEntity;
import vn.codegym.qlbanhang.entity.OrderEntity;
import vn.codegym.qlbanhang.entity.ProductEntity;
import vn.codegym.qlbanhang.model.CategoryModel;
import vn.codegym.qlbanhang.model.OrderModel;
import vn.codegym.qlbanhang.model.ProductModel;
import vn.codegym.qlbanhang.utils.DataUtil;
import vn.codegym.qlbanhang.utils.SftpUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@MultipartConfig
public class AdminService extends BaseService {
    public static final AdminService inst = new AdminService();

    public static AdminService getInstance() {
        return inst;
    }

    public final ProductModel productModel;
    private final CategoryModel categoryModel;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final StockService stockService;
    private final OrderModel orderModel;

    private AdminService() {
        super(null);
        this.categoryService = CategoryService.getInstance();
        this.orderService = new OrderService();

        this.productModel = ProductModel.getInstance();
        this.categoryModel = CategoryModel.getInstance();
        this.orderModel = OrderModel.getInstance();
        this.stockService = StockService.getInstance();

    }

    public void setNavigationBar(HttpServletRequest req) {
        List<UrlLevelDto> urlLevelDtos = new ArrayList<>();
        StringBuilder parentPath = new StringBuilder(req.getContextPath());
        int index = 1;
        for (String path : req.getRequestURI().split("/")) {
            if (!path.isEmpty()) {
                parentPath.append("/").append(path);
                String urlName = Const.getUrlPathName(path);
                if (urlName.isEmpty()) {
                    continue;
                }
                UrlLevelDto urlLevelDto = new UrlLevelDto();
                urlLevelDto.setUrl(parentPath.toString());
                urlLevelDto.setLevel(index++);
                urlLevelDto.setName(urlName);
                urlLevelDtos.add(urlLevelDto);
            }
        }
        req.setAttribute("urlLevelList", urlLevelDtos);
    }


    public void renderAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        HttpSession httpSession = req.getSession();
        if (httpSession.getAttribute("token") != null) {
            String userInfoJson = (String) httpSession.getAttribute("userInfo");
            UserInfoDto userInfo = gson.fromJson(userInfoJson, UserInfoDto.class);
            req.setAttribute("userInfo", userInfo);
        }
        req.setAttribute("renderProduct", true);
        req.setAttribute("renderCategory", false);
        req.setAttribute("renderOrder", false);
        req.setAttribute("renderStock", false);
        this.searchProductAdmin(req, resp);
        req.getRequestDispatcher("/views/admin/admin.jsp").forward(req, resp);
    }

    public void renderAdminFistTab(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        this.searchProductAdmin(req, resp);
        req.setAttribute("renderProduct", true);
        req.setAttribute("renderProductList", true);
        req.getRequestDispatcher("/views/admin/admin.jsp").forward(req, resp);
    }

    public void renderCreateProductForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        req.setAttribute("renderProductCreate", true);
        req.setAttribute("renderProduct", true);
        List<CategoryDto> categoryDtoList = categoryModel.getLstCategory();
        req.setAttribute("lstCategory", categoryDtoList);
        req.getRequestDispatcher("/views/admin/admin.jsp").forward(req, resp);

    }

    public void renderUpdateProductForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        req.setAttribute("renderProductUpdate", true);
        req.setAttribute("renderProduct", true);
        Integer id = Integer.parseInt(req.getParameter("id"));
        ProductDto productDto = productModel.getDetailProduct(null, null, id);
        req.setAttribute("product", productDto);
        req.getRequestDispatcher("/views/admin/admin.jsp").forward(req, resp);

    }

    public void searchProductAdmin(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        String keyword = req.getParameter("keyword");
        Long categoryId = null;
        if (!DataUtil.isNullOrEmpty(req.getParameter("category-id"))) {
            categoryId = DataUtil.safeToLong(req.getParameter("category-id"));
        }
        int size = 5;
        if (req.getParameter("size") != null) {
            size = Integer.parseInt(req.getParameter("size"));
        }
        int page = 1;
        if (req.getParameter("page") != null) {
            page = Integer.parseInt(req.getParameter("page"));
            if (page == 0) page = 1;
        }
        req.setAttribute("currentPage", page);
        req.getAttribute("currentPage");
        baseSearchDto.setKeyword(keyword);
        baseSearchDto.setSize(size);
        baseSearchDto.setPage(page);
        List<ProductDto> lstData = productModel.findProductByKeyword(baseSearchDto, categoryId, null);
        if (lstData != null && !lstData.isEmpty()) {
            int index = 1;
            DecimalFormat df = new DecimalFormat("#,###");
            for (ProductDto productDto : lstData) {
                productDto.setIndex(index++);
                productDto.setStrPrice(df.format(productDto.getPrice()));
                productDto.setStrQuantity(df.format(productDto.getQuantity()));
            }
            req.setAttribute("lstData", lstData);
        }
        List<CategoryDto> categoryDtoList = categoryModel.getLstCategory();
        req.setAttribute("lstCategory", categoryDtoList);
        int count = productModel.countProduct(baseSearchDto, categoryId, null);
        getPaging(req, resp, count, size, page);

    }

    public void createNewProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        try {
            req.setCharacterEncoding("UTF-8");
            if (DataUtil.isNullObject(req.getPart("file"))) {
                req.setAttribute("errorMsg", "Ảnh sản phẩm bắt buộc nhập");
                renderCreateProductForm(req, resp);
            } else if (DataUtil.isNullOrEmpty(req.getParameter("code"))) {
                req.setAttribute("errorMsg", "Mã sản phẩm bắt buộc nhập");
                renderCreateProductForm(req, resp);
            } else if (DataUtil.isNullOrEmpty(req.getParameter("category-id"))) {
                req.setAttribute("errorMsg", "Loại sản phẩm bắt buộc nhập");
                renderCreateProductForm(req, resp);
            } else if (DataUtil.isNullOrEmpty(req.getParameter("name"))) {
                req.setAttribute("errorMsg", "Tên sản phẩm bắt buộc nhập");
                renderCreateProductForm(req, resp);
            } else if (DataUtil.isNullOrEmpty(req.getParameter("quantity"))) {
                req.setAttribute("errorMsg", "Số lượng bắt buộc nhập");
                renderCreateProductForm(req, resp);
            } else if (DataUtil.isNullOrEmpty(req.getParameter("price"))) {
                req.setAttribute("errorMsg", "Giá sản phẩm bắt buộc nhập");
                renderCreateProductForm(req, resp);
            } else if (!DataUtil.isNullOrEmpty(req.getParameter("description")) && req.getParameter("description").length() > 500) {
                req.setAttribute("errorMsg", "Mô tả nhập quá 500 ký tự");
                renderCreateProductForm(req, resp);
            } else {
                if (productModel.checkExitsProduct(req.getParameter("code"), Integer.parseInt(req.getParameter("category-id")), null)) {
                    req.setAttribute("errorMsg", "Tồn tại sản phẩm có mã " + req.getParameter("code") + " trên hệ thống!");
                    renderCreateProductForm(req, resp);
                } else {
                    ProductEntity productEntity = new ProductEntity();
                    String imageUrl = SftpUtils.getPathSFTP(req, resp);
                    productEntity.setImageUrl(imageUrl);
                    productEntity.setProductCode(req.getParameter("code"));
                    productEntity.setProductName(req.getParameter("name"));
                    productEntity.setCategoryId(Integer.parseInt(req.getParameter("category-id")));
                    productEntity.setQuantity(Integer.parseInt(req.getParameter("quantity")));
                    productEntity.setPrice(DataUtil.safeToLong(req.getParameter("price")));
                    productEntity.setDescription(req.getParameter("description"));
                    productEntity = (ProductEntity) productModel.save(productEntity);
                    if (!DataUtil.isNullObject(productEntity)) {
                        req.setAttribute("successMsg", "Thêm mới sản phẩm thành công");
                        resp.sendRedirect("/admin/product");
                    } else {
                        req.setAttribute("errorMsg", "Thêm mới sản phẩm không thành công");
                        renderAdminFistTab(req, resp);
                    }
                }
            }
        } catch (Exception ex) {
            this.renderAdmin(req, resp);
//            throw new ServletException(ex.getMessage());
        }
    }

    public void updateProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        try {
            req.setCharacterEncoding("UTF-8");
            if (DataUtil.isNullOrEmpty(req.getParameter("code"))) {
                req.setAttribute("errorMsg", "Mã sản phẩm bắt buộc nhập");
                renderUpdateProductForm(req, resp);
            } else if (DataUtil.isNullOrEmpty(req.getParameter("category-id"))) {
                req.setAttribute("errorMsg", "Loại sản phẩm bắt buộc nhập");
                renderUpdateProductForm(req, resp);
            } else if (DataUtil.isNullOrEmpty(req.getParameter("name"))) {
                req.setAttribute("errorMsg", "Tên sản phẩm bắt buộc nhập");
                renderUpdateProductForm(req, resp);
            } else if (DataUtil.isNullOrEmpty(req.getParameter("quantity"))) {
                req.setAttribute("errorMsg", "Số lượng bắt buộc nhập");
                renderUpdateProductForm(req, resp);
            } else if (DataUtil.isNullOrEmpty(req.getParameter("price"))) {
                req.setAttribute("errorMsg", "Giá sản phẩm bắt buộc nhập");
                renderUpdateProductForm(req, resp);
            } else if (!DataUtil.isNullOrEmpty(req.getParameter("description")) && req.getParameter("description").length() > 500) {
                req.setAttribute("errorMsg", "Mô tả nhập quá 500 ký tự");
                renderUpdateProductForm(req, resp);
            } else {
                if (productModel.checkExitsProduct(req.getParameter("code"), Integer.parseInt(req.getParameter("category-id")), Integer.parseInt(req.getParameter("id")))) {
                    req.setAttribute("errorMsg", "Tồn tại sản phẩm có mã " + req.getParameter("code") + " trên hệ thống!");
                    throw new ServletException("Tồn tại sản phẩm có mã " + req.getParameter("code") + " trên hệ thống!");
//                    renderUpdateProductForm(req, resp);
                } else {
                    ProductEntity productEntity = new ProductEntity();
                    Integer id = Integer.parseInt(req.getParameter("id"));
                    productEntity.setId(id);
                    if (!DataUtil.isNullObject(req.getPart("file"))) {
                        String imageUrl = SftpUtils.getPathSFTP(req, resp);
                        productEntity.setImageUrl(imageUrl);
                    }
                    productEntity.setCategoryId(!DataUtil.isNullOrEmpty(req.getParameter("category-id")) ? Integer.parseInt(req.getParameter("category-id")) : null);
                    productEntity.setProductCode(!DataUtil.isNullOrEmpty(req.getParameter("code")) ? req.getParameter("code") : null);
                    productEntity.setProductName(!DataUtil.isNullOrEmpty(req.getParameter("name")) ? req.getParameter("name") : null);
                    productEntity.setQuantity(!DataUtil.isNullOrEmpty(req.getParameter("quantity")) ? Integer.parseInt(req.getParameter("quantity")) : null);
                    productEntity.setPrice(!DataUtil.isNullOrEmpty(req.getParameter("price")) ? Long.valueOf(req.getParameter("price")) : null);
                    productEntity.setDescription(!DataUtil.isNullOrEmpty(req.getParameter("description")) ? req.getParameter("description") : null);
                    int save = productModel.updateProduct(false, productEntity);
                    if (save == 1) {
                        req.setAttribute("successMsg", "Cập nhật sản phẩm thành công");
                        resp.sendRedirect("/admin/product");
                    } else {
                        req.setAttribute("errorMsg", "Cập nhật sản phẩm không thành công");
                        renderUpdateProductForm(req, resp);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.renderAdmin(req, resp);
        }
    }

    public void cancelProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        try {
            Integer id = Integer.parseInt(req.getParameter("id"));
            ProductEntity productEntity = new ProductEntity();
            productEntity.setId(id);
            productEntity.setStatus(Const.STATUS_UNACTIVE);
            productEntity.setUpdatedBy("admin");
            int save = productModel.updateProduct(true, productEntity);
            if (save == 1) {
                resp.sendRedirect("/admin/product");
            } else {
                req.setAttribute("errorMsg", "Cập nhật sản phẩm không thành công");
                renderAdminFistTab(req, resp);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.renderAdmin(req, resp);
        }
    }


    public void renderSearchCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        categoryService.renderSearchCategory(req, resp);

    }

    public void renderCreateCategoryForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("renderCategory", true);
        req.setAttribute("renderCreateCategory", true);
        req.getRequestDispatcher("/views/admin/admin.jsp").forward(req, resp);

    }

    public void renderUpdateCategoryForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        req.setAttribute("renderCategory", true);
        req.setAttribute("renderUpdateCategory", true);
        Integer id = Integer.parseInt(req.getParameter("id"));
        CategoryDto category = categoryService.getDetailCategory(null, id);
        req.setAttribute("category", category);
        req.getRequestDispatcher("/views/admin/admin.jsp").forward(req, resp);

    }

    public void createNewCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        try {
            req.setCharacterEncoding("UTF-8");
            CategoryEntity categoryEntity = new CategoryEntity();
            if (DataUtil.isNullOrEmpty(req.getParameter("name"))) {
                req.setAttribute("errorMsg", "Tên loại sản phẩm bắt buộc nhập");
                renderCreateCategoryForm(req, resp);
            } else {
                categoryEntity.setName(DataUtil.safeToString(req.getParameter("name")).trim());
                categoryEntity = (CategoryEntity) categoryModel.save(categoryEntity);
                if (!DataUtil.isNullObject(categoryEntity)) {
                    req.setAttribute("successMsg", "Thêm Loại sản phẩm thành công");
                    resp.sendRedirect("/admin/category");
                } else {
                    req.setAttribute("errorMsg", "Thêm Loại sản phẩm không thành công");
                    renderCreateCategoryForm(req, resp);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.renderAdmin(req, resp);
        }
    }

    public void updateCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        try {
            req.setCharacterEncoding("UTF-8");
            if (DataUtil.isNullOrEmpty(req.getParameter("name"))) {
                req.setAttribute("errorMsg", "Tên loại sản phẩm bắt buộc nhập");
                renderUpdateCategoryForm(req, resp);
            } else {
                CategoryEntity categoryEntity = new CategoryEntity();
                Integer id = Integer.parseInt(req.getParameter("id"));
                categoryEntity.setId(id);
                categoryEntity.setName(!DataUtil.isNullOrEmpty(req.getParameter("name")) ? req.getParameter("name") : null);
                categoryEntity.setUpdatedBy("admin");
                int save = categoryModel.updateCategory(false, categoryEntity);
                if (save == 1) {
                    req.setAttribute("successMsg", "Cập nhật Loại sản phẩm thành công");
                    resp.sendRedirect("/admin/category");
                } else {
                    req.setAttribute("errorMsg", "Cập nhật Loại sản phẩm không thành công");
                    renderUpdateCategoryForm(req, resp);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.renderAdmin(req, resp);
        }
    }

    public void cancelCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        req.setAttribute("renderCategory", true);
        req.setAttribute("renderProduct", false);
        req.setAttribute("renderOrder", false);
        try {
            Integer id = Integer.parseInt(req.getParameter("id"));
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setId(id);
            categoryEntity.setStatus(Const.STATUS_UNACTIVE);
            categoryEntity.setUpdatedBy("admin");
            int save = categoryModel.updateCategory(true, categoryEntity);
            if (save == 1) {
                req.setAttribute("successMsg", "Cập nhật Loại sản phẩm thành công");
                resp.sendRedirect("/admin/category");
            } else {
                req.setAttribute("errorMsg", "Cập nhật Loại sản phẩm không thành công");
                renderSearchCategory(req, resp);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.renderAdmin(req, resp);
        }
    }

    public void renderSearchOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        req.setAttribute("renderOrderAdmin", false);
        orderService.renderSearchOrderAdmin(req, resp);

    }


    public void renderDetailOrderForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {

        req.setAttribute("renderOrder", true);
        req.setAttribute("renderOrderAdmin", true);
        orderService.detailOrderForAdmin(req, resp);
        req.getRequestDispatcher("/views/admin/admin.jsp").forward(req, resp);

    }

    public void confirmOrder(HttpServletRequest req, HttpServletResponse resp, String action) throws ServletException, IOException, SQLException {
        req.setAttribute("renderOrder", true);
        Integer orderId = Integer.parseInt(req.getParameter("id"));
        OrderEntity orderEntity = (OrderEntity) orderModel.findById(orderId);
        switch (action) {
            case "confirm":
                orderEntity.setStatus(Const.OrderStatus.ACCEPTED);
                break;
            case "complete":
                orderEntity.setStatus(Const.OrderStatus.COMPLETED);
                break;
            case "cancel":
                orderEntity.setStatus(Const.OrderStatus.CANCELED);
                break;
        }
        orderEntity.setUpdatedBy("admin");
        int save = orderModel.updateOrder(orderEntity, action);
        if (save == 1) {
            req.setAttribute("successMsg", "Cập nhật đơn hàng thành công");
            resp.sendRedirect("/admin/transaction");
        } else {
            req.setAttribute("errorMsg", "Cập nhật đơn hàng thất bại. Vui lòng kiểm tra lại!");
//                resp.sendRedirect("/admin/transaction");
            renderSearchOrder(req, resp);
        }

    }


    public void updateStock(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        try {
            req.setCharacterEncoding("UTF-8");
            BaseResponse<StockDto> baseResponse = stockService.addProductToStock(req, resp);
            if (!DataUtil.isNullObject(baseResponse)) {
                req.setAttribute("successMsgStock", "Cập nhật kho thành công");
                resp.sendRedirect("/admin/stock");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.renderAdmin(req, resp);
        }
    }
}
