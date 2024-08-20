package vn.codegym.qlbanhang.model;

import lombok.Getter;
import vn.codegym.qlbanhang.constants.Const;
import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.QueryConditionDto;
import vn.codegym.qlbanhang.entity.BaseEntity;
import vn.codegym.qlbanhang.entity.CustomerEntity;

public class CustomerModel extends BaseModel {
    @Getter
    private static final CustomerModel instance = new CustomerModel();

    private CustomerModel() {
        super(CustomerEntity.class);
    }


    public CustomerEntity findByPhone(String phoneNumber) {
        try {
            BaseSearchDto baseSearchDto = new BaseSearchDto();
            QueryConditionDto queryConditionDto1 = QueryConditionDto.newAndCondition("phone", "=", phoneNumber);
            baseSearchDto.getQueryConditionDtos().add(queryConditionDto1);

            QueryConditionDto queryConditionDto2 = QueryConditionDto.newAndCondition("status", "=", Const.STATUS_ACTIVE);
            baseSearchDto.getQueryConditionDtos().add(queryConditionDto2);
            BaseEntity baseEntity = findOne(baseSearchDto);
            if (baseEntity != null) {
                return (CustomerEntity) baseEntity;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
