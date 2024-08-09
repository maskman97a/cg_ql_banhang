package vn.codegym.qlbanhang.service;

import lombok.var;
import vn.codegym.qlbanhang.constants.Const;
import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.CategoryDto;
import vn.codegym.qlbanhang.dto.Condition;
import vn.codegym.qlbanhang.dto.ProductDto;
import vn.codegym.qlbanhang.entity.*;
import vn.codegym.qlbanhang.model.CategoryModel;
import vn.codegym.qlbanhang.model.ProductModel;
import vn.codegym.qlbanhang.utils.ClassUtils;
import vn.codegym.qlbanhang.utils.DataUtil;
import vn.codegym.qlbanhang.utils.SftpUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MultipartConfig
public class AdminService extends BaseService {
    public ProductModel productModel;
    private final CategoryModel categoryModel;
    private final CategoryService categoryService;

    public AdminService() {
        super(null);
        this.productModel = new ProductModel();
        this.categoryModel = new CategoryModel();
        this.categoryService = new CategoryService();
    }


    public void renderAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("firstSearchTab", false);
            this.searchProductAdmin(req, resp);
            req.getRequestDispatcher("/views/admin/admin.jsp").forward(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp);
        }
    }

    public void renderAdminFistTab(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("firstSearchTab", true);
            this.searchProductAdmin(req, resp);
            req.getRequestDispatcher("/views/admin/admin.jsp").forward(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp);
        }
    }

    public void renderCreateProductForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("updateProduct", false);
            List<CategoryDto> categoryDtoList = new ArrayList<>();
            List<BaseEntity> baseEntities = categoryModel.findAll();
            for (BaseEntity baseEntity : baseEntities) {
                categoryDtoList.add(modelMapper.map(baseEntity, CategoryDto.class));
            }
            req.setAttribute("lstCategory", categoryDtoList);
            req.getRequestDispatcher("/views/admin/product/category-create.jsp").forward(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp, ex.getMessage());
        }
    }

    public void renderUpdateProductForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("updateProduct", true);
            Integer id = Integer.parseInt(req.getParameter("id"));
            ProductDto productDto = productModel.getDetailProduct(null, null, id);
            req.setAttribute("product", productDto);
            req.getRequestDispatcher("/views/admin/product/category-update.jsp").forward(req, resp);
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
                for (ProductDto productDto : lstData) {
                    productDto.setIndex(index++);
                }
                req.setAttribute("lstData", lstData);
            }
            List<CategoryDto> categoryDtoList = new ArrayList<>();
            List<BaseEntity> baseEntities = categoryModel.findAll();
            for (BaseEntity baseEntity : baseEntities) {
                categoryDtoList.add(modelMapper.map(baseEntity, CategoryDto.class));
            }
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
            Product product = new Product();
            String imageUrl = SftpUtils.getPathSFTP(req, resp);
            product.setImageUrl(imageUrl);
            product.setProductCode(req.getParameter("code"));
            product.setProductName(req.getParameter("name"));
            product.setCategoryId(Integer.parseInt(req.getParameter("categoryId")));
            product.setQuantity(Integer.parseInt(req.getParameter("quantity")));
            product.setPrice(DataUtil.safeToLong(req.getParameter("price")));
            product.setDescription(req.getParameter("description"));
            int save = productModel.save(product);
            if (save == 1) {
                this.searchProductAdmin(req, resp);
                resp.sendRedirect("/admin");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.renderAdmin(req, resp);
        }
    }

    public void updateProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");
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
                this.searchProductAdmin(req, resp);
                resp.sendRedirect("/admin");
            } else {
                renderErrorPage(req, resp, "Cập nhật sản phẩm không thành công");
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
                this.searchProductAdmin(req, resp);
                resp.sendRedirect("/admin");
            } else {
                renderErrorPage(req, resp, "Xóa sản phẩm thất bại. Vui lòng kiểm tra lại!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.renderAdmin(req, resp);
        }
    }


    public void renderSearchCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            categoryService.renderSearchCategory(req, resp);
            req.getRequestDispatcher("/views/admin/category/category-list.jsp").forward(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp);
        }
    }

    public void renderUpdateCategoryForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("updateCreate", true);
            Integer id = Integer.parseInt(req.getParameter("id"));
            ProductDto productDto = productModel.getDetailProduct(null, null, id);
            req.setAttribute("product", productDto);
            req.getRequestDispatcher("/views/admin/product/category-update.jsp").forward(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp, ex.getMessage());
        }
    }
}
