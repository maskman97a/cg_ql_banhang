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
    <script src="../../js/order/lookup-order.js"></script>
</head>
<body>
<div class="row" id="lookup-order-div">

    <form class="form row align-items-end p-3 mb-3 bg-body-secondary rounded" method="get"
          action="${pageContext.request.contextPath}/order/lookup-by-code"
          onsubmit="return validateLookupOrder()">
        <h1>Tra cứu đơn hàng</h1>
        <div class="col-md-8">
            <label class="" for="inp-order-code">Mã đơn hàng/Số điện thoại:</label>
            <input id="inp-order-code" class="form-control" type="text" name="orderCode" value="${orderCode}"
                   placeholder="Nhập Mã đơn hàng hoặc Số điện thoại">
        </div>
        <div class="col-md-4">
            <button type="submit" class="btn btn-primary"><i class="fa-solid fa-magnifying-glass"></i> Tra cứu</button>
        </div>
        <div class="col-12 mb-3">
            <span>${lookupResponse}</span>
            <span class="text-danger" id="lookup-order-error-response">${errorResponse}</span>
            <h2 style="color:green">${successResponse}</h2>
        </div>
    </form>

    <div class="col-12 row card card-body mb-3" ${!showOrderInfo ? 'hidden' : ''}>
        <div class="col-md-12 mb-3">
            <h6>Thông tin khách hàng</h6>
            <div class="col-12 card card-body row">
                <span class="row">Tên khách hàng: ${customerInfo.name}</span>
                <span class="row">Số điện thoại: ${customerInfo.phone}</span>
                <span class="row">Email: ${customerInfo.email}</span>
                <span class="row">Địa chỉ khách hàng (Đăng ký lần đặt hàng đầu tiên): ${customerInfo.address}</span>
            </div>
        </div>

        <c:forEach var="orderInfo" items="${orderList}">
            <div class="card card-body">
                <div class="col-md-12 mb-3">
                    <h6>Thông tin đơn hàng ${orderInfo.code}</h6>
                    <div class="col-12 card card-body">
                        <span class="col-12">Mã đơn hàng: ${orderInfo.code}</span>
                        <span class="col-12">Ngày đặt hàng: ${orderInfo.orderDateStr}</span>
                        <span id="span-total-amount-${orderInfo.id}"
                              class="col-12 float-end">Tổng tiền: ${orderInfo.totalAmount}đ</span>
                        <span class="col-12">Trạng thái: ${orderInfo.orderStatusName}</span>
                        <span class="col-12">Địa chỉ nhận hàng: ${orderInfo.address}</span>
                    </div>
                </div>

                <div class="col-md-12 mb-3">
                    <h6>Danh sách sản phẩm</h6>
                    <div class="col-12 card card-body">
                        <table class="table">
                            <thead style="font-weight: bold">
                            <tr>
                                <td>Tên Sản phẩm</td>
                                <td>Đơn giá</td>
                                <td>Số lượng</td>
                                <td>Thành tiền</td>
                            </tr>
                            </thead>
                            <c:forEach var="orderDetail" items="${orderInfo.orderDetailEntityList}">
                                <tr>
                                    <td>${orderDetail.productEntity.productName}</td>
                                    <td id="col-unit-price-${orderDetail.id}">${orderDetail.unitPrice}</td>
                                    <td>${orderDetail.quantity}</td>
                                    <td id="col-amount-${orderDetail.id}">${orderDetail.amount}</td>
                                </tr>
                                <script>
                                    document.getElementById("col-unit-price-" + ${orderDetail.id}).innerHTML = formatNumber(${orderDetail.unitPrice}) + "đ";
                                    document.getElementById("col-amount-" + ${orderDetail.id}).innerHTML = formatNumber(${orderDetail.amount}) + "đ";
                                </script>
                            </c:forEach>
                            <tfoot>
                            <tr>
                                <td>Tổng</td>
                                <td></td>
                                <td></td>
                                <td id="col-total-amount-${orderInfo.id}"
                                    style="color:red; font-weight: bold">${orderInfo.totalAmount}</td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
                <div class="col-md-12 mb-3 text-center">
                    <button type="button" class="btn btn-danger"
                        ${orderInfo.status == 0 ? '': 'disabled'}
                            onclick="prepareCancelOrder( ${orderInfo.id}, '${orderInfo.code}')">
                        Hủy đơn hàng
                    </button>
                    <button id="btn-open-dialog-cancel-order-otp" type="button" data-toggle="modal"
                            data-target="#otp-modal" hidden>

                    </button>
                        <%--                    <a href="${pageContext.request.contextPath}/home" class="btn btn-secondary">Trở về</a>--%>
                </div>
            </div>
            <script>
                document.getElementById("col-total-amount-" + ${orderInfo.id}).innerHTML = formatNumber(${orderInfo.totalAmount}) + "đ";
                document.getElementById("span-total-amount-" + ${orderInfo.id}).innerHTML = "Tổng tiền: " + formatNumber(${orderInfo.totalAmount}) + "đ";
            </script>
        </c:forEach>
    </div>
    <c:import url="/views/order/cancel-order-modal.jsp"/>
</div>


</body>
</html>
