<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div class="container">
    <form class="form" method="post"
          action="${pageContext.request.contextPath}/admin/transaction/detail">
        <div class="col-12 mb-3">
            <span>${lookupResponse}</span>
            <h2 style="color:red">${errorResponse}</h2>
            <h2 style="color:green">${successResponse}</h2>
        </div>
        <div class="col-12 row card card-body mb-3">
            <div class="col-md-12 mb-3">
                <h6>Thông tin khách hàng</h6>
                <div class="col-12 card card-body row">
                    <span class="row">Tên khách hàng: ${customerInfo.name}</span>
                    <span class="row">Số điện thoại: ${customerInfo.phone}</span>
                    <span class="row">Địa chỉ Nhận hàng: ${customerInfo.address}</span>
                </div>
            </div>

            <c:forEach var="orderInfo" items="${orderList}">
                <div class="card card-body row">
                    <div class="col-md-12 mb-3">
                        <h6>Thông tin đơn hàng ${orderInfo.code}</h6>
                        <div class="col-12 card card-body row">
                            <span class="row">Mã đơn hàng: ${orderInfo.code}</span>
                            <span class="row">Ngày đặt hàng: ${orderInfo.orderDateStr}</span>
                            <span id="span-total-amount-${orderInfo.id}"
                                  class="row float-end">Tổng tiền: ${orderInfo.totalAmount}đ</span>
                            <span class="row">Trạng thái: ${orderInfo.orderStatusName}</span>
                            <span class="row">Địa chỉ nhận hàng: ${orderInfo.address}</span>
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
                                <c:forEach var="orderDetail" items="${orderInfo.orderDetailList}">
                                    <tr>
                                        <td>${orderDetail.product.productName}</td>
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
                        <a href="${pageContext.request.contextPath}/admin/transaction" class="btn btn-secondary">Trở về</a>
                    </div>
                </div>
                <script>
                    document.getElementById("col-total-amount-" + ${orderInfo.id}).innerHTML = formatNumber(${orderInfo.totalAmount}) + "đ";
                    document.getElementById("span-total-amount-" + ${orderInfo.id}).innerHTML = "Tổng tiền: " + formatNumber(${orderInfo.totalAmount}) + "đ";
                </script>
            </c:forEach>
        </div>
    </form>
</div>
</body>
</html>
