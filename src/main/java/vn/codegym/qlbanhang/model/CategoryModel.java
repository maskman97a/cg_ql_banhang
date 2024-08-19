package vn.codegym.qlbanhang.model;


import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.CategoryDto;
import vn.codegym.qlbanhang.entity.BaseEntity;
import vn.codegym.qlbanhang.entity.CategoryEntity;
import vn.codegym.qlbanhang.utils.DataUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryModel extends BaseModel {
    private static final CategoryModel inst = new CategoryModel();

    private CategoryModel() {
        super(CategoryEntity.class);
    }

    public static CategoryModel getInstance() {
        return inst;
    }


    public List<CategoryEntity> findCategory() {
        try {
            List<BaseEntity> baseEntities = findAllActive();
            List<CategoryEntity> productList = new ArrayList<>();
            for (BaseEntity baseEntity : baseEntities) {
                productList.add((CategoryEntity) baseEntity);
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
            String sql = this.getSearchCategorySQL(baseSearchDto, id);
            sql += " order by id desc ";
            sql += " limit ? offset ?";
            PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
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
        String sql = " SELECT count(1) from (" + getSearchCategorySQL(baseSearchDto, id) + ") as countLst ";
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
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
        return 0;
    }

    public List<CategoryDto> getLstCategory() throws SQLException {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        String sql = this.getSearchCategorySQL(null, null);
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        int index = 1;
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(rs.getInt("id"));
            categoryDto.setName(rs.getString("name"));
            categoryDtoList.add(categoryDto);
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

    public int updateCategory(Boolean isCancel, CategoryEntity categoryEntity) throws SQLException {
        StringBuilder sb = new StringBuilder("");
        sb.append("UPDATE category " +
                "   SET updated_date = CURRENT_TIMESTAMP , " +
                "       updated_by = ? ");
        if (!DataUtil.isNullObject(categoryEntity)) {
            if (!isCancel) {
                if (!DataUtil.isNullOrEmpty(categoryEntity.getName()))
                    sb.append(" ,name = ? ");
            }
            if (!DataUtil.isNullObject(categoryEntity.getStatus()))
                sb.append(" ,status = ? ");
        }
        sb.append(" WHERE id = ? ");
        if (isCancel)
            sb.append(" AND status = 1 ");
        PreparedStatement preparedStatement = getConnection().prepareStatement(sb.toString());
        int index = 1;
        preparedStatement.setString(index++, categoryEntity.getUpdatedBy());
        if (!DataUtil.isNullObject(categoryEntity)) {
            if (!isCancel) {
                if (!DataUtil.isNullOrEmpty(categoryEntity.getName()))
                    preparedStatement.setString(index++, categoryEntity.getName().trim());
            }
            if (!DataUtil.isNullObject(categoryEntity.getStatus()))
                preparedStatement.setInt(index++, categoryEntity.getStatus());
        }
        preparedStatement.setInt(index++, categoryEntity.getId());
        return preparedStatement.executeUpdate();
    }

    public CategoryDto getDetailCategory(BaseSearchDto baseSearchDto, Integer id) throws SQLException {
        String sql = getSearchCategorySQL(baseSearchDto, id);
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
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
        return null;
    }
}
