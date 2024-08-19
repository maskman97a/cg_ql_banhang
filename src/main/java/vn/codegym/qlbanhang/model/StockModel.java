package vn.codegym.qlbanhang.model;


import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.QueryConditionDto;
import vn.codegym.qlbanhang.entity.StockEntity;
import vn.codegym.qlbanhang.enums.StockTransType;
import vn.codegym.qlbanhang.utils.DataUtil;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;

public class StockModel extends BaseModel {
    private static final StockModel inst = new StockModel();

    private StockModel() {
        super(StockEntity.class);
    }

    public static StockModel getInstance() {
        return inst;
    }

    public StockEntity updateStock(StockEntity stockEntity, int quantity, String type) throws SQLException {
        if (type.equals(StockTransType.EXPORT.name())) {
            stockEntity.setAvailableQuantity(stockEntity.getAvailableQuantity() - quantity);
            stockEntity.setTotalQuantity(stockEntity.getTotalQuantity() - quantity);
            stockEntity.setPendingQuantity(stockEntity.getPendingQuantity() - quantity);
        } else if (type.equals(StockTransType.IMPORT.name())) {
            stockEntity.setAvailableQuantity(stockEntity.getAvailableQuantity() + quantity);
            stockEntity.setTotalQuantity(stockEntity.getTotalQuantity() + quantity);
        } else {
            stockEntity.setAvailableQuantity(stockEntity.getAvailableQuantity() - quantity);
            stockEntity.setPendingQuantity(stockEntity.getPendingQuantity() + quantity);
        }
        stockEntity.setUpdatedBy("SYSTEM");
        stockEntity.setUpdatedDate(LocalDateTime.now());
        return (StockEntity) save(stockEntity);
    }

    public StockEntity getValidStock(int productId, int quantity) throws SQLException {
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        QueryConditionDto productQueryConditionDto = QueryConditionDto.newAndCondition("product_id", "=", productId);
        QueryConditionDto quantityCon = QueryConditionDto.newAndCondition("available_quantity", ">=", quantity);
        baseSearchDto.setQueryConditionDtos(Arrays.asList(productQueryConditionDto, quantityCon));
        StockEntity stockEntity = (StockEntity) findOne(baseSearchDto);
        if (!DataUtil.isNullObject(stockEntity)) {
            return stockEntity;
        }
        return null;
    }
}
