<%--
  Created by IntelliJ IDEA.
  User: ceotungbeo
  Date: 14/8/24
  Time: 21:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div id="order-customer-info">
    <h6>Thông tin khách hàng</h6>
    <div class="col-12 card card-body">
        <div class="col-md-12">
            <label for="inp-customer-name" class="form-label">Họ và tên <span
                    style="color: red">(*)</span></label>
            <input id="inp-customer-name" type="text" class="form-control"
                   name="customer-name"/>
        </div>
        <div class="col-md-12">
            <label for="inp-customer-phone" class="form-label">Số điện thoại <span
                    style="color: red">(*)</span></label>
            <input id="inp-customer-phone" type="text" class="form-control"
                   name="customer-phone"/>
        </div>
        <div class="col-md-12">
            <label for="inp-customer-email" class="form-label">Địa chỉ Email</label>
            <input id="inp-customer-email" type="text" class="form-control"
                   name="customer-email"/>
        </div>
        <div class="col-md-12">
            <label for="inp-customer-address" class="form-label">Địa chỉ nhận hàng <span
                    style="color: red">(*)</span></label>
            <input id="inp-customer-address" type="text" class="form-control"
                   name="customer-address"/>
        </div>
    </div>
</div>
</body>
</html>
