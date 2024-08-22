package vn.codegym.qlbanhang.model;


import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.QueryConditionDto;
import vn.codegym.qlbanhang.dto.StockDto;
import vn.codegym.qlbanhang.entity.StockEntity;
import vn.codegym.qlbanhang.enums.StockTransType;
import vn.codegym.qlbanhang.utils.DataUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public List<StockDto> searchListStock(String keyword) throws SQLException {
        List<StockDto> stockList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("select product_code, product_name, available_quantity, pending_quantity, total_quantity " +
                "from stock s " +
                "         inner join product p on s.product_id = p.id and p.status = 1 and p.product_name like ? " +
                "where s.status = 1");
        PreparedStatement ps = getConnection().prepareStatement(sb.toString());
        ps.setString(1, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            StockDto stockDto = new StockDto();
            stockDto.setProductCode(rs.getString("product_code"));
            stockDto.setProductName(rs.getString("product_name"));
            stockDto.setAvailableQuantity(rs.getInt("available_quantity"));
            stockDto.setPendingQuantity(rs.getInt("pending_quantity"));
            stockDto.setTotalQuantity(rs.getInt("total_quantity"));
            stockList.add(stockDto);
        }
        return stockList;
    }
}
