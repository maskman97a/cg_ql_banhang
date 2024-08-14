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
<nav class="navbar navbar-nav navbar-nav-scroll navbar-custom container-fluid">
    <div class="container-fluid nav-header">
        <div class="container">
            <div class="row">
                <div class="col-md-12 text-end m-1">
                    <a class="link" style="color:#860303 !important" href="${pageContext.request.contextPath}/admin">Đăng
                        nhập Quản trị</a>
                </div>
            </div>
        </div>
    </div>
    <div class="container-fluid nav-footer">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-1">
                    <a class="navbar-brand" href="/">
                        <img src="../../images/logo.png" alt="logo" class="img-fluid rounded"/>
                    </a>
                </div>

                <div class="col-10">
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
                <div class="col-1">
                    <button id="btn-open-cart" data-bs-toggle="modal" data-bs-target="#modalCart" hidden></button>
                    <div class="cart-icon" onclick="openCart()">
                        <i class="fa-solid fa-cart-shopping fs-3"></i>
                        <span class="cart-count" id="count-cart">${cartCount}</span>
                    </div>
                </div>
            </div>

        </div>
    </div>
</nav>
<c:import url="../product/product-cart-modal.jsp"/>
<script>

</script>
</body>
</html>
