package vn.codegym.qlbanhang.service;

import vn.codegym.qlbanhang.constants.Const;
import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.CategoryDto;
import vn.codegym.qlbanhang.dto.ProductDto;
import vn.codegym.qlbanhang.dto.UserInfoDto;
import vn.codegym.qlbanhang.entity.Category;
import vn.codegym.qlbanhang.entity.Order;
import vn.codegym.qlbanhang.entity.Product;
import vn.codegym.qlbanhang.model.BaseModel;
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
import java.text.DecimalFormat;
import java.util.List;

@MultipartConfig
public class AdminService extends BaseService {
    public ProductModel productModel;
    private final CategoryModel categoryModel;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final OrderModel orderModel;

    public AdminService() {
        super(null);
        this.categoryService = new CategoryService();
        this.orderService = new OrderService();

        this.productModel = (ProductModel) BaseModel.getInstance(Product.class);
        this.categoryModel = (CategoryModel) BaseModel.getInstance(Category.class);
        this.orderModel = (OrderModel) BaseModel.getInstance(Order.class);
    }


    public void renderAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession httpSession = req.getSession();
            if (httpSession.getAttribute("token") != null) {
                String userInfoJson = (String) httpSession.getAttribute("userInfo");
                UserInfoDto userInfo = gson.fromJson(userInfoJson, UserInfoDto.class);
                req.setAttribute("userInfo", userInfo);
            }
            req.setAttribute("renderProduct", true);
            req.setAttribute("renderCategory", false);
            req.setAttribute("renderOrder", false);
            this.searchProductAdmin(req, resp);
            req.getRequestDispatcher("/views/admin/admin.jsp").forward(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp);
        }
    }

    public void renderAdminFistTab(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            this.searchProductAdmin(req, resp);
            req.setAttribute("renderProduct", true);
            req.setAttribute("renderProductList", true);
            req.getRequestDispatcher("/views/admin/admin.jsp").forward(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp);
        }
    }

    public void renderCreateProductForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("renderProductCreate", true);
            req.setAttribute("renderProduct", true);
            List<CategoryDto> categoryDtoList = categoryModel.getLstCategory();
            req.setAttribute("lstCategory", categoryDtoList);
            req.getRequestDispatcher("/views/admin/admin.jsp").forward(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp, ex.getMessage());
        }
    }

    public void renderUpdateProductForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("renderProductUpdate", true);
            req.setAttribute("renderProduct", true);
            Integer id = Integer.parseInt(req.getParameter("id"));
            ProductDto productDto = productModel.getDetailProduct(null, null, id);
            req.setAttribute("product", productDto);
            req.getRequestDispatcher("/views/admin/admin.jsp").forward(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp, ex.getMessage());
        }
    }

    public void searchProductAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        try {
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
        } catch (Exception ex) {
            renderErrorPage(req, resp, ex.getMessage());
        }
    }

    public void createNewProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
                req.setAttribute("errorMsg", "Mã sản phẩm bắt buộc nhập");
                renderCreateProductForm(req, resp);
            } else if (DataUtil.isNullOrEmpty(req.getParameter("quantity"))) {
                req.setAttribute("errorMsg", "Số lượng bắt buộc nhập");
                renderCreateProductForm(req, resp);
            } else if (DataUtil.isNullOrEmpty(req.getParameter("price"))) {
                req.setAttribute("errorMsg", "Giá sản phẩm bắt buộc nhập");
                renderCreateProductForm(req, resp);
            }else if (!DataUtil.isNullOrEmpty(req.getParameter("description")) && req.getParameter("description").length()> 500) {
                req.setAttribute("errorMsg", "Mô tả nhập quá 500 ký tự");
                renderCreateProductForm(req, resp);
            } else {
                Product product = new Product();
                String imageUrl = SftpUtils.getPathSFTP(req, resp);
                product.setImageUrl(imageUrl);
                product.setProductCode(req.getParameter("code"));
                product.setProductName(req.getParameter("name"));
                product.setCategoryId(Integer.parseInt(req.getParameter("category-id")));
                product.setQuantity(Integer.parseInt(req.getParameter("quantity")));
                product.setPrice(DataUtil.safeToLong(req.getParameter("price")));
                product.setDescription(req.getParameter("description"));
                product = (Product) productModel.save(product);
                if (!DataUtil.isNullObject(product)) {
                    req.setAttribute("successMsg", "Thêm mới sản phẩm thành công");
                    resp.sendRedirect("/admin/product");
                } else {
                    req.setAttribute("errorMsg", "Thêm mới sản phẩm không thành công");
                    renderAdminFistTab(req, resp);
                }
            }
        } catch (Exception ex) {
            this.renderAdmin(req, resp);
        }
    }

    public void updateProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");
            if (DataUtil.isNullOrEmpty(req.getParameter("code"))) {
                req.setAttribute("errorMsg", "Mã sản phẩm bắt buộc nhập");
                renderUpdateProductForm(req, resp);
            } else if (DataUtil.isNullOrEmpty(req.getParameter("category-id"))) {
                req.setAttribute("errorMsg", "Loại sản phẩm bắt buộc nhập");
                renderUpdateProductForm(req, resp);
            } else if (DataUtil.isNullOrEmpty(req.getParameter("name"))) {
                req.setAttribute("errorMsg", "Mã sản phẩm bắt buộc nhập");
                renderUpdateProductForm(req, resp);
            } else if (DataUtil.isNullOrEmpty(req.getParameter("quantity"))) {
                req.setAttribute("errorMsg", "Số lượng bắt buộc nhập");
                renderUpdateProductForm(req, resp);
            } else if (DataUtil.isNullOrEmpty(req.getParameter("price"))) {
                req.setAttribute("errorMsg", "Giá sản phẩm bắt buộc nhập");
                renderUpdateProductForm(req, resp);
            } else if (!DataUtil.isNullOrEmpty(req.getParameter("description")) && req.getParameter("description").length()> 500) {
                req.setAttribute("errorMsg", "Mô tả nhập quá 500 ký tự");
                renderUpdateProductForm(req, resp);
            } else {
                Product product = new Product();
                Integer id = Integer.parseInt(req.getParameter("id"));
                product.setId(id);
                if (!DataUtil.isNullObject(req.getPart("file"))) {
                    String imageUrl = SftpUtils.getPathSFTP(req, resp);
                    product.setImageUrl(imageUrl);
                }
                product.setCategoryId(!DataUtil.isNullOrEmpty(req.getParameter("category-id")) ? Integer.parseInt(req.getParameter("category-id")) : null);
                product.setProductCode(!DataUtil.isNullOrEmpty(req.getParameter("code")) ? req.getParameter("code") : null);
                product.setProductName(!DataUtil.isNullOrEmpty(req.getParameter("name")) ? req.getParameter("name") : null);
                product.setQuantity(!DataUtil.isNullOrEmpty(req.getParameter("quantity")) ? Integer.parseInt(req.getParameter("quantity")) : null);
                product.setPrice(!DataUtil.isNullOrEmpty(req.getParameter("price")) ? Long.valueOf(req.getParameter("price")) : null);
                product.setDescription(!DataUtil.isNullOrEmpty(req.getParameter("description")) ? req.getParameter("description") : null);
                int save = productModel.updateProduct(false, product);
                if (save == 1) {
                    req.setAttribute("successMsg", "Cập nhật sản phẩm thành công");
                    resp.sendRedirect("/admin/product");
                } else {
                    req.setAttribute("errorMsg", "Cập nhật sản phẩm không thành công");
                    renderUpdateProductForm(req, resp);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.renderAdmin(req, resp);
        }
    }

    public void cancelProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Integer id = Integer.parseInt(req.getParameter("id"));
            Product product = new Product();
            product.setId(id);
            product.setStatus(Const.STATUS_UNACTIVE);
            product.setUpdatedBy("admin");
            int save = productModel.updateProduct(true, product);
            if (save == 1) {
                resp.sendRedirect("/admin/product");
            }  else {
                req.setAttribute("errorMsg", "Cập nhật sản phẩm không thành công");
                renderAdminFistTab(req, resp);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.renderAdmin(req, resp);
        }
    }


    public void renderSearchCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            categoryService.renderSearchCategory(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp);
        }
    }

    public void renderCreateCategoryForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("renderCategory", true);
            req.setAttribute("renderCreateCategory", true);
            req.getRequestDispatcher("/views/admin/admin.jsp").forward(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp, ex.getMessage());
        }
    }

    public void renderUpdateCategoryForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("renderCategory", true);
            req.setAttribute("renderUpdateCategory", true);
            Integer id = Integer.parseInt(req.getParameter("id"));
            CategoryDto category = categoryService.getDetailCategory(null, id);
            req.setAttribute("category", category);
            req.getRequestDispatcher("/views/admin/admin.jsp").forward(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp, ex.getMessage());
        }
    }

    public void createNewCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");
            Category category = new Category();
            if (DataUtil.isNullOrEmpty(req.getParameter("name"))) {
                req.setAttribute("errorMsg", "Tên loại sản phẩm bắt buộc nhập");
                renderCreateCategoryForm(req, resp);
            } else {
                category.setName(DataUtil.safeToString(req.getParameter("name")).trim());
                category = (Category) categoryModel.save(category);
                if (!DataUtil.isNullObject(category)) {
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

    public void updateCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");
            if (DataUtil.isNullOrEmpty(req.getParameter("name"))) {
                req.setAttribute("errorMsg", "Tên loại sản phẩm bắt buộc nhập");
                renderUpdateCategoryForm(req, resp);
            } else {
                Category category = new Category();
                Integer id = Integer.parseInt(req.getParameter("id"));
                category.setId(id);
                category.setName(!DataUtil.isNullOrEmpty(req.getParameter("name")) ? req.getParameter("name") : null);
                category.setUpdatedBy("admin");
                int save = categoryModel.updateCategory(false, category);
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

    public void cancelCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("renderCategory", true);
        req.setAttribute("renderProduct", false);
        req.setAttribute("renderOrder", false);
        try {
            Integer id = Integer.parseInt(req.getParameter("id"));
            Category category = new Category();
            category.setId(id);
            category.setStatus(Const.STATUS_UNACTIVE);
            category.setUpdatedBy("admin");
            int save = categoryModel.updateCategory(true, category);
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

    public void renderSearchOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("renderOrderAdmin", false);
            orderService.renderSearchOrderAdmin(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp);
        }
    }


    public void renderDetailOrderForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            req.setAttribute("renderOrder", true);
            req.setAttribute("renderOrderAdmin", true);
            orderService.detailOrderForAdmin(req, resp);
            req.getRequestDispatcher("/views/admin/admin.jsp").forward(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp, ex.getMessage());
        }
    }

    public void confirmOrder(HttpServletRequest req, HttpServletResponse resp, String action) throws ServletException, IOException {
        try {
            req.setAttribute("renderOrder", true);
            Integer orderId = Integer.parseInt(req.getParameter("id"));
            Order order = (Order) orderModel.findById(orderId);
            switch (action) {
                case "confirm":
                    order.setStatus(Const.OrderStatus.ACCEPTED);
                    break;
                case "complete":
                    order.setStatus(Const.OrderStatus.COMPLETED);
                    break;
                case "cancel":
                    order.setStatus(Const.OrderStatus.CANCELED);
                    break;
            }
            order.setUpdatedBy("admin");
            int save = orderModel.updateOrder(order, action);
            if (save == 1) {
                req.setAttribute("successMsg", "Cập nhật đơn hàng thành công");
                resp.sendRedirect("/admin/transaction");
            } else {
                req.setAttribute("errorMsg", "Cập nhật đơn hàng thất bại. Vui lòng kiểm tra lại!");
//                resp.sendRedirect("/admin/transaction");
                renderSearchOrder(req, resp);
            }
        } catch (Exception ex) {
            renderErrorPage(req, resp, ex.getMessage());
        }
    }
}
