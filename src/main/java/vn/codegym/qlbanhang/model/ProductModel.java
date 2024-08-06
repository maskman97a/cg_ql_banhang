package vn.codegym.qlbanhang.model;

import vn.codegym.qlbanhang.database.DatabaseConnection;
import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.Condition;
import vn.codegym.qlbanhang.dto.JoinCondition;
import vn.codegym.qlbanhang.dto.ProductDto;
import vn.codegym.qlbanhang.entity.BaseEntity;
import vn.codegym.qlbanhang.entity.Note;
import vn.codegym.qlbanhang.entity.Product;
import vn.codegym.qlbanhang.entity.BaseEntity;
import vn.codegym.qlbanhang.entity.Product;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductModel extends BaseModel {
    public ProductModel() {
        super(Product.getTableName());
    }


    public List<Product> findProduct() {
        List<BaseEntity> baseEntities = findAll();
        List<Product> productList = new ArrayList<>();
        for (BaseEntity baseEntity : baseEntities) {
            productList.add((Product) baseEntity);
        }
        return productList;
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
