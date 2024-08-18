package vn.codegym.qlbanhang.model;

import vn.codegym.qlbanhang.constants.Const;
import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.Condition;
import vn.codegym.qlbanhang.entity.BaseEntity;
import vn.codegym.qlbanhang.entity.Customer;

public class CustomerModel extends BaseModel {
    private static final CustomerModel instance = new CustomerModel();

    private CustomerModel() {
        super(Customer.class);
    }

    public static CustomerModel getInstance() {
        return instance;
    }


    public Customer findByPhone(String phoneNumber) {
        try {
            BaseSearchDto baseSearchDto = new BaseSearchDto();
            Condition condition1 = Condition.newAndCondition("phone", "=", phoneNumber);
            baseSearchDto.getConditions().add(condition1);

            Condition condition2 = Condition.newAndCondition("status", "=", Const.STATUS_ACTIVE);
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
