function validateLookupOrder() {
    let orderCode = document.getElementById("inp-order-code").value.trim()
    if (orderCode === '') {
        return renderErrorMessage("lookup-order-error-response", "Vui lòng nhập Mã đơn hàng!");
    }
    let phoneNumber = document.getElementById("inp-phone-number").value.trim()
    if (phoneNumber === '') {
        return renderErrorMessage("lookup-order-error-response", "Vui lòng nhập Số điện thoại đặt hàng!");
    }
}


async function prepareCancelOrder(orderId, orderCode) {
    if (await sendCancelOrderOtp(orderCode)) {
        setData(orderId, orderCode);
        document.getElementById("btn-open-dialog-cancel-order-otp").click();
        document.getElementById("btn-resend-otp").onclick = function () {
            sendCancelOrderOtp(orderCode);
        }
    }
}

async function sendCancelOrderOtp(orderCode) {
    let apiUtl = contextPath + "/order/prepare-cancel";
    const body = new URLSearchParams({
        orderCode: orderCode
    });

    let data = await callApi(apiUtl, "POST", body.toString(), "application/x-www-form-urlencoded")
    if (data.errorCode === 0) {
        return true
    }
}

function setData(orderId, orderCode) {
    document.getElementById("inp-order-id").value = orderId;
    document.getElementById("inp-otp").value = '';
    document.getElementById("cancel-order-lb").innerHTML = orderCode;
}