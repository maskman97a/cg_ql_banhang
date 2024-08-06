<%--
  Created by IntelliJ IDEA.
  User: ceotungbeo
  Date: 5/8/24
  Time: 19:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div class="container-fluid row">
    <div class="col-6 shadow p-3 mb-5 bg-body-tertiary rounded">
        <img src="${pageContext.request.contextPath}/image/${product.imageUrl}"
             id="product-image-${product.id}"
             class="img-fluid col-12"
             alt="${product.name}" width="100%" height="100%">
    </div>
    <div class="col-6 row">
        <div class="col-12">
            <h1 class="col-12">${product.productName}</h1>
            <input value="${product.price}" id="view-product-price-hid" hidden/>
            <h2 class="col-12"><span id="view-product-price">${product.price}</span>đ</h2>
            <input type="button" class="btn btn-primary" value="Đặt hàng" data-bs-toggle="modal"
                   data-bs-target="#modalOrder" onclick="initOrderForm()"/>
        </div>
        <div class="col-12">

        </div>
    </div>

    <div class="modal modal-xl fade" tabindex="-1" id="modalOrder" data-bs-backdrop="static"
         data-bs-keyboard="false">
        <form class="form needs-validation" action="${pageContext.request.contextPath}/order/create" method="post">
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
                                        <label for="inp-customer-name" class="form-label">Họ và tên</label>
                                        <input id="inp-customer-name" type="text" class="form-control"
                                               name="customer-name"
                                               required/>
                                        <div class="invalid-feedback">
                                            Vui lòng nhập đầy đủ Họ và tên
                                        </div>
                                    </div>
                                    <div class="col-md-12">
                                        <label for="inp-customer-phone" class="form-label">Số điện thoại</label>
                                        <input id="inp-customer-phone" type="text" class="form-control"
                                               name="customer-phone"
                                               required/>
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
                                        <label for="inp-customer-address" class="form-label">Địa chỉ nhận hàng</label>
                                        <input id="inp-customer-address" type="text" class="form-control"
                                               name="customer-address"
                                               required/>
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
                                                <span class="col-12 form-label">Mã sản phẩm: ${product.productCode}</span>
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

</div>
<script>
    formatPriceDetailRender();

    function formatPriceDetailRender() {
        let productPrice = parseFloat(document.getElementById("view-product-price-hid").value);
        document.getElementById("view-product-price").innerHTML = formatNumber(productPrice);
    }


    function formatPrice() {
        let productPrice = parseFloat(document.getElementById("inp-product-price").value);
        document.getElementById("out-product-price").innerHTML = formatNumber(productPrice);
    }


    function initOrderForm() {
        formatPrice();
        document.getElementById('inp-quantity').value = 1
        changeProductQuantity();
    }

    function changeProductQuantity() {
        let quantityInput = document.getElementById('inp-quantity');
        let price = parseFloat(document.getElementById('inp-product-price').value);

        let quantity = parseInt(quantityInput.value);
        let totalPrice = price * quantity;

        let totalPriceOutput = document.getElementById('total-order-value');
        totalPriceOutput.innerHTML = formatNumber(totalPrice);
    }
</script>
</body>
</html>
