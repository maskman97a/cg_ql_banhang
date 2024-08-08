<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ceotungbeo
  Date: 8/8/24
  Time: 15:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div class="row">
    <form class="form row align-items-end mb-3" method="get"
          action="${pageContext.request.contextPath}/order/lookup-by-code"
          oninvalid="validateLookupOrder()">
        <div class="col-md-8">
            <label class="" for="inp-order-code">Mã đơn hàng:</label>
            <input id="inp-order-code" class="form-control" type="text" name="orderCode" value="${orderCode}">
        </div>
        <div class="col-md-4">
            <button type="submit" class="btn btn-primary">Tra cứu</button>
        </div>
    </form>
    <div class="col-12 mb-3">
        <span>${lookupResponse}</span>
        <h2 style="color:red">${errorResponse}</h2>
        <h2 style="color:green">${successResponse}</h2>
    </div>
    <div class="col-12 row card card-body mb-3" ${!showOrderInfo ? 'hidden' : ''}>
        <div class="col-md-12 mb-3">
            <h6>Thông tin đơn hàng</h6>
            <div class="col-12 card card-body row">
                <span class="row">Mã đơn hàng: ${orderInfo.code}</span>
                <span class="row">Ngày đặt hàng: ${orderInfo.orderDateStr}</span>
                <span id="span-total-amount" class="row float-end">Tổng tiền: ${orderInfo.totalAmount}đ</span>
                <span class="row float-end">Trạng thái: ${orderInfo.orderStatusName}</span>
            </div>
        </div>

        <div class="col-md-12 mb-3">
            <h6>Thông tin khách hàng</h6>
            <div class="col-12 card card-body row">
                <span class="row">Tên khách hàng: ${customerInfo.name}</span>
                <span class="row">Số điện thoại: ${customerInfo.phone}</span>
                <span class="row">Địa chỉ Nhận hàng: ${orderInfo.address}</span>
            </div>
        </div>

        <div class="col-md-12 mb-3">
            <h6>Chi tiết đơn hàng</h6>
            <div class="col-12 card card-body row">
                <table class="table">
                    <thead style="font-weight: bold">
                    <tr>
                        <td>Sản phẩm</td>
                        <td>Đơn giá</td>
                        <td>Số lượng</td>
                        <td>Thành tiền</td>
                    </tr>
                    </thead>
                    <c:forEach var="orderDetail" items="${orderDetailInfo}">
                        <tr>
                            <td>${orderDetail.product.productName}</td>
                            <td id="col-unit-price-${orderDetail.index}">${orderDetail.unitPrice}</td>
                            <td>${orderDetail.quantity}</td>
                            <td id="col-amount-${orderDetail.index}">${orderDetail.amount}</td>
                        </tr>
                        <script>
                            document.getElementById("col-unit-price-" + ${orderDetail.index}).innerHTML = formatNumber(${orderDetail.unitPrice}) + "đ";
                            document.getElementById("col-amount-" + ${orderDetail.index}).innerHTML = formatNumber(${orderDetail.amount}) + "đ";
                        </script>
                    </c:forEach>
                    <tfoot>
                    <tr>
                        <td>Tổng</td>
                        <td></td>
                        <td></td>
                        <td id="col-total-amount" style="color:red; font-weight: bold">${orderInfo.totalAmount}</td>
                    </tr>
                    </tfoot>
                </table>
            </div>
        </div>
        <div class="col-md-12 mb-3 text-center">
            <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#otp-modal"
            ${orderInfo.status == 2 ? 'disabled': ''}>
                Hủy đơn hàng
            </button>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-secondary">Trở về</a>
        </div>
    </div>
    <div class="modal fade" id="otp-modal" tabindex="-1" aria-labelledby="otp-modal" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <form class="form" action="${pageContext.request.contextPath}/order/cancel" method="post">
                    <div class="modal-header">
                        <h1 class="modal-title fs-5" id="exampleModalLabel">Xác nhận OTP</h1>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <input type="text" value="${orderInfo.id}" name="orderId" hidden/>
                        <input type="text" class="form-control" placeholder="Nhập OTP" value="" name="otp"/>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary">Xác nhận</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    function validateLookupOrder() {
        let orderCode = document.getElementById("inp-order-code").value
        if (orderCode === '') {
            window.alert("Vui lòng nhập Mã đơn hàng");
        }
    }

    document.getElementById("col-total-amount").innerHTML = formatNumber(${orderInfo.totalAmount}) + "đ";
    document.getElementById("span-total-amount").innerHTML = "Tổng tiền: " + formatNumber(${orderInfo.totalAmount}) + "đ";
</script>
</body>
</html>
