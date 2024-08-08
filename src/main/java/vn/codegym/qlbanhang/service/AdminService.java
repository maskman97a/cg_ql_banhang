package vn.codegym.qlbanhang.service;

import lombok.var;
import vn.codegym.qlbanhang.constants.Const;
import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.Condition;
import vn.codegym.qlbanhang.dto.ProductDto;
import vn.codegym.qlbanhang.entity.*;
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

    public AdminService() {
        super(null);
        this.productModel = new ProductModel();
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
            req.getRequestDispatcher("/views/admin/product/product-create.jsp").forward(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp, ex.getMessage());
        }
    }

    public void renderUpdateProductForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("updateProduct", true);
            Integer id = Integer.parseInt(req.getParameter("id"));
            Product product = (Product) productModel.findById(id);
            ProductDto productDto = modelMapper.map(product, ProductDto.class);
            req.setAttribute("product", productDto);
            req.getRequestDispatcher("/views/admin/product/product-create.jsp").forward(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp, ex.getMessage());
        }
    }

    public void renderDeleteProductForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("updateProduct", true);
            Integer id = Integer.parseInt(req.getParameter("id"));
            Product product = (Product) productModel.findById(id);
            ProductDto productDto = modelMapper.map(product, ProductDto.class);
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
            List<ProductDto> lstData = productModel.findProductByKeyword(baseSearchDto);
            if (lstData != null && !lstData.isEmpty()) {
                int index = 1;
                for (ProductDto productDto : lstData) {
                    productDto.setIndex(index++);
                }
                req.setAttribute("lstData", lstData);
            }
            int count = productModel.countProduct(baseSearchDto);
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
            Product product = new Product();
            Integer id = Integer.parseInt(req.getParameter("id"));
            product.setId(id);
            String imageUrl = SftpUtils.getPathSFTP(req, resp);
            product.setImageUrl(imageUrl);
            product.setProductCode(req.getParameter("code"));
            product.setProductName(req.getParameter("name"));
            product.setQuantity(Integer.parseInt(req.getParameter("quantity")));
            product.setPrice(DataUtil.safeToLong(req.getParameter("price")));
            product.setDescription(req.getParameter("description"));
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
}
