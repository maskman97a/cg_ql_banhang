function validateCreateOrder(orderTyp) {
    let partnerId = "";
    let messageElementId;
    if (orderTyp === "batch") {
        partnerId = "#cart-order-customer-info-input";
        messageElementId = "order-validate-message";
    } else {
        partnerId = "#modalOrder";
        messageElementId = "order-single-validate-message";

    }

    let customerName = document.querySelector(partnerId + " #inp-customer-name").value;
    if (customerName === '') {
        return renderErrorMessage(messageElementId, "Vui lòng nhập Họ và tên!")
    }
    let customerPhone = document.querySelector(partnerId + " #inp-customer-phone").value;
    if (customerPhone === '') {
        return renderErrorMessage(messageElementId, "Vui lòng nhập Số điện thoại!")
    }
    if (customerPhone.length < 9 || customerPhone.length > 11) {
        return renderErrorMessage(messageElementId, "Vui lòng nhập Số điện thoại hợp lệ!")
    }
    let customerAddress = document.querySelector(partnerId + " #inp-customer-address").value;
    if (customerAddress.length > 500 || customerAddress.length < 10) {
        return renderErrorMessage(messageElementId, "Vui lòng nhập Địa chỉ hợp lệ!")
    }
    let customerEmail = document.querySelector(partnerId + " #inp-customer-email").value;
    if (!customerEmail.contains("@")) {
        return renderErrorMessage(messageElementId, "Vui lòng nhập Địa chỉ email hợp lệ!")
    }
    let quantity = document.getElementById("inp-quantity").value;
    if (quantity === 0) {
        return renderErrorMessage(messageElementId, "Số lượng không hợp lệ!")
    }
}

document.getElementById('form-create-order-batch').addEventListener('submit', function (event) {
    let rowCount = getQuantity()

    document.getElementById("cart-row-count").value = rowCount;
});

function validateSubmitCreateOrder() {
    if (document.getElementById("cart-order-customer-info-input").hidden) {
        renderCustomerInfo();
        return false;
    }
    return validateCreateOrder('batch');
}

function renderCustomerInfo() {
    document.getElementById("cart-order-customer-info-input").hidden = false;
}

function drawProductList(listProductInCart) {
    let returnHtml = "";
    let totalAmount = 0;
    for (let i = 0; i < listProductInCart.length; i++) {
        console.log(listProductInCart[i].product.productName);
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
        returnHtml += `<td><span>#productName</span></td>`
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
    console.log(returnHtml);
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