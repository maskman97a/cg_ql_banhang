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
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public StockEntity getValidStock(int productId, Integer quantity) throws SQLException {
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        QueryConditionDto productQueryConditionDto = QueryConditionDto.newAndCondition("product_id", "=", productId);
        baseSearchDto.getQueryConditionDtos().add(productQueryConditionDto);
        if (quantity != null) {
            QueryConditionDto quantityCon = QueryConditionDto.newAndCondition("available_quantity", ">=", quantity);
            baseSearchDto.getQueryConditionDtos().add(quantityCon);
        }

        StockEntity stockEntity = (StockEntity) findOne(baseSearchDto);
        if (!DataUtil.isNullObject(stockEntity)) {
            return stockEntity;
        }
        return null;
    }

    public List<StockDto> searchListStock(String keyword, int size, int page) throws SQLException {
        List<StockDto> stockList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("select product_id, product_code, product_name, available_quantity, pending_quantity, total_quantity " +
                "from stock s " +
                "         inner join product p on s.product_id = p.id and p.status = 1 " +
                "         inner join category c on c.id = p.category_id and c.status = 1 ");
        if (!DataUtil.isNullOrEmpty(keyword))
            sb.append(" and ( p.product_name like ? or p.product_code like ? ) ");
        sb.append(" where s.status = 1 ");
        sb.append(" order by c.sort, product_name asc ");
        sb.append(" limit ? offset ? ");
        int indexParam = 1;
        PreparedStatement ps = getConnection().prepareStatement(sb.toString());
        if (!DataUtil.isNullOrEmpty(keyword)) {
            ps.setString(indexParam++, "%" + keyword + "%");
            ps.setString(indexParam++, "%" + keyword + "%");
        }
        ps.setInt(indexParam++, size);
        ps.setInt(indexParam, (page - 1) * size);
        ResultSet rs = ps.executeQuery();
        DecimalFormat df = new DecimalFormat("#,###");
        int index = 1;
        while (rs.next()) {
            StockDto stockDto = new StockDto();
            stockDto.setIndex(index++);
            stockDto.setProductId(rs.getInt("product_id"));
            stockDto.setProductCode(rs.getString("product_code"));
            stockDto.setProductName(rs.getString("product_name"));
            stockDto.setAvailableQuantity(rs.getInt("available_quantity"));
            stockDto.setPendingQuantity(rs.getInt("pending_quantity"));
            stockDto.setTotalQuantity(rs.getInt("total_quantity"));
            stockDto.setStrAvailableQuantity(df.format(stockDto.getAvailableQuantity()));
            stockDto.setStrPendingQuantity(df.format(stockDto.getPendingQuantity()));
            stockDto.setStrTotalQuantity(df.format(stockDto.getTotalQuantity()));
            stockList.add(stockDto);
        }
        return stockList;
    }

    public Integer countListStock(String keyword) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT count(1) from ( ");
        sb.append("select product_code, product_name, available_quantity, pending_quantity, total_quantity " +
                "from stock s " +
                "         inner join product p on s.product_id = p.id and p.status = 1 ");
        if (!DataUtil.isNullOrEmpty(keyword))
            sb.append(" and p.product_name like ? ");
        sb.append(" where s.status = 1 ");
        sb.append(" ) b ");
        PreparedStatement ps = getConnection().prepareStatement(sb.toString());
        if (!DataUtil.isNullOrEmpty(keyword))
            ps.setString(1, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }
}
