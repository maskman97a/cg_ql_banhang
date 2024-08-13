package vn.codegym.qlbanhang.entity;

import vn.codegym.qlbanhang.annotation.Table;

import java.util.List;


public class BaseData {
    int totalRow;
    List<BaseEntity> lstData;

    public BaseData(int totalRow, List<BaseEntity> lstData) {
        this.totalRow = totalRow;
        this.lstData = lstData;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public List<BaseEntity> getLstData() {
        return lstData;
    }

    public void setLstData(List<BaseEntity> lstData) {
        this.lstData = lstData;
    }
}
