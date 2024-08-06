package vn.codegym.qlbanhang.service;

import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.Condition;
import vn.codegym.qlbanhang.dto.ProductDto;
import vn.codegym.qlbanhang.entity.*;
import vn.codegym.qlbanhang.model.ProductModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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


    public void renderAdmin(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setAttribute("firstSearchTab", true);
            this.searchProductAdmin(req, resp);
            req.getRequestDispatcher("/views/admin/admin.jsp").forward(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp);
        }
    }

    public void searchProductAdmin(HttpServletRequest req, HttpServletResponse resp) {
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

    public void renderCreateProductForm(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.getRequestDispatcher("/views/admin/product/product-create.jsp").forward(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp, ex.getMessage());
        }
    }

    public void createNewProduct(HttpServletRequest req, HttpServletResponse resp) {
        try {
            BaseEntity baseEntity = new Product();
            Map<String, Object> mapValue = new HashMap<>();
            mapValue.put("productCode", req.getAttribute("code"));
            mapValue.put("productName", req.getAttribute("name"));
            mapValue.put("note", req.getAttribute("note"));
            mapValue.put("price", req.getAttribute("price"));
            mapValue.put("quantity", req.getAttribute("quantity"));
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
        }
    }

//    public BaseData searchProduct(HttpServletRequest req, HttpServletResponse resp) throws Exception {
//        try {
//            String keyword = req.getParameter("keyword");
//            int size = 10;
//            if (req.getParameter("size") != null) {
//                size = Integer.parseInt(req.getParameter("size"));
//            }
//            int page = 1;
//            if (req.getParameter("page") != null) {
//                page = Integer.parseInt(req.getParameter("page"));
//            }
//            req.setAttribute("currentPage", page);
//            baseSearchDto.setKeyword(keyword);
//            baseSearchDto.setSize(size);
//            baseSearchDto.setPage(page);
//
//            List<BaseEntity> lstData = baseModel.search(baseSearchDto);
//            int count = baseModel.count(baseSearchDto);
//            getPaging(req, resp, count, size, page);
//            req.setAttribute("keyword", keyword);
//            return new BaseData(count, lstData);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }


}
