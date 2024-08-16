<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ceotungbeo
  Date: 12/8/24
  Time: 19:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div class="modal modal-xl fade" id="modalCart" tabindex="-1" aria-labelledby="modalCartLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="form-create-order-batch" class="form form-control p-2"
                  onsubmit="return validateSubmitCreateOrder()"
                  action="${pageContext.request.contextPath}/order/create-order-batch" method="post">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="exampleModalLabel">Giỏ hàng</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">

                <table class="table">
                    <thead>
                    <tr>
                        <th>STT</th>
                        <th>Tên sản phẩm</th>
                        <th>Số lượng</th>
                        <th>Đơn giá (VNĐ)</th>
                        <th>Thành tiền (VNĐ)</th>
                    </tr>
                    </thead>
                    <tbody id="tbody-table-cart">

                    </tbody>
                    <tfoot id="tfoot-table-cart">
                    </tfoot>
                </table>
                <span id="cart-empty-message"></span>
                <input type="hidden" id="cart-row-count" name="rowCount"/>
                <div class="col-12" id="cart-order-customer-info-input" hidden>
                    <c:import url="/views/order/order-customer-info-input.jsp"/>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Trở lại</button>
                <button type="submit"
                        class="btn btn-primary" id="btn-render-create-order">
                    Tiến hành đặt hàng
                </button>
            </div>
            </form>
        </div>
    </div>
</div>
</body>
<script>
    document.getElementById('form-create-order-batch').addEventListener('submit', function (event) {
        let rowCount = getQuantity()

        document.getElementById("cart-row-count").value = rowCount;
    });

    function validateSubmitCreateOrder() {
        if (document.getElementById("cart-order-customer-info-input").hidden) {
            renderCustomerInfo();
            return false;
        }
        return validateCreateOrder();
    }

    function renderCustomerInfo() {
        document.getElementById("cart-order-customer-info-input").hidden = false;
    }

    function drawProductList(listProductInCart) {
        let returnHtml = "";
        let totalAmount = 0;
        for (let i = 0; i < listProductInCart.length; i++) {
            let index = listProductInCart[i].index;
            let productId = listProductInCart[i].product.id;
            let productName = listProductInCart[i].product.productName;
            let productQuantity = listProductInCart[i].quantity;
            let productPrice = listProductInCart[i].product.price;
            let productAmount = listProductInCart[i].amount;
            totalAmount += productAmount;

            returnHtml += `<tr>`
            returnHtml += `<input type="hidden" name="inp-product-id-#index" value="#productId"/>`
            returnHtml += `<input type="number" id="inp-product-amount-#index" value="#productAmount" hidden/>`
            returnHtml += `<td>#index</td>`
            returnHtml += `<td>#productName</td>`
            returnHtml += `<td><input id="inp-quantity-#index" type="number" min="1" onchange="updateQuantity(#index , #productPrice, #productId )"
                        name="inp-quantity-#index" value="#productQuantity"></input></td>`
            returnHtml += `<td>` + formatNumber(productPrice) + `</td>`
            returnHtml += `<td id="col-amount-#index">` + formatNumber(productAmount) + `</td>`
            returnHtml += `<td><i class="fa-solid fa-trash" onclick="removeProductFromCart(#productId)"></i></td>`
            returnHtml += "</tr>"
            returnHtml = returnHtml.replaceAll("#index", index)
                .replaceAll("#productId", productId)
                .replaceAll("#productName", productName)
                .replaceAll("#productQuantity", productQuantity)
                .replaceAll("#productPrice", productPrice)
                .replaceAll("#productAmount", productAmount);
        }
        if (listProductInCart.length === 0) {
            document.getElementById("cart-empty-message").innerHTML = "Không có sản phẩm nào được chọn";
            document.getElementById("btn-render-create-order").disabled = true;
            document.getElementById("cart-order-customer-info-input").hidden = true;
        } else {
            document.getElementById("tfoot-table-cart").innerHTML =
                `<tr>
                      <td colspan='4' class="text-left" >Tổng</td>
                      <td id="total-amount" colspan='1' class="text-right" style="color:red">` + formatNumber(totalAmount) + `</td>
                 </tr>`
                + `<input id="total-quantity" type="number" value="#totalQuantity" hidden/>`.replace("#totalQuantity", listProductInCart.length);
            document.getElementById("cart-empty-message").innerHTML = "";
            document.getElementById("btn-render-create-order").disabled = false;
        }
        document.getElementById("tbody-table-cart").innerHTML = returnHtml;
    }

    async function removeProductFromCart(productId) {
        let apiUrl = contextPath + "/product/remove-from-cart?productId=" + productId;
        let data = await callApi(apiUrl, "GET", null);
        if (data.errorCode === 0) {
            let listProductInCart = data.additionalData.productList;
            document.getElementById("count-cart").innerHTML = data.additionalData.cartCount;
            drawProductList(listProductInCart);
            updateTotalAmount()
        } else {
            alert("Có lỗi khi load giỏ hàng, vui lòng thử lại sau");
        }
    }

    async function updateQuantity(index, price, productId) {
        let quantity = document.getElementById("inp-quantity-" + index).value
        let apiUrl = contextPath + "/product/update-cart?productId=" + productId + "&quantity=" + quantity;
        let data = await callApi(apiUrl, "GET", null);
        if (data.errorCode === 0) {
            let listProductInCart = data.additionalData.productList;
            document.getElementById("count-cart").innerHTML = data.additionalData.cartCount;
            drawProductList(listProductInCart);
            updateTotalAmount()
        } else {
            alert("Có lỗi khi load giỏ hàng, vui lòng thử lại sau");
        }
        updateAmount(index, price, quantity)
    }

    function updateAmount(index, price, quantity) {
        let newAmount = quantity * price;
        document.getElementById("col-amount-" + index).innerHTML = formatNumber(newAmount);
        document.getElementById("inp-product-amount-" + index).value = newAmount;
        updateTotalAmount();
    }

    function updateTotalAmount() {
        let totalAmount = 0;
        for (let index = 1; index <= getQuantity(); index++) {
            let productAmount = document.getElementById("inp-product-amount-" + index).value;
            totalAmount = totalAmount + parseInt(productAmount);
        }
        document.getElementById("total-amount").innerHTML = formatNumber(totalAmount);
    }

    function getQuantity() {
        var tbody = document.getElementById("tbody-table-cart");

        return tbody.getElementsByTagName("tr").length;
    }
</script>
</html>
