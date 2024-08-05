<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ceotungbeo
  Date: 3/8/24
  Time: 18:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div class="row">
    <c:forEach var="product" items="${lstProduct}">
        <div class="col-3 p-3">
            <div class="shadow p-3 mb-5 bg-body-tertiary rounded">
                <a href="${pageContext.request.contextPath}/product/detail?id=${product.id}" class="row">
                    <img src="${product.imageUrl}" class="img-fluid col-12" alt="${product.name}">
                    <span class="col-12" style="font-weight: bold">${product.name}</span>

                    <span id="col-price-${product.id}" class="col-12 formatted-number"
                          style="font-weight: bold; color:red;">
                <script>
                    document.getElementById("col-price-${product.id}").innerHTML = formatNumber(${product.price})
                </script>
                đ</span>
                </a>
            </div>
        </div>
    </c:forEach>
</div>
</body>
</html>
