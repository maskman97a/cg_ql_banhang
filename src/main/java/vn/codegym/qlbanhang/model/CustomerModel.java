package vn.codegym.qlbanhang.model;

import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.Condition;
import vn.codegym.qlbanhang.entity.BaseEntity;
import vn.codegym.qlbanhang.entity.Customer;

import java.util.Collections;

public class CustomerModel extends BaseModel {
    public CustomerModel() {
        super(Customer.getTableName());
    }

    public Customer findByPhone(String phoneNumber) {
        try {
            BaseSearchDto baseSearchDto = new BaseSearchDto();
            Condition condition = new Condition();
            condition.setColumnName("phone");
            condition.setOperator("=");
            condition.setValue(phoneNumber);
            baseSearchDto.setConditions(Collections.singletonList(condition));
            BaseEntity baseEntity = findOne(baseSearchDto);
            if (baseEntity != null) {
                return (Customer) baseEntity;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
