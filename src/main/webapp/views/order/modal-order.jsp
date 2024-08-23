<%--
  Created by IntelliJ IDEA.
  User: ceotungbeo
  Date: 7/8/24
  Time: 08:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Title</title>
    <script src="${pageContext.request.contextPath}/js/order/order.js"></script>
</head>
<body>
<div class="modal modal-xl fade" tabindex="-1" id="modalOrder" data-backdrop="static"
     data-keyboard="false" style="">
    <c:import url="../component/alert-modal.jsp"/>
    <form class="form needs-validation" action="${pageContext.request.contextPath}/order/create" method="post"
          onsubmit="return validateCreateOrder('single')">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">Thông tin đặt hàng</h4>
                    <button type="button" class="btn-close" data-dismiss="modal" aria-label="Đóng"></button>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-6">
                            <c:import url="/views/order/order-customer-info-input.jsp"/>
                        </div>
                        <div class="col-6">
                            <h6>Thông tin sản phẩm</h6>
                            <div class="col-12 card card-body">
                                <div class="row">
                                    <input type="text" value="${product.id}" name="product-id" hidden/>
                                    <div class="col-6 shadow p-3 bg-body-tertiary rounded">
                                        <img src="${pageContext.request.contextPath}/image/${product.imageUrl}"
                                             id="product-image-${product.id}"
                                             class="img-fluid col-12"
                                             alt="${product.name}" width="100%" height="100%">
                                    </div>
                                    <div class="col-6">
                                        <div class="col-12 row">
                                                <span class="col-12 form-label">
                                                    <span class="fs-4 text">${product.productName}</span></span>
                                            <span class="col-12 form-label"
                                                  style="font-size: 12px">Mã sản phẩm: ${product.productCode}</span>
                                            <input value="${product.price}" id="inp-product-price" hidden/>
                                            <span id="product-price" class="col-12 form-label">Đơn giá:
                                                    <span id="out-product-price" class="fs-5 text"
                                                          style="color:red; font-weight:bold"></span>
                                                    <span class="fs-5 text"
                                                          style="color:red; font-weight:bold">đ</span>
                                                </span>
                                            <div class="col-12 form-label">
                                                <label for="inp-quantity" class="form-label">Số lượng:</label>
                                                <input id="inp-quantity" onchange="changeProductQuantity()"
                                                       type="number"
                                                       min="1"
                                                       name="quantity" value="1"/>
                                            </div>
                                            <div class="col-12 form-label">
                                                <span>Số tiền thanh toán:</span>
                                                <div class="col-12">
                                                        <span class="fs-5 text"
                                                              style="color:red; font-weight:bold"
                                                              id="total-order-value">${quantity * product.price}</span>
                                                    <span class="fs-5 text"
                                                          style="color:red; font-weight:bold">đ</span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-12">

                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <span id="order-single-validate-message" class="text-danger"></span>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                    <input type="submit" class="btn btn-primary" value="Đặt hàng"></input>
                </div>
            </div>
        </div>
    </form>
</div>
</body>
</html>
