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

        <div class="col-11">
            <form class="d-flex row" role="search"
                  action="${pageContext.request.contextPath}/product/search">
                <div class="col-2">
                    <input type="text" hidden name="sortType" value="${sortType}"/>
                    <input type="text" hidden name="sortCol" value="${sortCol}"/>
                    <input type="text" hidden name="page" value="${page}"/>
                    <input type="text" hidden name="size" value="${size}"/>

                    <select class="form-control" name="categoryId">
                        <option value="">--Chọn thể loại--</option>
                        <c:forEach var="category" items="${lstCategory}">
                            <option value="${category.id}" ${selectedCategoryId == category.id ? 'selected':''} >${category.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-8">
                    <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search"
                           value="${keyword}">
                </div>
                <div class="col-2">
                <button class="btn btn-outline-success" type="submit">Search</button>
                </div>
            </form>
        </div>

    </div>
    <div style="position: fixed; top:10px; right: 10px;">
        <a class="link" href="${pageContext.request.contextPath}/admin">Đăng nhập Quản trị</a>
    </div>
    <div style="position: fixed; bottom:100px; right: 20px;">
        <a class="" href="${pageContext.request.contextPath}/order/lookup">
            <img id="icon-lookup-order" class="rounded-circle" src="../../images/icon-lookup.png" width="50px" height="50px"/>
        </a>
    </div>
</nav>
</body>
</html>
