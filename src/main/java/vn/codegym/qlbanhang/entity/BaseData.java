package vn.codegym.qlbanhang.entity;

import lombok.Getter;
import lombok.Setter;
import vn.codegym.qlbanhang.annotation.Table;

import java.util.List;


@Setter
@Getter
public class BaseData {
    int totalRow;
    List<BaseEntity> lstData;

    public BaseData(int totalRow, List<BaseEntity> lstData) {
        this.totalRow = totalRow;
        this.lstData = lstData;
    }

}
