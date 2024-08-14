package vn.codegym.qlbanhang.model;

import vn.codegym.qlbanhang.constants.Const;
import vn.codegym.qlbanhang.database.DatabaseConnection;
import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.Condition;
import vn.codegym.qlbanhang.dto.OrderByCondition;
import vn.codegym.qlbanhang.dto.ProductDto;
import vn.codegym.qlbanhang.entity.BaseData;
import vn.codegym.qlbanhang.entity.BaseEntity;
import vn.codegym.qlbanhang.entity.Product;
import vn.codegym.qlbanhang.utils.DataUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductModel extends BaseModel {
    public ProductModel() {
        super(Product.class);
    }

    public Product findProductById(int id) throws SQLException {
        return (Product) findById(id);
    }


    public List<Product> findProduct() {
        try {
            List<BaseEntity> baseEntities = findAll();
            List<Product> productList = new ArrayList<>();
            for (BaseEntity baseEntity : baseEntities) {
                productList.add((Product) baseEntity);
            }
            return productList;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    public BaseData findProductByKeywordAndCategoryId(String keyword, Integer categoryId, String sortCol, String sortType, Integer page, Integer size) throws SQLException {
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        if (!DataUtil.isNullOrEmpty(keyword)) {
            Condition condition = new Condition();
            condition.setColumnName("product_name");
            condition.setOperator("LIKE");
            condition.setValue("%" + keyword + "%");
            baseSearchDto.getConditions().add(condition);
        }

        if (!DataUtil.isNullOrZero(categoryId)) {
            Condition condition = new Condition();
            condition.setColumnName("category_id");
            condition.setOperator("=");
            condition.setValue(categoryId);
            baseSearchDto.getConditions().add(condition);
        }
        Condition condition = new Condition();
        condition.setColumnName("status");
        condition.setOperator("=");
        condition.setValue(Const.STATUS_ACTIVE);
        baseSearchDto.getConditions().add(condition);

        baseSearchDto.setSize(size);
        baseSearchDto.setPage(page);
        if (DataUtil.isNullOrEmpty(sortCol)) {
            OrderByCondition orderByCondition = new OrderByCondition();
            orderByCondition.setColumnName("category_id");
            orderByCondition.setOrderType("ASC");
            baseSearchDto.getOrderByConditions().add(orderByCondition);

            OrderByCondition orderByCondition2 = new OrderByCondition();
            orderByCondition2.setColumnName("product_name");
            orderByCondition2.setOrderType("ASC");
            baseSearchDto.getOrderByConditions().add(orderByCondition2);
        } else {
            OrderByCondition orderByCondition = new OrderByCondition();
            orderByCondition.setColumnName(sortCol);
            if (sortType != null) {
                orderByCondition.setOrderType(sortType);
            }
            baseSearchDto.getOrderByConditions().add(orderByCondition);
        }

        List<Product> productList = new ArrayList<>();
        List<BaseEntity> baseEntities = super.search(baseSearchDto);
        int count = super.count(baseSearchDto);
        return new BaseData(count, baseEntities);
    }

    public List<ProductDto> findProductByKeyword(BaseSearchDto baseSearchDto, Long categoryId, Integer id) throws SQLException {
        List<ProductDto> productDtoList = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = this.getSearchSQL(baseSearchDto, categoryId, id);
            sql += " order by id desc ";
            sql += " limit ? offset ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            int index = 1;
            if (baseSearchDto != null) {
                if (baseSearchDto.getKeyword() != null && !baseSearchDto.getKeyword().isEmpty()) {
                    preparedStatement.setString(index++, "%" + baseSearchDto.getKeyword() + "%");
                    preparedStatement.setString(index++, "%" + baseSearchDto.getKeyword() + "%");
                }
            }
            if (!DataUtil.isNullOrZero(categoryId)) {
                preparedStatement.setLong(index++, categoryId);
            }
            if (!DataUtil.isNullObject(id)) {
                preparedStatement.setInt(index++, id);
            }
            preparedStatement.setInt(index++, baseSearchDto.getSize());
            preparedStatement.setInt(index, (baseSearchDto.getPage() - 1) * baseSearchDto.getSize());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ProductDto productDto = new ProductDto();
                productDto.setId(rs.getInt("id"));
                productDto.setImageUrl(rs.getString("imageUrl"));
                productDto.setProductCode(rs.getString("productCode"));
                productDto.setProductName(rs.getString("productName"));
                productDto.setPrice(rs.getLong("price"));
                productDto.setQuantity(rs.getInt("quantity"));
                productDto.setDescription(rs.getString("description"));
                productDto.setCategoryName(rs.getString("categoryName"));
                productDto.setCategoryId(rs.getInt("categoryId"));
                productDtoList.add(productDto);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return productDtoList;
    }

    public Integer countProduct(BaseSearchDto baseSearchDto, Long categoryId, Integer id) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            String sql = " SELECT count(1) from (" + getSearchSQL(baseSearchDto, categoryId, id) + ") as countLst ";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            int index = 1;
            if (baseSearchDto != null) {
                if (baseSearchDto.getKeyword() != null && !baseSearchDto.getKeyword().isEmpty()) {
                    preparedStatement.setString(index++, "%" + baseSearchDto.getKeyword() + "%");
                    preparedStatement.setString(index++, "%" + baseSearchDto.getKeyword() + "%");
                }
            }
            if (!DataUtil.isNullOrZero(categoryId)) {
                preparedStatement.setLong(index++, categoryId);
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

    public ProductDto getDetailProduct(BaseSearchDto baseSearchDto, Long categoryId, Integer id) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            String sql = this.getSearchSQL(baseSearchDto, categoryId, id);
            if (DataUtil.isNullObject(id)) {
                sql += " order by id desc ";
                sql += " limit ? offset ?";
            }
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            int index = 1;
            if (baseSearchDto != null) {
                if (baseSearchDto.getKeyword() != null && !baseSearchDto.getKeyword().isEmpty()) {
                    preparedStatement.setString(index++, "%" + baseSearchDto.getKeyword() + "%");
                    preparedStatement.setString(index++, "%" + baseSearchDto.getKeyword() + "%");
                }
            }
            if (!DataUtil.isNullObject(categoryId)) {
                preparedStatement.setLong(index++, categoryId);
            }
            if (!DataUtil.isNullObject(id)) {
                preparedStatement.setInt(index++, id);
            }
            if (DataUtil.isNullObject(id)) {
                preparedStatement.setInt(index++, baseSearchDto.getSize());
                preparedStatement.setInt(index, (baseSearchDto.getPage() - 1) * baseSearchDto.getSize());
            }
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ProductDto productDto = new ProductDto();
                productDto.setId(rs.getInt("id"));
                productDto.setImageUrl(rs.getString("imageUrl"));
                productDto.setProductCode(rs.getString("productCode"));
                productDto.setProductName(rs.getString("productName"));
                productDto.setPrice(rs.getLong("price"));
                productDto.setQuantity(rs.getInt("quantity"));
                productDto.setDescription(rs.getString("description"));
                productDto.setCategoryName(rs.getString("categoryName"));
                productDto.setCategoryId(rs.getInt("categoryId"));
                return productDto;
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


    public String getSearchSQL(BaseSearchDto baseSearchDto, Long categoryId, Integer id) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT p.id,p.image_url as imageUrl,p.product_code as productCode,p.product_name as productName,p.price,p.quantity,p.description,c.name as categoryName,p.category_id as categoryId ");
        sb.append("  FROM product p ");
        sb.append("       left join category c on c.status = 1 and p.category_id = c.id ");
        sb.append(" WHERE p.status = 1 ");
        if (!DataUtil.isNullObject(baseSearchDto)) {
            if (baseSearchDto.getKeyword() != null && !baseSearchDto.getKeyword().isEmpty()) {
                sb.append(" AND ( p.product_code LIKE ? OR p.product_name LIKE ? ) ");
            }
        }
        if (!DataUtil.isNullOrZero(categoryId)) {
            sb.append(" AND p.category_id = ? ");
        }
        if (!DataUtil.isNullObject(id)) {
            sb.append(" AND p.id= ? ");
        }
        return sb.toString();
    }

    public int updateProduct(Boolean isCancel, Product product) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            StringBuilder sb = new StringBuilder("");
            sb.append("UPDATE product " +
                    "   SET updated_date = CURRENT_TIMESTAMP , " +
                    "       updated_by = ? ");
            if (!DataUtil.isNullObject(product)) {
                if (!isCancel) {
                    if (!DataUtil.isNullOrEmpty(product.getImageUrl()))
                        sb.append(" ,image_url = ? ");
                    if (!DataUtil.isNullOrEmpty(product.getProductName()))
                        sb.append(" ,product_name = ? ");
                    if (!DataUtil.isNullOrZero(product.getPrice()))
                        sb.append(" ,price = ? ");
                    if (!DataUtil.isNullOrZero(product.getQuantity()))
                        sb.append(" ,quantity = ? ");
                    if (!DataUtil.isNullOrEmpty(product.getDescription()))
                        sb.append(" ,description = ? ");
                }
                if (!DataUtil.isNullObject(product.getStatus()))
                    sb.append(" ,status = ? ");
            }
            sb.append(" WHERE id = ? ");
            if (isCancel)
                sb.append(" AND status = 1 ");
            PreparedStatement preparedStatement = con.prepareStatement(sb.toString());
            int index = 1;
            preparedStatement.setString(index++, product.getUpdatedBy());
            if (!DataUtil.isNullObject(product)) {
                if (!isCancel) {
                    if (!DataUtil.isNullOrEmpty(product.getImageUrl()))
                        preparedStatement.setString(index++, product.getImageUrl().trim());
                    if (!DataUtil.isNullOrEmpty(product.getProductName()))
                        preparedStatement.setString(index++, product.getProductName().trim());
                    if (!DataUtil.isNullOrZero(product.getPrice()))
                        preparedStatement.setLong(index++, product.getPrice());
                    if (!DataUtil.isNullOrZero(product.getQuantity()))
                        preparedStatement.setInt(index++, product.getQuantity());
                    if (!DataUtil.isNullOrEmpty(product.getDescription()))
                        preparedStatement.setString(index++, product.getDescription().trim());
                }
                if (!DataUtil.isNullObject(product.getStatus()))
                    preparedStatement.setInt(index++, product.getStatus());
            }
            preparedStatement.setInt(index++, product.getId());
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
