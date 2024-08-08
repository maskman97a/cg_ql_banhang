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
    <form class="form row" method="get" action="${pageContext.request.contextPath}/order/lookup-by-code"
          oninvalid="validateLookupOrder()">
        <div class="col-md-8">
            <label class="" for="inp-order-code">Mã đơn hàng:</label>
            <input id="inp-order-code" class="form-control" type="text" name="orderCode">
        </div>
        <div class="col-md-4">
            <button type="submit" class="btn btn-primary">Tìm kiếm</button>
        </div>
    </form>
    <div class="col-12">
        <span>${lookupResponse}</span>
    </div>
</div>
<script>
    function validateLookupOrder() {
        let orderCode = document.getElementById("inp-order-code").value
        if (orderCode === '') {
            window.alert("Vui lòng nhập Mã đơn hàng");
        }
    }
</script>
</body>
</html>
