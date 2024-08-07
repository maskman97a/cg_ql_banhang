package vn.codegym.qlbanhang.model;

import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.Condition;
import vn.codegym.qlbanhang.dto.ProductDto;
import vn.codegym.qlbanhang.entity.BaseEntity;
import vn.codegym.qlbanhang.entity.Product;
import vn.codegym.qlbanhang.utils.DataUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductModel extends BaseModel {
    public ProductModel() {
        super(Product.getTableName());
    }

    public Product findProductById(int id) throws SQLException {
        return (Product) findById(id);
    }


    public List<Product> findProduct() {
        List<BaseEntity> baseEntities = findAll();
        List<Product> productList = new ArrayList<>();
        for (BaseEntity baseEntity : baseEntities) {
            productList.add((Product) baseEntity);
        }
        return productList;
    }

    public List<Product> findProductByKeywordAndCategoryId(String keyword, Integer categoryId, Integer page, Integer size) throws SQLException {
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
        baseSearchDto.setSize(size);
        baseSearchDto.setPage(page);
        List<Product> productList = new ArrayList<>();
        List<BaseEntity> baseEntities = super.search(baseSearchDto);
        if (!DataUtil.isNullOrEmpty(baseEntities)) {
            for (BaseEntity baseEntity : baseEntities) {
                productList.add((Product) baseEntity);
            }
        }
        return productList;
    }

    public List<ProductDto> findProductByKeyword(BaseSearchDto baseSearchDto) throws SQLException {
        List<ProductDto> productDtoList = new ArrayList<>();
        String sql = this.getSearchSQL(baseSearchDto);
        sql += " order by id desc ";
        sql += " limit ? offset ?";
        PreparedStatement preparedStatement = this.con.prepareStatement(sql);
        int index = 1;
        if (baseSearchDto != null) {
            if (baseSearchDto.getKeyword() != null && !baseSearchDto.getKeyword().isEmpty()) {
                preparedStatement.setString(index++, "%" + baseSearchDto.getKeyword() + "%");
                preparedStatement.setString(index++, "%" + baseSearchDto.getKeyword() + "%");
            }
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
            productDtoList.add(productDto);
        }
        return productDtoList;
    }


    public Integer countProduct(BaseSearchDto baseSearchDto) throws SQLException {
        String sql = " SELECT count(1) from (" + getSearchSQL(baseSearchDto) + ") as countLst ";
        PreparedStatement preparedStatement = this.con.prepareStatement(sql);
        int index = 1;
        if (baseSearchDto != null) {
            if (baseSearchDto.getKeyword() != null && !baseSearchDto.getKeyword().isEmpty()) {
                preparedStatement.setString(index++, "%" + baseSearchDto.getKeyword() + "%");
                preparedStatement.setString(index++, "%" + baseSearchDto.getKeyword() + "%");
            }
        }
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public String getSearchSQL(BaseSearchDto baseSearchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT id,image_url as imageUrl,product_code as productCode,product_name as productName,price,quantity,description ");
        sb.append("  FROM product ");
        sb.append(" WHERE status = 1 ");
        if (baseSearchDto != null) {
            if (baseSearchDto.getKeyword() != null && !baseSearchDto.getKeyword().isEmpty()) {
                sb.append(" AND ( product_code LIKE ? OR product_name LIKE ? ) ");
            }
        }
        return sb.toString();
    }

    public int updateProduct(Boolean isCancel, Product product) throws SQLException {
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
                if (!DataUtil.isNullOrZero(product.getDescription()))
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
                if (!DataUtil.isNullOrZero(product.getDescription()))
                    preparedStatement.setString(index++, product.getDescription().trim());
            }
            if (!DataUtil.isNullObject(product.getStatus()))
                preparedStatement.setInt(index++, product.getStatus());
        }
        preparedStatement.setInt(index++, product.getId());
        return preparedStatement.executeUpdate();
    }
}
