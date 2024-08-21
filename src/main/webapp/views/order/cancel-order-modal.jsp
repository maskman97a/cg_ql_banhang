<%--
  Created by IntelliJ IDEA.
  User: ceotungbeo
  Date: 21/8/24
  Time: 14:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div class="modal fade" id="otp-modal" tabindex="-1" aria-labelledby="otp-modal" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form class="form" action="${pageContext.request.contextPath}/order/cancel" method="post">
                <div class="modal-header">
                    <h1 class="modal-title fs-5">Hủy đơn hàng <span id="cancel-order-lb"></span>. Xác nhận OTP</h1>
                    <button type="button" class="btn-close" data-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <input id="inp-order-id" type="text" name="orderId" hidden/>
                    <input id="inp-otp" type="text" class="form-control" placeholder="Nhập OTP" value=""
                           name="otp"/>
                </div>
                <div class="modal-footer">
                    <span id="cancel-order-message" class="text-danger"></span>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary"
                            onclick="validateCancelOrder()">Xác nhận
                    </button>
                    <button type="button" data-target="#confirmDialog" data-toggle="modal"
                            class="btn btn-primary" id="btn-open-confirm-cancel-order-dialog"
                            hidden>1234
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
<script>
    function validateCancelOrder() {
        let otpInput = document.getElementById("inp-otp").value;
        if (otpInput.trim().length !== 6) {
            document.getElementById("cancel-order-message").innerHTML = "OTP không hợp lệ";
            return;
        }
        openConfirmDialog('btn-open-confirm-cancel-order-dialog', 'Bạn có chắc chắn muốn hủy đơn hàng?', 'inp-order-id')
    }
</script>
</body>
</html>
