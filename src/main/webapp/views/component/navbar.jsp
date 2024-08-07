<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ceotungbeo
  Date: 3/8/24
  Time: 07:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<nav class="navbar navbar-nav navbar-nav-scroll"
     style="overflow-x: auto;">
    <div class="row container">
        <div class="col-1">
            <a class="navbar-brand" href="/">
                <img src="../../images/logo.png" alt="logo" class="img-fluid rounded"/>
            </a>
        </div>
        <div class="col-2">
            <select class="form-control" name="category-id">
                <option value="0">--Chọn thể loại--</option>
                <c:forEach var="category" items="${lstCategory}" >
                    <option value="${category.id}">${category.name}</option>
                </c:forEach>
            </select>
        </div>
        <div class="col-9">
            <form class="d-flex" role="search" action="${pageContext.request.contextPath}/product/search">
                <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search">
                <button class="btn btn-outline-success" type="submit">Search</button>
            </form>
        </div>

    </div>
</nav>
</body>
</html>
