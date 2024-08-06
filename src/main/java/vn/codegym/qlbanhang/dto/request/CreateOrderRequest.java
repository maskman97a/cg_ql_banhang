package vn.codegym.qlbanhang.dto.request;

import lombok.Getter;
import lombok.Setter;
import vn.codegym.qlbanhang.dto.CustomerDto;
import vn.codegym.qlbanhang.dto.ProductDto;

import java.util.List;

@Getter
@Setter
public class CreateOrderRequest {
    private CustomerDto customer;
    private List<ProductDto> productList;
}
