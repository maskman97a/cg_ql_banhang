package vn.codegym.qlbanhang.model;


import vn.codegym.qlbanhang.database.DatabaseConnection;
import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.CategoryDto;
import vn.codegym.qlbanhang.dto.ProductDto;
import vn.codegym.qlbanhang.entity.BaseEntity;
import vn.codegym.qlbanhang.entity.Category;
import vn.codegym.qlbanhang.entity.Product;
import vn.codegym.qlbanhang.utils.DataUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryModel extends BaseModel {
    public CategoryModel() {
        super(Category.class);
    }


    public List<Category> findCategory() {
        try {
            List<BaseEntity> baseEntities = findAll();
            List<Category> productList = new ArrayList<>();
            for (BaseEntity baseEntity : baseEntities) {
                productList.add((Category) baseEntity);
            }
            return productList;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }


    public List<CategoryDto> findCategoryByKeyword(BaseSearchDto baseSearchDto, Integer id) throws SQLException {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = this.getSearchCategorySQL(baseSearchDto, id);
            sql += " order by id desc ";
            sql += " limit ? offset ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            int index = 1;
            if (baseSearchDto != null) {
                if (baseSearchDto.getKeyword() != null && !baseSearchDto.getKeyword().isEmpty()) {
                    preparedStatement.setString(index++, "%" + baseSearchDto.getKeyword() + "%");
                }
            }

            if (!DataUtil.isNullObject(id)) {
                preparedStatement.setInt(index++, id);
            }
            preparedStatement.setInt(index++, baseSearchDto.getSize());
            preparedStatement.setInt(index, (baseSearchDto.getPage() - 1) * baseSearchDto.getSize());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setId(rs.getInt("id"));
                categoryDto.setName(rs.getString("name"));
                categoryDtoList.add(categoryDto);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return categoryDtoList;
    }

    public Integer countCategory(BaseSearchDto baseSearchDto, Integer id) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            String sql = " SELECT count(1) from (" + getSearchCategorySQL(baseSearchDto, id) + ") as countLst ";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            int index = 1;
            if (baseSearchDto != null) {
                if (baseSearchDto.getKeyword() != null && !baseSearchDto.getKeyword().isEmpty()) {
                    preparedStatement.setString(index++, "%" + baseSearchDto.getKeyword() + "%");
                }
            }
            if (!DataUtil.isNullObject(id)) {
                preparedStatement.setLong(index++, id);
            }
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return 0;
    }

    public List<CategoryDto> getLstCategory() throws SQLException {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = this.getSearchCategorySQL(null, null);
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            int index = 1;
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setId(rs.getInt("id"));
                categoryDto.setName(rs.getString("name"));
                categoryDtoList.add(categoryDto);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return categoryDtoList;
    }

    public String getSearchCategorySQL(BaseSearchDto baseSearchDto, Integer id) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT id, name ");
        sb.append("  FROM  category  ");
        sb.append(" WHERE status = 1");
        if (!DataUtil.isNullObject(baseSearchDto)) {
            if (baseSearchDto.getKeyword() != null && !baseSearchDto.getKeyword().isEmpty()) {
                sb.append(" AND name LIKE ?   ");
            }
        }
        if (!DataUtil.isNullObject(id)) {
            sb.append(" AND id= ? ");
        }
        return sb.toString();
    }

    public int updateCategory(Boolean isCancel, Category category) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            StringBuilder sb = new StringBuilder("");
            sb.append("UPDATE category " +
                    "   SET updated_date = CURRENT_TIMESTAMP , " +
                    "       updated_by = ? ");
            if (!DataUtil.isNullObject(category)) {
                if (!isCancel) {
                    if (!DataUtil.isNullOrEmpty(category.getName()))
                        sb.append(" ,name = ? ");
                }
                if (!DataUtil.isNullObject(category.getStatus()))
                    sb.append(" ,status = ? ");
            }
            sb.append(" WHERE id = ? ");
            if (isCancel)
                sb.append(" AND status = 1 ");
            PreparedStatement preparedStatement = con.prepareStatement(sb.toString());
            int index = 1;
            preparedStatement.setString(index++, category.getUpdatedBy());
            if (!DataUtil.isNullObject(category)) {
                if (!isCancel) {
                    if (!DataUtil.isNullOrEmpty(category.getName()))
                        preparedStatement.setString(index++, category.getName().trim());
                }
                if (!DataUtil.isNullObject(category.getStatus()))
                    preparedStatement.setInt(index++, category.getStatus());
            }
            preparedStatement.setInt(index++, category.getId());
            return preparedStatement.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return 0;
    }
}
