package vn.codegym.qlbanhang.model;

import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.Condition;
import vn.codegym.qlbanhang.entity.Order;

import java.sql.SQLException;
import java.util.Collections;

public class OrderModel extends BaseModel {
    public OrderModel() {
        super(Order.getTableName());
    }

    public Order getByCode(String code) throws SQLException {
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        Condition condition = new Condition();
        condition.setColumnName("code");
        condition.setOperator("=");
        condition.setValue(code);
        baseSearchDto.setConditions(Collections.singletonList(condition));
        return (Order) findOne(baseSearchDto);
    }
}
