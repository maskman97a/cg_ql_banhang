package vn.codegym.qlbanhang.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ExecuteStockDto {
    private List<UpdateStockDto> updateStockList;
    private int orderStatus;

    public ExecuteStockDto(){
        updateStockList =new ArrayList<>();
    }
}
