<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
             class="img-fluid col-12  product-image"
             alt="${product.name}" width="100%" height="100%">
    </div>
    <div class="col-6 row">
        <div class="col-12 p-5">
            <h1 class="col-12">${product.productName}</h1>
            <input value="${product.price}" id="view-product-price-hid" hidden/>
            <h2 class="col-12 mb-3">
                <span id="view-product-price" style="color:red">${product.price}đ</span>
            </h2>

            <div class="col-12 mb-3">
                <div class="col-12 p-3 bg-body-secondary justify-content-center">
                    <label>MÔ TẢ SẢN PHẨM: </label>
                </div>
                <div class="col-12">
                    <textarea class="form-control rounded text-start" style="text-align: left!important;"
                              readonly>${product.description}
                    </textarea>
                </div>
            </div>
            <div class="col-12 mb-3">
                <span class="text-success">
                Số lượng tồn kho: ${product.availableQuantity}
            </span>
            </div>
            <input id="btn-order" type="button" class="btn btn-primary" value="Đặt hàng" data-toggle="modal"
                   data-target="#modalOrder" onclick="initOrderForm()"/>
            <input type="button" class="btn btn-secondary"
                   value="Thêm vào giỏ"
                   onclick="addToCart(${product.id}, 'product-image-${product.id}', ${product.availableQuantity})"/>
        </div>
        <div class="col-12">

        </div>
    </div>

    <c:import url="../order/modal-order.jsp"/>
</div>
<script>
    formatPriceDetailRender();

    function formatPriceDetailRender() {
        let productPrice = parseFloat(document.getElementById("view-product-price-hid").value);
        document.getElementById("view-product-price").innerHTML = formatNumber(productPrice) + "đ";
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
