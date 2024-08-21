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
<nav class="navbar navbar-nav navbar-nav-scroll navbar-custom container-fluid" style="position: fixed; padding:0">
    <div class="container-fluid nav-header">
        <div class="container">
            <div class="row">
                <div class="col-md-12 text-end m-1">
                    <a class="link" style="color:#860303 !important; text-decoration: none"
                       href="${pageContext.request.contextPath}/admin">Đăng
                        nhập</a>
                </div>
            </div>
        </div>
    </div>
    <div class="container-fluid nav-footer">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-4">
                    <a class="navbar-brand row p-3 align-items-center" href="${pageContext.request.contextPath}/">
                        <div class="col-3">
                            <img src="../../images/logo.png" alt="logo" class="img-fluid rounded-circle"/>
                        </div>
                        <div class="col-9">
                            <span class="fs-4">TẠP HÓA ANH BÉO</span>
                        </div>
                    </a>
                </div>

                <div class="col-5 ">
                    <form class="d-flex row  align-items-center" role="search"
                          action="${pageContext.request.contextPath}/product/search">
                        <div class="col-12">
                            <input class="form-control me-2 fs-4" type="search"
                                   placeholder="Nhập tên sản phẩm mà bạn muốn tìm..." aria-label="Search"
                                   value="${keyword}" name="keyword">
                        </div>
                    </form>
                </div>
                <div class="col-3 row align-items-center">
                    <div class="col-4">
                        <button id="btn-open-cart" data-toggle="modal" data-target="#modalCart" hidden></button>
                        <div onclick="openCart()">
                            <div class="row align-items-center navbar-text">
                                <div class="col-6 text-end">
                                    <div class="cart-icon">
                                        <i class="fa-solid fa-cart-shopping fs-3" id="cart-icon"></i>
                                        <span class="cart-count" id="count-cart">${cartCount}</span>
                                    </div>

                                </div>
                                <div class="col-6 text-start">
                                    <span>Giỏ hàng</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-8">
                        <a class="btn" href="${pageContext.request.contextPath}/order/lookup" data-toggle="tooltip"
                           data-placement="bottom" data-title="Tra cứu đơn hàng">
                            <div class="row align-items-center navbar-text">
                                <div class="col-5 text-end" style="padding-right:0">
                                    <i class="fa-solid fa-truck-fast fs-3"></i>
                                </div>
                                <div class="col-7 text-start">
                                    <span>Tra cứu đơn hàng</span>
                                </div>
                            </div>
                        </a>
                    </div>
                </div>
            </div>

        </div>
    </div>
</nav>
<c:import url="../product/product-cart-modal.jsp"/>
</body>
</html>
