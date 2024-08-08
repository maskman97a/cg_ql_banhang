package vn.codegym.qlbanhang.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum ProductSort {
    NAME_ASC("product_name", "ASC", "Sắp xếp theo ABC tăng dần"),
    NAME_DESC("product_name", "DESC", "Sắp xếp theo ABC giảm dần"),
    PRICE_ASC("price", "ASC", "Sắp xếp theo giá tăng dần"),
    PRICE_DESC("price", "DESC", "Sắp xếp theo giá giảm dần");

    final String columnName;
    final String sortType;
    final String description;

    ProductSort(String columnName, String sortType, String description) {
        this.columnName = columnName;
        this.sortType = sortType;
        this.description = description;
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
