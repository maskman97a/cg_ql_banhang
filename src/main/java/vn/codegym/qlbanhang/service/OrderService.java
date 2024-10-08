package vn.codegym.qlbanhang.service;

import vn.codegym.qlbanhang.constants.Const;
import vn.codegym.qlbanhang.dto.*;
import vn.codegym.qlbanhang.dto.request.CreateOrderRequest;
import vn.codegym.qlbanhang.dto.response.BaseResponse;
import vn.codegym.qlbanhang.entity.*;
import vn.codegym.qlbanhang.enums.ErrorType;
import vn.codegym.qlbanhang.enums.OrderStatus;
import vn.codegym.qlbanhang.model.*;
import vn.codegym.qlbanhang.utils.DataUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class OrderService extends HomeService {
    private final ProductModel productModel;
    private final CustomerModel customerModel;
    private final OrderModel orderModel;
    private final OrderDetailModel orderDetailModel;
    private final EmailModel emailModel;
    private final CancelOrderOtpModel cancelOrderOtpModel;
    private final StockModel stockModel;

    public OrderService() {
        super(OrderModel.getInstance());

        this.orderModel = (OrderModel) super.getBaseModel();
        this.productModel = ProductModel.getInstance();
        this.customerModel = CustomerModel.getInstance();
        this.orderDetailModel = OrderDetailModel.getInstance();
        this.emailModel = EmailModel.getInstance();
        this.cancelOrderOtpModel = CancelOrderOtpModel.getInstance();
        this.stockModel = StockModel.getInstance();
    }

    public void executeCreateOrderSingle(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BaseResponse<OrderEntity> baseResponse = createOrder(prepareCreateOrderRequest(req, false));
        if (baseResponse.getErrorCode() == ErrorType.SUCCESS.getErrorCode()) {
            OrderEntity orderEntity = baseResponse.getAdditionalData();
            resp.sendRedirect("/order/success?orderId=" + orderEntity.getId());
        } else {
            resp.sendRedirect("/order/error?errorMessage=" + Base64.getEncoder().encodeToString(baseResponse.getErrorMessage().getBytes()));
        }

    }

    public void executeCreateOrderBatch(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        BaseResponse<OrderEntity> baseResponse = createOrder(prepareCreateOrderRequest(req, true));
        if (baseResponse.getErrorCode() == ErrorType.SUCCESS.getErrorCode()) {
            HttpSession httpSession = req.getSession();
            httpSession.setAttribute("cartProductJson", "");
            OrderEntity orderEntity = baseResponse.getAdditionalData();
            resp.sendRedirect("/order/success?orderId=" + orderEntity.getId());
        } else {
            resp.sendRedirect("/order/error?errorMessage=" + Base64.getEncoder().encodeToString(baseResponse.getErrorMessage().getBytes()));
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
        BaseResponse<OrderEntity> baseResponse = new BaseResponse<>();
        try {
            baseResponse = validateStock(createOrderRequest.getProductList());
            if (baseResponse.getErrorCode() == ErrorType.SUCCESS.getErrorCode()) {
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
                    orderDetailEntity.setProductId(productDto.getId());
                    orderDetailEntity.setOrderId(orderEntity.getId());
                    orderDetailEntity.setQuantity(productDto.getQuantity());
                    orderDetailEntity.setUnitPrice(productEntity.getPrice());
                    orderDetailEntity.setAmount(orderDetailEntity.getUnitPrice() * orderDetailEntity.getQuantity());
                    orderDetailEntity = (OrderDetailEntity) orderDetailModel.save(orderDetailEntity);
                    orderDetailEntityList.add(orderDetailEntity);
                }
                baseResponse.setAdditionalData(orderEntity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            baseResponse.setError(ErrorType.INTERNAL_SERVER_ERROR);
        }
        return baseResponse;
    }

    public BaseResponse validateStock(List<ProductDto> productDtoList) throws SQLException {
        BaseResponse baseResponse = new BaseResponse();
        for (ProductDto productDto : productDtoList) {
            BaseSearchDto baseSearchDto = new BaseSearchDto();
            baseSearchDto.getQueryConditionDtos().add(QueryConditionDto.newAndCondition("product_id", "=", productDto.getId()));
            baseSearchDto.getQueryConditionDtos().add(QueryConditionDto.newAndCondition("status", "=", Const.STATUS_ACTIVE));
            baseSearchDto.getQueryConditionDtos().add(QueryConditionDto.newAndCondition("available_quantity", ">=", productDto.getQuantity()));
            StockEntity stockEntity = (StockEntity) stockModel.findOne(baseSearchDto);
            if (DataUtil.isNullObject(stockEntity)) {
                ProductEntity productEntity = productModel.findProductById(productDto.getId());
                baseResponse.setError(ErrorType.INVALID_DATA, "Mặt hàng %s không còn đủ số lượng", productEntity.getProductName());
                return baseResponse;
            }
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
                    if (DataUtil.isNullOrEmpty(otp)) {
                        throw new Exception("Otp không hợp lệ");
                    }
                    BaseSearchDto baseSearchDto = new BaseSearchDto();
                    baseSearchDto.getQueryConditionDtos().add(QueryConditionDto.newAndCondition("expired_date", ">", LocalDateTime.now()));
                    baseSearchDto.getQueryConditionDtos().add(QueryConditionDto.newAndCondition("order_id", "=", orderEntity.getId()));
                    baseSearchDto.getQueryConditionDtos().add(QueryConditionDto.newAndCondition("status", "=", Const.STATUS_ACTIVE));
                    CancelOrderOtpEntity cancelOrderOtpEntity = (CancelOrderOtpEntity) cancelOrderOtpModel.findOne(baseSearchDto);
                    if (DataUtil.isNullObject(cancelOrderOtpEntity)) {
                        throw new Exception("OTP hết hạn!");
                    } else {
                        if (!otp.equals(cancelOrderOtpEntity.getOtp())) {
                            throw new Exception("OTP không hợp lệ!");
                        }
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
                        req.setAttribute("renderOrderLink", true);
                        CustomerEntity customerEntity = (CustomerEntity) customerModel.findById(orderEntity.getCustomerId());
                        if (!DataUtil.isNullObject(customerEntity)) {
                            req.setAttribute("phoneNumber", customerEntity.getPhone());
                            req.setAttribute("orderCode", orderEntity.getCode());
                        }
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

    public void renderOrderSuccessPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        String id = req.getParameter("orderId");
        OrderEntity orderEntity = (OrderEntity) orderModel.findById(Integer.parseInt(id));
        CustomerEntity customerEntity = (CustomerEntity) customerModel.findById(orderEntity.getCustomerId());
        req.setAttribute("showOrderSuccess", true);
        req.setAttribute("phoneNumber", customerEntity.getPhone());
        req.setAttribute("orderCode", orderEntity.getCode());
        renderPage(req, resp);

    }

    public void renderOrderErrorPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("showOrderError", true);
        if (req.getParameter("errorMessage") != null && !req.getParameter("errorMessage").isEmpty()) {
            String errorMsg = req.getParameter("errorMessage");
            req.setAttribute("errorMessage", new String(Base64.getDecoder().decode(errorMsg)));
        } else {
            req.setAttribute("errorMessage", "Tạo đơn hàng thất bại, vui lòng liên hệ hotline để được hỗ trợ!");
        }
        renderPage(req, resp);

    }

    public void renderLookupOrderPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute("showLookupOrder", true);

        renderPage(req, resp);

    }

    public void executeLookupOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        String orderCode = req.getParameter("orderCode");
        req.setAttribute("orderCode", orderCode);
        if (DataUtil.isNullOrEmpty(orderCode)) {
            req.setAttribute("lookupResponse", "Vui lòng nhập Mã đơn hàng");
            renderLookupOrderPage(req, resp);
            return;
        }
        String phoneNumber = req.getParameter("phoneNumber");
        req.setAttribute("phoneNumber", phoneNumber);
        if (DataUtil.isNullOrEmpty(phoneNumber)) {
            req.setAttribute("lookupResponse", "Vui lòng nhập Số điện thoại đặt hàng");
            renderLookupOrderPage(req, resp);
            return;
        }

        BaseSearchDto baseSearchDto = new BaseSearchDto();
        baseSearchDto.getQueryConditionDtos().add(QueryConditionDto.newAndCondition("phone_number", "=", phoneNumber));
        CustomerEntity customerEntity = customerModel.findByPhone(phoneNumber);
        if (!DataUtil.isNullObject(customerEntity)) {
            baseSearchDto.getQueryConditionDtos().add(QueryConditionDto.newOrCondition("customer_id", "=", customerEntity.getId()));
        } else {
            req.setAttribute("lookupResponse", "Không tìm thấy đơn hàng");
            renderLookupOrderPage(req, resp);
            return;
        }
        baseSearchDto = new BaseSearchDto();
        baseSearchDto.getQueryConditionDtos().add(QueryConditionDto.newAndCondition("code", "=", orderCode));
        baseSearchDto.getQueryConditionDtos().add(QueryConditionDto.newAndCondition("customer_id", "=", customerEntity.getId()));
        baseSearchDto.getOrderByConditionDtos().add(new OrderByConditionDto("updated_date", "DESC"));
        List<BaseEntity> baseEntities = orderModel.search(baseSearchDto);
        if (DataUtil.isNullOrEmpty(baseEntities)) {
            req.setAttribute("lookupResponse", "Không tìm thấy đơn hàng");
            renderLookupOrderPage(req, resp);
            return;
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
                Long totalAmount = 0L;
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

    }


    public void renderSearchOrderAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        req.setAttribute("renderOrder", true);
        req.setAttribute("renderCategory", false);
        req.setAttribute("renderProduct", false);
        this.searchOrderAdmin(req, resp);
        req.getRequestDispatcher(req.getContextPath() + "/views/admin/admin.jsp").forward(req, resp);

    }

    public void searchOrderAdmin(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        String keyword = DataUtil.safeToString(req.getParameter("keyword"));
        req.setAttribute("keyword", keyword);
        int size = 10;
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
        if (req.getParameter("statusOrderId") != null && !req.getParameter("statusOrderId").isEmpty())
            baseSearchDto.setStatus(DataUtil.safeToInt(Integer.parseInt(req.getParameter("statusOrderId"))));
        req.setAttribute("statusOrderId", req.getParameter("statusOrderId"));
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

    }


    public void detailOrderForAdmin(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        String orderCode = req.getParameter("orderCode");
        req.setAttribute("orderCode", orderCode);
        if (DataUtil.isNullOrEmpty(orderCode)) {
            req.setAttribute("lookupResponse", "Vui lòng nhập Mã đơn hàng");
            renderLookupOrderPage(req, resp);
            return;
        }


        BaseSearchDto baseSearchDto = new BaseSearchDto();
        baseSearchDto.getQueryConditionDtos().add(QueryConditionDto.newAndCondition("code", "=", orderCode));
        baseSearchDto.getOrderByConditionDtos().add(new OrderByConditionDto("updated_date", "DESC"));
        List<BaseEntity> baseEntities = orderModel.search(baseSearchDto);
        if (DataUtil.isNullOrEmpty(baseEntities)) {
            req.setAttribute("lookupResponse", "Không tìm thấy đơn hàng");
            renderLookupOrderPage(req, resp);
            return;
        } else {
            CustomerEntity customerEntity = new CustomerEntity();
            req.setAttribute("showOrderInfo", true);
            for (BaseEntity baseEntity : baseEntities) {
                OrderEntity orderEntity = (OrderEntity) baseEntity;
                customerEntity = (CustomerEntity) customerModel.findById(orderEntity.getCustomerId());
                orderEntity.setOrderStatusName(OrderStatus.getDescription(orderEntity.getStatus()));
                BaseSearchDto baseSearchDtoForDetail = new BaseSearchDto();
                baseSearchDtoForDetail.getQueryConditionDtos().add(QueryConditionDto.newAndCondition("order_id", "=", orderEntity.getId()));
                List<BaseEntity> baseEntitiesForDetail = orderDetailModel.search(baseSearchDtoForDetail);
                orderEntity.setOrderDetailEntityList(new ArrayList<>());
                Long totalAmount = 0L;
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
            req.setAttribute("orderList", baseEntities);
        }

    }

    public BaseResponse prepareCancel(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        BaseResponse baseResponse = new BaseResponse();
        try {
            String orderCode = req.getParameter("orderCode");
            req.setAttribute("orderCode", orderCode);
            if (DataUtil.isNullOrEmpty(orderCode)) {
                baseResponse.setError(ErrorType.INVALID_DATA, "Vui lòng nhập mã đơn hàng!");
            }

            BaseSearchDto baseSearchDto = new BaseSearchDto();
            baseSearchDto.getQueryConditionDtos().add(QueryConditionDto.newAndCondition("code", "=", orderCode));
            baseSearchDto.getQueryConditionDtos().add(QueryConditionDto.newAndCondition("status", "=", OrderStatus.NEW.value));
            OrderEntity orderEntity = (OrderEntity) orderModel.findOne(baseSearchDto);
            if (DataUtil.isNullObject(orderEntity)) {
                baseResponse.setError(ErrorType.INVALID_DATA, "Thông tin đơn hàng không hợp lệ");
            }
            CustomerEntity customerEntity = (CustomerEntity) customerModel.findById(orderEntity.getCustomerId());
            if (DataUtil.isNullObject(customerEntity)) {
                baseResponse.setError(ErrorType.INVALID_DATA, "Thông tin khách hàng không hợp lệ");
            }
            Random random = new Random();
            int randomNumber = 100000 + random.nextInt(900000);

            BaseSearchDto baseSearchForCancelOrderOtp = new BaseSearchDto();
            baseSearchForCancelOrderOtp.getQueryConditionDtos().add(QueryConditionDto.newAndCondition("order_id", "=", orderEntity.getId()));
            baseSearchForCancelOrderOtp.getQueryConditionDtos().add(QueryConditionDto.newAndCondition("expired_date", ">", LocalDateTime.now()));
            CancelOrderOtpEntity cancelOrderOtpEntity = (CancelOrderOtpEntity) cancelOrderOtpModel.findOne(baseSearchForCancelOrderOtp);
            String otp;
            if (DataUtil.isNullObject(cancelOrderOtpEntity)) {
                otp = String.valueOf(randomNumber);
                cancelOrderOtpEntity = new CancelOrderOtpEntity();
                cancelOrderOtpEntity.setOrderId(orderEntity.getId());
                cancelOrderOtpEntity.setOtp(otp);
                LocalDateTime expiredDate = LocalDateTime.now().plusMinutes(5);
                cancelOrderOtpEntity.setExpiredDate(expiredDate);
                cancelOrderOtpModel.save(cancelOrderOtpEntity);
            } else {
                otp = cancelOrderOtpEntity.getOtp();
            }

            byte[] mailBody = createMailCancelOrderOtpContent(orderCode, otp);
            EmailEntity emailEntity = new EmailEntity();
            emailEntity.setMailBody(mailBody);
            emailEntity.setStatus(0);
            emailEntity.setReceiver(customerEntity.getEmail());
            emailEntity.setMailTitle("OTP xác nhận hủy đơn hàng: " + orderCode);
            emailModel.save(emailEntity);
        } catch (Exception ex) {
            baseResponse.setError(ErrorType.INTERNAL_SERVER_ERROR);
        }

        resp.getWriter().write(gson.toJson(baseResponse));
        resp.setContentType("application/json; charset=UTF-8");
        return baseResponse;
    }

    public byte[] createMailCancelOrderOtpContent(String orderCode, String otp) throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream("/html/otp-email.html");
        StringBuilder htmlContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            String line;
            while ((line = reader.readLine()) != null) {
                htmlContent.append(line).append("\n");
            }
        }
        input.close();
        String htmlContentStr = htmlContent.toString();
        htmlContentStr = htmlContentStr.replace("{{otp}}", otp);
        htmlContentStr = htmlContentStr.replace("{{orderCode}}", orderCode);
        htmlContentStr = htmlContentStr.replace("{{hotline}}", "0328760158");

        return Base64.getEncoder().encode(htmlContentStr.getBytes());
    }
}
