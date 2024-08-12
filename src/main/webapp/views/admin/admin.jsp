<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body style="height: 100%;
    min-height: 100vh;
    display: flex;
    flex-direction: column;
    justify-content: space-between;">
<main>
    <div class="container-fluid pt-3">
        <div class="row">
            <div class="col-2 bg-light">
                <div class="card card-body">
                    <div class="col-12">
                        <button class="btn" type="button" data-bs-toggle="collapse"
                                data-bs-target="#category-list" aria-expanded="false"
                                aria-controls="category-list">
                            Danh mục
                        </button>
                    </div>
                    <div id="category-list" class="collapse">
                        <div class="card card-body">
                            <div class="row">
                                <div class="col-12">
                                    <button class="btn" type="button"
                                            data-bs-toggle="collapse"
                                            data-bs-target="#div-product-manager-list" aria-expanded="false"
                                            aria-controls="div-product-manager-list">
                                        Sản phẩm
                                    </button>
                                </div>
                                <div id="div-product-manager-list" class="collapse">
                                    <div class="card card-body">
                                        <div class="row">
                                            <a href="${pageContext.request.contextPath}/admin/category" type="button"
                                               class="btn">Quản lý loại sản phẩm</a>
                                        </div>
                                        <div class="row text-left">
                                            <a href="${pageContext.request.contextPath}/admin/product" type="button"
                                               class="btn">Quản lý sản phẩm</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-12">
                                    <button class="btn" type="button"
                                            data-bs-toggle="collapse"
                                            data-bs-target="#div-order-manager-list" aria-expanded="false"
                                            aria-controls="div-order-manager-list">
                                        Đơn hàng
                                    </button>
                                </div>
                                <div id="div-order-manager-list" class="collapse">
                                    <div class="card card-body">
                                        <div class="col-12">
                                            <a href="${pageContext.request.contextPath}/admin/transaction" type="button"
                                               class="btn">Quản lý đơn hàng</a>
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>


            </div>
            <div class="col-8">
                <div class="col-12">
                    <c:forEach var="urlLevel" items="${urlLevelList}">
                        <c:if test="${urlLevel.level > 1}"> > </c:if>
                        <a class="" href="${urlLevel.url}">${urlLevel.name}</a>
                    </c:forEach>
                </div>
                <div class="pt-5">
                    <div ${!renderMainAdmin? 'hidden': ''}>
                        <h1>Chào mừng đến với Trang quản trị</h1>
                    </div>
                    <div ${!renderCategory? 'hidden': ''}>
                        <c:import url="category/category-list.jsp"/>
                    </div>
                    <div ${!renderProduct? 'hidden': ''}>
                        <c:import url="product/product-list.jsp"/>
                    </div>
                    <div ${!renderOrder? 'hidden': ''}>
                        <c:import url="transaction/transaction-list.jsp"/>
                    </div
                </div>
            </div>
            <div class=" col-2
                        ">

            </div>
        </div>
    </div>
</main>
<footer>
    <c:import url="/views/component/footer.jsp"/>
</footer>
</body>
</html>
