package vn.codegym.qlbanhang.model;

import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.ProductDto;
import vn.codegym.qlbanhang.entity.BaseEntity;
import vn.codegym.qlbanhang.entity.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductModel extends BaseModel {
    public ProductModel() {
        super(Product.getTableName());
    }

    public Product findProductById(int id) {
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
            productDto.setNote(rs.getString("note"));
            productDtoList.add(productDto);
        }
        return productDtoList;
    }


    public Integer count(BaseSearchDto baseSearchDto) throws SQLException {
        PreparedStatement preparedStatement = this.con.prepareStatement(getSearchSQL(baseSearchDto));
        int index = 1;
        if (baseSearchDto != null) {
            if (baseSearchDto.getKeyword() != null && !baseSearchDto.getKeyword().isEmpty()) {
                preparedStatement.setString(index++, baseSearchDto.getKeyword());
                preparedStatement.setString(index++, baseSearchDto.getKeyword());
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
        sb.append(" SELECT id,image_url as imageUrl,product_code as productCode,product_name as productName,price,quantity,note ");
        sb.append("  FROM product ");
        sb.append(" WHERE status = 1 ");
        if (baseSearchDto != null) {
            if (baseSearchDto.getKeyword() != null && !baseSearchDto.getKeyword().isEmpty()) {
                sb.append(" AND ( product_code LIKE ? OR product_name LIKE ? ) ");
            }
        }
        return sb.toString();
    }
}
