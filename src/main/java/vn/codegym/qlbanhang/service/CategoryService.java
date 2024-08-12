package vn.codegym.qlbanhang.service;

import vn.codegym.qlbanhang.constants.Const;
import vn.codegym.qlbanhang.database.DatabaseConnection;
import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.CategoryDto;
import vn.codegym.qlbanhang.dto.ProductDto;
import vn.codegym.qlbanhang.entity.BaseEntity;
import vn.codegym.qlbanhang.entity.Category;
import vn.codegym.qlbanhang.entity.Product;
import vn.codegym.qlbanhang.model.CategoryModel;
import vn.codegym.qlbanhang.model.ProductModel;
import vn.codegym.qlbanhang.utils.DataUtil;
import vn.codegym.qlbanhang.utils.SftpUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@MultipartConfig
public class CategoryService extends BaseService {
    public ProductModel productModel;
    private final CategoryModel categoryModel;

    public CategoryService() {
        super(null);
        this.productModel = new ProductModel();
        this.categoryModel = new CategoryModel();
    }


    public void renderSearch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<BaseEntity> categoryDtoList = categoryModel.findAll();
            req.setAttribute("lstCategory", categoryDtoList);
            req.getRequestDispatcher("/views/admin/admin.jsp").forward(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp);
        }
    }

    public void createNewCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");
            Category category = new Category();

            category.setName(req.getParameter("name"));
            int save = categoryModel.save(category);
            if (save == 1) {
                this.searchCategory(req, resp);
//                resp.sendRedirect("/admin");
                req.getRequestDispatcher("/views/admin/category/transaction-list.jsp").forward(req, resp);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void renderSearchCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("renderCategory", true);
            req.setAttribute("renderProduct", false);
            req.setAttribute("renderOrder", false);
            this.searchCategory(req, resp);
            req.getRequestDispatcher(req.getContextPath() + "/views/admin/admin.jsp").forward(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp);
        }
    }


    public void searchCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        try {
            String keyword = DataUtil.safeToString(req.getParameter("keyword"));
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
            List<CategoryDto> lstData = categoryModel.findCategoryByKeyword(baseSearchDto, null);
            if (lstData != null && !lstData.isEmpty()) {
                int index = 1;
                for (CategoryDto productDto : lstData) {
                    productDto.setIndex(index++);
                }
                req.setAttribute("lstCategory", lstData);
            }
            int count = categoryModel.countCategory(baseSearchDto, null);
            getPaging(req, resp, count, size, page);
        } catch (Exception ex) {
            renderErrorPage(req, resp, ex.getMessage());
        }
    }

    public CategoryDto getDetailCategory(BaseSearchDto baseSearchDto, Integer id) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            String sql = categoryModel.getSearchCategorySQL(baseSearchDto, id);
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            int index = 1;
            if (!DataUtil.isNullObject(id)) {
                preparedStatement.setInt(index++, id);
            }
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setId(rs.getInt("id"));
                categoryDto.setName(rs.getString("name"));

                return categoryDto;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return null;
    }
}
