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
</head>
<body>
<div class="modal modal-xl fade" tabindex="-1" id="modalOrder" data-bs-backdrop="static"
     data-bs-keyboard="false">
    <c:import url="../component/alert-modal.jsp"/>
    <form class="form needs-validation" action="${pageContext.request.contextPath}/order/create" method="post"
          onsubmit="return validateCreateOrder()">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">Thông tin đặt hàng</h4>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-6">
                            <h6>Thông tin khách hàng</h6>
                            <div class="col-12 card card-body row">
                                <div class="col-md-12">
                                    <label for="inp-customer-name" class="form-label">Họ và tên <span
                                            style="color: red">(*)</span></label>
                                    <input id="inp-customer-name" type="text" class="form-control"
                                           name="customer-name"/>
                                </div>
                                <div class="col-md-12">
                                    <label for="inp-customer-phone" class="form-label">Số điện thoại <span
                                            style="color: red">(*)</span></label>
                                    <input id="inp-customer-phone" type="text" class="form-control"
                                           name="customer-phone"/>
                                    <div class="invalid-feedback">
                                        Vui lòng nhập Số diện thoại
                                    </div>
                                </div>
                                <div class="col-md-12">
                                    <label for="inp-customer-email" class="form-label">Địa chỉ Email</label>
                                    <input id="inp-customer-email" type="text" class="form-control"
                                           name="customer-email"/>
                                    <div class="invalid-feedback">
                                        Vui lòng nhập Địa chỉ Email
                                    </div>
                                </div>
                                <div class="col-md-12">
                                    <label for="inp-customer-address" class="form-label">Địa chỉ nhận hàng <span
                                            style="color: red">(*)</span></label>
                                    <input id="inp-customer-address" type="text" class="form-control"
                                           name="customer-address"/>
                                    <div class="invalid-feedback">
                                        Vui lòng nhập Địa chỉ nhận hàng
                                    </div>
                                </div>
                            </div>
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
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                    <input type="submit" class="btn btn-primary" value="Đặt hàng"></input>
                </div>
            </div>
        </div>
    </form>
</div>
<script>
    function validateCreateOrder() {
        let customerName = document.getElementById("inp-customer-name").value;
        if (customerName === '') {
            window.alert("Vui lòng nhập Họ và tên")
            return false;
        }
        let customerPhone = document.getElementById("inp-customer-phone").value;
        if (customerPhone === '') {
            window.alert("Vui lòng nhập số điện thoại")
            return false;
        }
        if (customerPhone.length < 9 || customerPhone.length > 11) {
            window.alert("Số điện thoại không hợp lệ")
            return false;
        }
        let customerAddress = document.getElementById("inp-customer-address").value;
        if (customerAddress.length > 500 || customerAddress.length < 10) {
            window.alert("Vui lòng nhập địa chỉ hợp lệ")
            return false;
        }
        let customerEmail = document.getElementById("inp-customer-email").value;
        let quantity = document.getElementById("inp-quantity").value;
        if (quantity === 0) {
            window.alert("Số lượng không hợp lệ")
            return false;
        }
    }
</script>
</body>
</html>
