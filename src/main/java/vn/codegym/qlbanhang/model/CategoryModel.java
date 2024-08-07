package vn.codegym.qlbanhang.model;


import vn.codegym.qlbanhang.entity.Category;

public class CategoryModel extends BaseModel {
    public CategoryModel() {
        super(Category.getTableName());
    }
}
