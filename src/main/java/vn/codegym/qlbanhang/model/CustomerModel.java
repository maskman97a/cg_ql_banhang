package vn.codegym.qlbanhang.model;

import vn.codegym.qlbanhang.constants.Const;
import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.Condition;
import vn.codegym.qlbanhang.entity.BaseEntity;
import vn.codegym.qlbanhang.entity.Customer;

public class CustomerModel extends BaseModel {
    public CustomerModel() {
        super(Customer.class);
    }

    public Customer findByPhone(String phoneNumber) {
        try {
            BaseSearchDto baseSearchDto = new BaseSearchDto();
            Condition condition1 = new Condition();
            condition1.setColumnName("phone");
            condition1.setOperator("=");
            condition1.setValue(phoneNumber);
            baseSearchDto.getConditions().add(condition1);

            Condition condition2 = new Condition();
            condition2.setColumnName("status");
            condition2.setOperator("=");
            condition2.setValue(Const.STATUS_ACTIVE);
            baseSearchDto.getConditions().add(condition2);
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
