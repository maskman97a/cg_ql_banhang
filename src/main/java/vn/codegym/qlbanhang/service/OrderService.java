package vn.codegym.qlbanhang.service;

import vn.codegym.qlbanhang.constants.Const;
import vn.codegym.qlbanhang.constants.ErrorType;
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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

    public void executeCreateOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
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
            createOrderRequest.setProductList(productList);
            BaseResponse<Order> baseResponse = createOrder(createOrderRequest);
            if (baseResponse.getErrorCode() == ErrorType.SUCCESS.getErrorCode()) {
                Order order = baseResponse.getAdditionalData();
                resp.sendRedirect("/order/success?orderId=" + order.getId());
            } else {
                resp.sendRedirect("/order/error?errorMessage=" + baseResponse.getErrorMessage());
            }
        } catch (Exception ex) {
            renderErrorPage(req, resp);
        }
    }


    public BaseResponse createOrder(CreateOrderRequest createOrderRequest) {
        BaseResponse baseResponse = new BaseResponse();
        try {
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
            order.setOrderDate(LocalDateTime.now());
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
            baseResponse.setAdditionalData(order);
        } catch (Exception ex) {
            ex.printStackTrace();
            baseResponse.setError(ErrorType.INTERNAL_SERVER_ERROR);
        }
        return baseResponse;
    }

    public void renderOrderSuccessPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String id = req.getParameter("orderId");
            Order order = (Order) orderModel.findById(Integer.parseInt(id));
            req.setAttribute("showOrderSuccess", true);
            req.setAttribute("orderCode", order.getCode());
            renderPage(req, resp);
        } catch (Exception e) {
            renderErrorPage(req, resp);
        }
    }

    public void renderOrderErrorPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("showOrderError", true);
            req.setAttribute("errorMessage", "Tạo đơn hàng thất bại, vui lòng liên hệ hotline để được hỗ trợ!");
            renderPage(req, resp);
        } catch (Exception e) {
            renderErrorPage(req, resp);
        }
    }
}
