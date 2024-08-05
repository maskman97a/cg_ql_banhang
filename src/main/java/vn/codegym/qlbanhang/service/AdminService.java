package vn.codegym.qlbanhang.service;

import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.entity.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminService extends BaseService {
    public AdminService() {
        super(null);
    }


    public void renderAdmin(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setAttribute("firstSearchTab", true);
            req.getRequestDispatcher("/views/admin/admin.jsp").forward(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp);
        }
    }

    public void searchProduct(HttpServletRequest req, HttpServletResponse resp) {

        BaseSearchDto baseSearchDto = new BaseSearchDto();
        BaseData baseData = super.doSearch(req, resp, baseSearchDto, null);
        if (baseData != null && baseData.getLstData() != null && !baseData.getLstData().isEmpty()) {
            List<Product> lstData = new ArrayList<>();
            int index = 1;
            for (BaseEntity baseEntity : baseData.getLstData()) {
                Product product = (Product) baseEntity;
                product.setIndex(index++);
                lstData.add(product);
            }
            req.setAttribute("lstData", lstData);
        }
        try {
            req.setAttribute("firstSearchTab", false);
            req.getRequestDispatcher("/views/admin/product/product-list.jsp").forward(req, resp);
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
                this.searchProduct(req, resp);
                this.renderAdmin(req, resp);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
