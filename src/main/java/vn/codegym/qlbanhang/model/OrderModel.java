package vn.codegym.qlbanhang.model;

import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.Condition;
import vn.codegym.qlbanhang.entity.Order;

import java.util.Collections;

public class OrderModel extends BaseModel {
    public OrderModel() {
        super(Order.getTableName());
    }

    public Order getByCode(String code) {
        try {
            BaseSearchDto baseSearchDto = new BaseSearchDto();
            Condition condition = new Condition();
            condition.setColumnName("code");
            condition.setOperator("=");
            condition.setValue(code);
            baseSearchDto.setConditions(Collections.singletonList(condition));
            return (Order) findOne(baseSearchDto);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
