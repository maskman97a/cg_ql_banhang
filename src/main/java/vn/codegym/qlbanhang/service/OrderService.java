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
        super(OrderModel.getInstance());

        this.orderModel = (OrderModel) super.getBaseModel();
        this.productModel = ProductModel.getInstance();
        this.customerModel = CustomerModel.getInstance();
        this.orderDetailModel = OrderDetailModel.getInstance();
    }

    public void executeCreateOrderSingle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            BaseResponse<OrderEntity> baseResponse = createOrder(prepareCreateOrderRequest(req, false));
            if (baseResponse.getErrorCode() == ErrorType.SUCCESS.getErrorCode()) {
                OrderEntity orderEntity = baseResponse.getAdditionalData();
                resp.sendRedirect("/order/success?orderId=" + orderEntity.getId());
            } else {
                resp.sendRedirect("/order/error?errorMessage=" + baseResponse.getErrorMessage());
            }
        } catch (Exception ex) {
            renderErrorPage(req, resp);
        }
    }

    public void executeCreateOrderBatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            BaseResponse<OrderEntity> baseResponse = createOrder(prepareCreateOrderRequest(req, true));
            if (baseResponse.getErrorCode() == ErrorType.SUCCESS.getErrorCode()) {
                HttpSession httpSession = req.getSession();
                httpSession.setAttribute("cartProductJson", "");
                OrderEntity orderEntity = baseResponse.getAdditionalData();
                resp.sendRedirect("/order/success?orderId=" + orderEntity.getId());
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

    public BaseResponse<OrderEntity> createOrder(CreateOrderRequest createOrderRequest) {
        BaseResponse<OrderEntity> baseResponse = new BaseResponse();
        try {
            CustomerDto customerDto = createOrderRequest.getCustomer();
            CustomerEntity customerEntity = customerModel.findByPhone(customerDto.getCustomerPhoneNumber());
            if (customerEntity == null) {
                CustomerEntity newCustomerEntity = new CustomerEntity();
                newCustomerEntity.setName(customerDto.getCustomerName());
                newCustomerEntity.setPhone(customerDto.getCustomerPhoneNumber());
                newCustomerEntity.setEmail(customerDto.getCustomerEmail());
                newCustomerEntity.setAddress(customerDto.getCustomerAddress());
                customerModel.save(newCustomerEntity);

                customerEntity = customerModel.findByPhone(customerDto.getCustomerPhoneNumber());
            }
            OrderEntity orderEntity = new OrderEntity();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String orderCode = "ORD_" + simpleDateFormat.format(new Date());
            int orderID = orderModel.getNextID();
            orderEntity.setStatus(Const.OrderStatus.NEW);
            orderEntity.setCode(orderCode);
            orderEntity.setCustomerId(customerEntity.getId());
            orderEntity.setAddress(createOrderRequest.getCustomer().getCustomerAddress());
            orderEntity.setOrderDate(LocalDateTime.now());
            orderEntity = (OrderEntity) orderModel.save(orderEntity);

            List<OrderDetailEntity> orderDetailEntityList = new ArrayList<>();
            for (ProductDto productDto : createOrderRequest.getProductList()) {
                ProductEntity productEntity = productModel.findProductById(productDto.getId());
                OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
                orderDetailEntity.setOrderId(orderID);
                orderDetailEntity.setProductId(productDto.getId());
                orderDetailEntity.setOrderId(orderEntity.getId());
                orderDetailEntity.setQuantity(productDto.getQuantity());
                orderDetailEntity.setUnitPrice(productEntity.getPrice().intValue());
                orderDetailEntity = (OrderDetailEntity) orderDetailModel.save(orderDetailEntity);
                orderDetailEntityList.add(orderDetailEntity);
            }
            baseResponse.setAdditionalData(orderEntity);
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
                OrderEntity orderEntity = (OrderEntity) orderModel.findById(orderId);
                if (orderEntity != null) {
                    String otp = req.getParameter("otp");
                    if (DataUtil.isNullOrEmpty(otp) || !otp.equals("000000")) {
                        throw new Exception("Otp không hợp lệ");
                    }
                    if (orderEntity.getStatus() != OrderStatus.NEW.getValue()) {
                        throw new Exception("Đơn hàng ở trạng thái không hợp lệ");
                    }
                    orderEntity.setStatus(OrderStatus.CANCELED.getValue());
                    orderEntity.setUpdatedBy("CUSTOMER");
                    orderEntity.setUpdatedDate(LocalDateTime.now());
                    orderEntity = (OrderEntity) orderModel.save(orderEntity);
                    if (!DataUtil.isNullObject(orderEntity)) {
                        req.setAttribute("successResponse", "Hủy đơn hàng " + orderEntity.getCode() + " thành công!");
                    } else {
                        responseMessage = "Hủy đơn hàng " + orderEntity.getCode() + " thất bại!";
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
            OrderEntity orderEntity = (OrderEntity) orderModel.findById(Integer.parseInt(id));
            req.setAttribute("showOrderSuccess", true);
            req.setAttribute("orderCode", orderEntity.getCode());
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
            baseSearchDto.getQueryConditionDtos().add(QueryConditionDto.newAndCondition("code", "=", orderCode));
            CustomerEntity customerEntity = customerModel.findByPhone(orderCode);
            if (!DataUtil.isNullObject(customerEntity)) {
                baseSearchDto.getQueryConditionDtos().add(QueryConditionDto.newOrCondition("customer_id", "=", customerEntity.getId()));
            }
            List<BaseEntity> baseEntities = orderModel.search(baseSearchDto);
            if (DataUtil.isNullOrEmpty(baseEntities)) {
                req.setAttribute("lookupResponse", "Không tìm thấy đơn hàng");
            } else {
                req.setAttribute("showOrderInfo", true);
                req.setAttribute("orderList", baseEntities);
                for (BaseEntity baseEntity : baseEntities) {
                    OrderEntity orderEntity = (OrderEntity) baseEntity;
                    if (DataUtil.isNullObject(customerEntity)) {
                        customerEntity = (CustomerEntity) customerModel.findById(orderEntity.getCustomerId());
                    }
                    orderEntity.setOrderStatusName(OrderStatus.getDescription(orderEntity.getStatus()));
                    BaseSearchDto baseSearchDtoForDetail = new BaseSearchDto();
                    baseSearchDtoForDetail.getQueryConditionDtos().add(QueryConditionDto.newAndCondition("order_id", "=", orderEntity.getId()));
                    List<BaseEntity> baseEntitiesForDetail = orderDetailModel.search(baseSearchDtoForDetail);
                    orderEntity.setOrderDetailEntityList(new ArrayList<>());
                    int totalAmount = 0;
                    int index = 1;
                    for (BaseEntity baseE2 : baseEntitiesForDetail) {
                        OrderDetailEntity orderDetailEntity = (OrderDetailEntity) baseE2;
                        orderDetailEntity.setIndex(index++);
                        totalAmount += orderDetailEntity.getAmount();
                        ProductEntity productEntity = (ProductEntity) productModel.findById(orderDetailEntity.getProductId());
                        orderDetailEntity.setProductEntity(productEntity);
                        orderEntity.getOrderDetailEntityList().add(orderDetailEntity);
                    }
                    orderEntity.setTotalAmount(totalAmount);
                }
                req.setAttribute("customerInfo", customerEntity);
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
            baseSearchDto.getQueryConditionDtos().add(QueryConditionDto.newAndCondition("code", "=", orderCode));
            CustomerEntity customerEntity = customerModel.findByPhone(orderCode);
            if (!DataUtil.isNullObject(customerEntity)) {
                baseSearchDto.getQueryConditionDtos().add(QueryConditionDto.newOrCondition("customer_id", "=", customerEntity.getId()));
            }
            List<BaseEntity> baseEntities = orderModel.search(baseSearchDto);
            if (DataUtil.isNullOrEmpty(baseEntities)) {
                req.setAttribute("lookupResponse", "Không tìm thấy đơn hàng");
            } else {
                req.setAttribute("showOrderInfo", true);
                req.setAttribute("orderList", baseEntities);
                for (BaseEntity baseEntity : baseEntities) {
                    OrderEntity orderEntity = (OrderEntity) baseEntity;
                    if (DataUtil.isNullObject(customerEntity)) {
                        customerEntity = (CustomerEntity) customerModel.findById(orderEntity.getCustomerId());
                    }
                    orderEntity.setOrderStatusName(OrderStatus.getDescription(orderEntity.getStatus()));
                    BaseSearchDto baseSearchDtoForDetail = new BaseSearchDto();
                    baseSearchDtoForDetail.getQueryConditionDtos().add(QueryConditionDto.newAndCondition("order_id", "=", orderEntity.getId()));
                    List<BaseEntity> baseEntitiesForDetail = orderDetailModel.search(baseSearchDtoForDetail);
                    orderEntity.setOrderDetailEntityList(new ArrayList<>());
                    int totalAmount = 0;
                    int index = 1;
                    for (BaseEntity baseE2 : baseEntitiesForDetail) {
                        OrderDetailEntity orderDetailEntity = (OrderDetailEntity) baseE2;
                        orderDetailEntity.setIndex(index++);
                        totalAmount += orderDetailEntity.getAmount();
                        ProductEntity productEntity = (ProductEntity) productModel.findById(orderDetailEntity.getProductId());
                        orderDetailEntity.setProductEntity(productEntity);
                        orderEntity.getOrderDetailEntityList().add(orderDetailEntity);
                    }
                    orderEntity.setTotalAmount(totalAmount);
                }
                req.setAttribute("customerInfo", customerEntity);
            }
        } catch (Exception e) {
            renderErrorPage(req, resp);
        }
    }
}
