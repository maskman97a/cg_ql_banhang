package vn.codegym.qlbanhang.service;

import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.Condition;
import vn.codegym.qlbanhang.dto.ProductDto;
import vn.codegym.qlbanhang.entity.*;
import vn.codegym.qlbanhang.model.ProductModel;
import vn.codegym.qlbanhang.utils.SftpUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminService extends BaseService {
    public ProductModel productModel;

    public AdminService() {
        super(null);
        this.productModel = new ProductModel();
    }


    public void renderAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("firstSearchTab", true);
            this.searchProductAdmin(req, resp);
            resp.sendRedirect(req.getContextPath() + "/admin");
            req.getRequestDispatcher("/views/admin/admin.jsp").forward(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp);
        }
    }

    public void searchProductAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        try {
            String keyword = req.getParameter("keyword");
            int size = 10;
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
            req.setAttribute("firstSearchTab", false);
        } catch (Exception ex) {
            renderErrorPage(req, resp, ex.getMessage());
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
            req.getRequestDispatcher("/views/admin/product/product-create.jsp").forward(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp, ex.getMessage());
        }
    }

    public void createNewProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            BaseEntity baseEntity = new Product();
            Map<String, Object> mapValue = new HashMap<>();
            req.getAttribute("image");
            String imageUrl = SftpUtils.getPathSFTP(req, resp);
            mapValue.put("imageUrl", imageUrl);
            mapValue.put("productCode", req.getAttribute("code"));
            mapValue.put("productName", req.getAttribute("name"));
            mapValue.put("quantity", req.getAttribute("quantity"));
            mapValue.put("price", req.getAttribute("price"));
            mapValue.put("note", req.getAttribute("note"));
            mapValue.put("status", "1");
            mapValue.put("createdBy", "ADMIN");
            mapValue.put("updatedBy", "ADMIN");
            baseEntity.setMapValue(mapValue);
            int save = super.save(baseEntity);
            if (save == 1) {
                this.searchProductAdmin(req, resp);
                this.renderAdmin(req, resp);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.renderAdmin(req, resp);
        }
    }

    public void updateProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            BaseEntity baseEntity = new Product();
            this.commonUpdate(req, resp, baseEntity, 1L);
        } catch (Exception ex) {
            ex.printStackTrace();
            this.renderAdmin(req, resp);
        }
    }

    public void cancleProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            BaseEntity baseEntity = new Product();
            this.commonUpdate(req, resp, baseEntity, 0L);
        } catch (Exception ex) {
            ex.printStackTrace();
            this.renderAdmin(req, resp);
        }
    }

    private void commonUpdate(HttpServletRequest req, HttpServletResponse resp, BaseEntity baseEntity, Long status) throws ServletException, IOException, SQLException {
        Map<String, Object> mapValue = new HashMap<>();
//            req.getAttribute("image");
//            String imageUrl = SftpUtils.getPathSFTP(req, resp);
//            mapValue.put("imageUrl", imageUrl);
        mapValue.put("id", req.getParameter("id"));
        mapValue.put("productCode", req.getAttribute("code"));
        mapValue.put("productName", req.getAttribute("name"));
        mapValue.put("quantity", req.getAttribute("quantity"));
        mapValue.put("price", req.getAttribute("price"));
        mapValue.put("note", req.getAttribute("note"));
        mapValue.put("status", status);
        mapValue.put("updatedBy", "ADMIN");
        mapValue.put("updatedDate", LocalDateTime.now());
        baseEntity.setMapValue(mapValue);
        int save = super.save(baseEntity);
        if (save == 1) {
            this.searchProductAdmin(req, resp);
            this.renderAdmin(req, resp);
        }
    }
}
