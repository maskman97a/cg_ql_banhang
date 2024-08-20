package vn.codegym.qlbanhang.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum ProductSort {
    NAME_ASC("product_name", "ASC", "Sắp xếp theo A-Z", "fa-arrow-up-a-z"),
    NAME_DESC("product_name", "DESC", "Sắp xếp theo Z-A", "fa-arrow-down-z-a"),
    PRICE_ASC("price", "ASC", "Giá tăng dần", "fa-arrow-up-wide-short"),
    PRICE_DESC("price", "DESC", "Giá giảm dần", "fa-arrow-down-wide-short");

    final String columnName;
    final String sortType;
    final String description;
    final String fontAwesome;

    ProductSort(String columnName, String sortType, String description, String fontAwesome) {
        this.columnName = columnName;
        this.sortType = sortType;
        this.description = description;
        this.fontAwesome = fontAwesome;
    }

    public static List<ProductSort> getAllSort() {
        return new ArrayList<>(Arrays.asList(ProductSort.values()));
    }

    public String getName() {
        return this.name();
    }

    public static  ProductSort getProductSort(String columnName, String sortType) {
        for (ProductSort p : getAllSort()) {
            if (p.columnName.equals(columnName) && p.sortType.equals(sortType)) {
                return p;
            }
        }
        return null;
    }
}
