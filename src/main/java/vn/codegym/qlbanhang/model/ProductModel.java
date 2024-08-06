package vn.codegym.qlbanhang.model;

import vn.codegym.qlbanhang.entity.BaseEntity;
import vn.codegym.qlbanhang.entity.Product;

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
}
