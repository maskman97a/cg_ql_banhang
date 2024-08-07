package vn.codegym.qlbanhang.service;

import vn.codegym.qlbanhang.constants.Const;
import vn.codegym.qlbanhang.dto.CustomerDto;
import vn.codegym.qlbanhang.dto.ProductDto;
import vn.codegym.qlbanhang.dto.request.CreateOrderRequest;
import vn.codegym.qlbanhang.dto.response.BaseResponse;
import vn.codegym.qlbanhang.entity.Customer;
import vn.codegym.qlbanhang.entity.Order;
import vn.codegym.qlbanhang.entity.OrderDetail;
import vn.codegym.qlbanhang.entity.Product;
import vn.codegym.qlbanhang.model.CustomerModel;
import vn.codegym.qlbanhang.model.OrderDetailModel;
import vn.codegym.qlbanhang.model.OrderModel;
import vn.codegym.qlbanhang.model.ProductModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderService extends HomeService {
    private final ProductModel productModel;
    private final CustomerModel customerModel;
    private final OrderModel orderModel;
    private final OrderDetailModel orderDetailModel;

    public OrderService() {
        super(new ProductModel());
        this.productModel = (ProductModel) super.baseModel;
        this.customerModel = new CustomerModel();
        this.orderModel = new OrderModel();
        this.orderDetailModel = new OrderDetailModel();
    }

    public void executeCreateOrder(HttpServletRequest req, HttpServletResponse resp) {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        String customerName = req.getParameter("customer-name");
        String customerPhoneNumber = req.getParameter("customer-phone");
        String customerEmail = req.getParameter("customer-email");
        String customerAddress = req.getParameter("customer-address");
        CustomerDto customerDto = new CustomerDto(customerName, customerPhoneNumber, customerEmail, customerAddress);
        createOrderRequest.setCustomer(customerDto);

        List<ProductDto> productList = new ArrayList<>();
        ProductDto productDto = new ProductDto();
        String productIdStr = req.getParameter("product-id");
        if (productIdStr != null && !productIdStr.isEmpty()) {
            productDto.setId(Integer.parseInt(productIdStr));
        }
        String quantityStr = req.getParameter("quantity");
        if (quantityStr != null && !quantityStr.isEmpty()) {
            productDto.setQuantity(Integer.parseInt(quantityStr));
        }
        productList.add(productDto);
        createOrder(createOrderRequest);
    }

    public BaseResponse createOrder(CreateOrderRequest createOrderRequest) {
        BaseResponse baseResponse = new BaseResponse();
        CustomerDto customerDto = createOrderRequest.getCustomer();
        Customer customer = customerModel.findByPhone(customerDto.getCustomerPhoneNumber());
        if (customer == null) {
            Customer newCustomer = new Customer();
            newCustomer.setName(customerDto.getCustomerName());
            newCustomer.setPhone(customerDto.getCustomerPhoneNumber());
            newCustomer.setEmail(customerDto.getCustomerEmail());
            newCustomer.setAddress(customerDto.getCustomerAddress());
            customerModel.save(newCustomer);

            customer = customerModel.findByPhone(customerDto.getCustomerPhoneNumber());
        }
        Order order = new Order();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String orderCode = "ORD_" + simpleDateFormat.format(new Date());
        order.setStatus(Const.OrderStatus.NEW);
        order.setCode(orderCode);
        order.setCustomerId(customer.getId());
        order.setAddress(createOrderRequest.getCustomer().getCustomerAddress());
        order.setOrderDate(new Date());
        orderModel.save(order);
        order = orderModel.getByCode(orderCode);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ProductDto productDto : createOrderRequest.getProductList()) {
            Product product = productModel.findProductById(productDto.getId());
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProductId(productDto.getId());
            orderDetail.setOrderId(order.getId());
            orderDetail.setQuantity(productDto.getQuantity());
            orderDetail.setUnitPrice(product.getPrice().intValue());
            orderDetailModel.save(orderDetail);
            orderDetailList.add(orderDetail);
        }
        return baseResponse;
    }
}
