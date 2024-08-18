package vn.codegym.qlbanhang.model;


import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.Condition;
import vn.codegym.qlbanhang.entity.Stock;
import vn.codegym.qlbanhang.enums.StockTransType;
import vn.codegym.qlbanhang.utils.DataUtil;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;

public class StockModel extends BaseModel {
    private static final StockModel inst = new StockModel();

    private StockModel() {
        super(Stock.class);
    }

    public static StockModel getInstance() {
        return inst;
    }

    public Stock updateStock(Stock stock, int quantity, String type) throws SQLException {
        if (type.equals(StockTransType.EXPORT.name())) {
            stock.setAvailableQuantity(stock.getAvailableQuantity() - quantity);
            stock.setTotalQuantity(stock.getTotalQuantity() - quantity);
            stock.setPendingQuantity(stock.getPendingQuantity() - quantity);
        } else if (type.equals(StockTransType.IMPORT.name())) {
            stock.setAvailableQuantity(stock.getAvailableQuantity() + quantity);
            stock.setTotalQuantity(stock.getTotalQuantity() + quantity);
        } else {
            stock.setAvailableQuantity(stock.getAvailableQuantity() - quantity);
            stock.setPendingQuantity(stock.getPendingQuantity() + quantity);
        }
        stock.setUpdatedBy("SYSTEM");
        stock.setUpdatedDate(LocalDateTime.now());
        return (Stock) save(stock);
    }

    public Stock getValidStock(int productId, int quantity) throws SQLException {
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        Condition productCondition = Condition.newAndCondition("product_id", "=", productId);
        Condition quantityCon = Condition.newAndCondition("available_quantity", ">=", quantity);
        baseSearchDto.setConditions(Arrays.asList(productCondition, quantityCon));
        Stock stock = (Stock) findOne(baseSearchDto);
        if (!DataUtil.isNullObject(stock)) {
            return stock;
        }
        return null;
    }
}
