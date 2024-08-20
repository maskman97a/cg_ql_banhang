function validateLookupOrder() {
    let orderCode = document.getElementById("inp-order-code").value.trim()
    if (orderCode === '') {
        return renderErrorMessage("lookup-order-error-response", "Vui lòng nhập Mã đơn hàng hoặc Số điện thoại!");
    }
}


function setData(orderId, orderCode) {
    document.getElementById("inp-order-id").value = orderId;
    document.getElementById("inp-otp").value = '';
    document.getElementById("cancel-order-lb").innerHTML = orderCode;
}