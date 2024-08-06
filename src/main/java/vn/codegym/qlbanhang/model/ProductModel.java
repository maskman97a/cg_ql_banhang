package vn.codegym.qlbanhang.model;

import vn.codegym.qlbanhang.database.DatabaseConnection;
import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.Condition;
import vn.codegym.qlbanhang.dto.JoinCondition;
import vn.codegym.qlbanhang.dto.ProductDto;
import vn.codegym.qlbanhang.entity.BaseEntity;
import vn.codegym.qlbanhang.entity.Note;
import vn.codegym.qlbanhang.entity.Product;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductModel extends BaseModel {

    private final Connection con;

    public ProductModel() {
        super(Product.getTableName());
        this.con = DatabaseConnection.getConnection();
    }


    public List<ProductDto> findProduct() {
        List<ProductDto> productDtoList = new ArrayList<>();
//        productDtoList.add(new ProductDto(1, "Product 1", 30000, "../../images/logo.png"));
//        productDtoList.add(new ProductDto(2, "Product 2", 30000, "../../images/logo.png"));
//        productDtoList.add(new ProductDto(3, "Product 3", 2000, "../../images/logo.png"));
//        productDtoList.add(new ProductDto(4, "Product 4", 2000, "../../images/logo.png"));
        return productDtoList;
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
            productDto.setPrice(rs.getInt("price"));
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
