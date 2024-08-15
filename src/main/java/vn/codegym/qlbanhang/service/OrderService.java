package vn.codegym.qlbanhang.service;

import vn.codegym.qlbanhang.constants.Const;
import vn.codegym.qlbanhang.dto.*;
import vn.codegym.qlbanhang.dto.request.CreateOrderRequest;
import vn.codegym.qlbanhang.dto.response.BaseResponse;
import vn.codegym.qlbanhang.entity.*;
import vn.codegym.qlbanhang.enums.ErrorType;
import vn.codegym.qlbanhang.enums.OrderStatus;
import vn.codegym.qlbanhang.model.CustomerModel;
import vn.codegym.qlbanhang.model.OrderDetailModel;
import vn.codegym.qlbanhang.model.OrderModel;
import vn.codegym.qlbanhang.model.ProductModel;
import vn.codegym.qlbanhang.utils.DataUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
        super(new OrderModel());
        this.productModel = new ProductModel();
        this.customerModel = new CustomerModel();
        this.orderModel = (OrderModel) super.getBaseModal();
        this.orderDetailModel = new OrderDetailModel();
    }

    public void executeCreateOrderSingle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            BaseResponse<Order> baseResponse = createOrder(prepareCreateOrderRequest(req, false));
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

    public void executeCreateOrderBatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            BaseResponse<Order> baseResponse = createOrder(prepareCreateOrderRequest(req, true));
            if (baseResponse.getErrorCode() == ErrorType.SUCCESS.getErrorCode()) {
                HttpSession httpSession = req.getSession();
                httpSession.setAttribute("cartProductJson", "");
                Order order = baseResponse.getAdditionalData();
                resp.sendRedirect("/order/success?orderId=" + order.getId());
            } else {
                resp.sendRedirect("/order/error?errorMessage=" + baseResponse.getErrorMessage());
            }
        } catch (Exception ex) {
            renderErrorPage(req, resp);
        }
    }

    public CreateOrderRequest prepareCreateOrderRequest(HttpServletRequest req, boolean isBatch) {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        String customerName = req.getParameter("customer-name");
        String customerPhoneNumber = req.getParameter("customer-phone");
        String customerEmail = req.getParameter("customer-email");
        String customerAddress = req.getParameter("customer-address");
        CustomerDto customerDto = new CustomerDto(customerName, customerPhoneNumber, customerEmail, customerAddress);
        createOrderRequest.setCustomer(customerDto);
        if (isBatch) {
            String rowCountStr = req.getParameter("rowCount");
            if (rowCountStr != null && !rowCountStr.isEmpty()) {
                int rowCount = Integer.parseInt(rowCountStr);
                for (int i = 1; i <= rowCount; i++) {
                    String productIdStr = req.getParameter("inp-product-id-" + i);
                    String quantityStr = req.getParameter("inp-quantity-" + i);
                    if (productIdStr != null && !productIdStr.isEmpty() && quantityStr != null && !quantityStr.isEmpty()) {
                        ProductDto productDto = new ProductDto();
                        productDto.setId(Integer.parseInt(productIdStr));
                        productDto.setQuantity(Integer.parseInt(quantityStr));
                        createOrderRequest.getProductList().add(productDto);
                    }
                }
            }
        } else {
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
        }
        return createOrderRequest;
    }

    public BaseResponse<Order> createOrder(CreateOrderRequest createOrderRequest) {
        BaseResponse<Order> baseResponse = new BaseResponse();
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

    public void executeCancelOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String responseMessage = "";
        try {
            String orderIdStr = req.getParameter("orderId");
            if (!DataUtil.isNullOrEmpty(orderIdStr)) {
                int orderId = Integer.parseInt(orderIdStr);
                Order order = (Order) orderModel.findById(orderId);
                if (order != null) {
                    String otp = req.getParameter("otp");
                    if (DataUtil.isNullOrEmpty(otp) || !otp.equals("000000")) {
                        throw new Exception("Otp không hợp lệ");
                    }
                    if (order.getStatus() != OrderStatus.NEW.getValue()) {
                        throw new Exception("Đơn hàng ở trạng thái không hợp lệ");
                    }
                    order.setStatus(OrderStatus.CANCELED.getValue());
                    order.setUpdatedBy("CUSTOMER");
                    order.setUpdatedDate(LocalDateTime.now());
                    int updateRecord = orderModel.save(order);
                    if (updateRecord == 1) {
                        req.setAttribute("successResponse", "Hủy đơn hàng " + order.getCode() + " thành công!");
                    } else {
                        responseMessage = "Hủy đơn hàng " + order.getCode() + " thất bại!";
                    }
                } else {
                    throw new Exception("Mã đơn hàng không tồn tại");
                }
            } else {
                throw new Exception("Mã đơn hàng không tồn tại");
            }


        } catch (Exception ex) {
            responseMessage = "Hủy đơn hàng thất bại. " + ex.getMessage();
        }
        req.setAttribute("errorResponse", responseMessage);
        renderLookupOrderPage(req, resp);
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

    public void renderLookupOrderPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("showLookupOrder", true);

            renderPage(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            renderErrorPage(req, resp);
        }
    }

    public void executeLookupOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String orderCode = req.getParameter("orderCode");
            req.setAttribute("orderCode", orderCode);
            if (DataUtil.isNullOrEmpty(orderCode)) {
                req.setAttribute("lookupResponse", "Vui lòng nhập Mã đơn hàng");
            }

            BaseSearchDto baseSearchDto = new BaseSearchDto();
            baseSearchDto.getConditions().add(new Condition("code", "=", orderCode, "AND"));
            Customer customer = customerModel.findByPhone(orderCode);
            if (!DataUtil.isNullObject(customer)) {
                baseSearchDto.getConditions().add(new Condition("customer_id", "=", customer.getId(), "OR"));
            }
            List<BaseEntity> baseEntities = orderModel.search(baseSearchDto);
            if (DataUtil.isNullOrEmpty(baseEntities)) {
                req.setAttribute("lookupResponse", "Không tìm thấy đơn hàng");
            } else {
                req.setAttribute("showOrderInfo", true);
                req.setAttribute("orderList", baseEntities);
                for (BaseEntity baseEntity : baseEntities) {
                    Order order = (Order) baseEntity;
                    if (DataUtil.isNullObject(customer)) {
                        customer = (Customer) customerModel.findById(order.getCustomerId());
                    }
                    order.setOrderStatusName(OrderStatus.getDescription(order.getStatus()));
                    BaseSearchDto baseSearchDtoForDetail = new BaseSearchDto();
                    baseSearchDtoForDetail.getConditions().add(new Condition("order_id", "=", order.getId(), "AND"));
                    List<BaseEntity> baseEntitiesForDetail = orderDetailModel.search(baseSearchDtoForDetail);
                    order.setOrderDetailList(new ArrayList<>());
                    int totalAmount = 0;
                    int index = 1;
                    for (BaseEntity baseE2 : baseEntitiesForDetail) {
                        OrderDetail orderDetail = (OrderDetail) baseE2;
                        orderDetail.setIndex(index++);
                        totalAmount += orderDetail.getAmount();
                        Product product = (Product) productModel.findById(orderDetail.getProductId());
                        orderDetail.setProduct(product);
                        order.getOrderDetailList().add(orderDetail);
                    }
                    order.setTotalAmount(totalAmount);
                }
                req.setAttribute("customerInfo", customer);
            }

            renderLookupOrderPage(req, resp);
        } catch (Exception e) {
            renderErrorPage(req, resp);
        }
    }


    public void renderSearchOrderAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("renderOrder", true);
            req.setAttribute("renderCategory", false);
            req.setAttribute("renderProduct", false);
            this.searchOrderAdmin(req, resp);
            req.getRequestDispatcher(req.getContextPath() + "/views/admin/admin.jsp").forward(req, resp);
        } catch (Exception ex) {
            renderErrorPage(req, resp);
        }
    }

    public void searchOrderAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        try {
            String keyword = DataUtil.safeToString(req.getParameter("keyword"));
            int size = 5;
            if (req.getParameter("size") != null) {
                size = Integer.parseInt(req.getParameter("size"));
            }
            int page = 1;
            if (req.getParameter("page") != null) {
                page = Integer.parseInt(req.getParameter("page"));
                if (page == 0) page = 1;
            }
            req.setAttribute("currentPage", page);
            req.getAttribute("currentPage");
            baseSearchDto.setKeyword(keyword);
            baseSearchDto.setSize(size);
            baseSearchDto.setPage(page);
            if (req.getParameter("status-order-id") != null && !req.getParameter("status-order-id").isEmpty())
                baseSearchDto.setStatus(DataUtil.safeToInt(Integer.parseInt(req.getParameter("status-order-id"))));
            List<OrdersDto> lstData = orderModel.findOrderByKeyword(baseSearchDto);
            if (lstData != null && !lstData.isEmpty()) {
                int index = 1;
                for (OrdersDto ordersDto : lstData) {
                    ordersDto.setIndex(index++);
                }
                req.setAttribute("lstOrder", lstData);
            }
            int count = orderModel.countOrder(baseSearchDto);
            getPaging(req, resp, count, size, page);
        } catch (Exception ex) {
            renderErrorPage(req, resp, ex.getMessage());
        }
    }


    public void detailOrderForAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String orderCode = req.getParameter("orderCode");
            req.setAttribute("orderCode", orderCode);
            if (DataUtil.isNullOrEmpty(orderCode)) {
                req.setAttribute("lookupResponse", "Vui lòng nhập Mã đơn hàng");
            }

            BaseSearchDto baseSearchDto = new BaseSearchDto();
            baseSearchDto.getConditions().add(new Condition("code", "=", orderCode, "AND"));
            Customer customer = customerModel.findByPhone(orderCode);
            if (!DataUtil.isNullObject(customer)) {
                baseSearchDto.getConditions().add(new Condition("customer_id", "=", customer.getId(), "OR"));
            }
            List<BaseEntity> baseEntities = orderModel.search(baseSearchDto);
            if (DataUtil.isNullOrEmpty(baseEntities)) {
                req.setAttribute("lookupResponse", "Không tìm thấy đơn hàng");
            } else {
                req.setAttribute("showOrderInfo", true);
                req.setAttribute("orderList", baseEntities);
                for (BaseEntity baseEntity : baseEntities) {
                    Order order = (Order) baseEntity;
                    if (DataUtil.isNullObject(customer)) {
                        customer = (Customer) customerModel.findById(order.getCustomerId());
                    }
                    order.setOrderStatusName(OrderStatus.getDescription(order.getStatus()));
                    BaseSearchDto baseSearchDtoForDetail = new BaseSearchDto();
                    baseSearchDtoForDetail.getConditions().add(new Condition("order_id", "=", order.getId(), "AND"));
                    List<BaseEntity> baseEntitiesForDetail = orderDetailModel.search(baseSearchDtoForDetail);
                    order.setOrderDetailList(new ArrayList<>());
                    int totalAmount = 0;
                    int index = 1;
                    for (BaseEntity baseE2 : baseEntitiesForDetail) {
                        OrderDetail orderDetail = (OrderDetail) baseE2;
                        orderDetail.setIndex(index++);
                        totalAmount += orderDetail.getAmount();
                        Product product = (Product) productModel.findById(orderDetail.getProductId());
                        orderDetail.setProduct(product);
                        order.getOrderDetailList().add(orderDetail);
                    }
                    order.setTotalAmount(totalAmount);
                }
                req.setAttribute("customerInfo", customer);
            }
        } catch (Exception e) {
            renderErrorPage(req, resp);
        }
    }
}
