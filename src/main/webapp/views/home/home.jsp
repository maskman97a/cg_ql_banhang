<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý bán hàng</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"
            integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js"
            integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy"
            crossorigin="anonymous"></script>
    <script src="../../js/index.js"></script>
    <link href="../../css/index.css" rel="stylesheet"/>
    <script src="https://kit.fontawesome.com/7fc3852c80.js" crossorigin="anonymous"></script>
</head>
<body style="height: 100%;
    min-height: 100vh;
    display: flex;
    flex-direction: column;
    justify-content: space-between;">
<header>
    <div class="container-fluid"
         style=" position: fixed; top: 0; background-color: #e3f2fd; z-index: 10; padding-top:25px">
        <c:import url="../component/navbar.jsp"/>
    </div>
</header>

<main style=" FLEX-GROW: 1; padding-top: 150px">
    <i class="fa-solid fa-cart-shopping"></i>
    <div class="container-fluid" style="display: flex">
        <c:import url="../component/alert.jsp"/>
        <div id="div-product-show" class="container row" style="margin:0 auto"
             <c:if test="${!showListProduct}">hidden</c:if> >
            <c:import url="../product/product-show.jsp"/>
        </div>
        <div id="div-product-detail" class="container row" style="margin:0 auto"
             <c:if test="${!showProductDetail}">hidden</c:if> >
            <c:import url="../product/product-detail.jsp"/>
        </div>
        <div id="div-order-result" class="container row" style="margin:0 auto"
             <c:if test="${!showOrderSuccess}">hidden</c:if> >
            <c:import url="../order/order-success.jsp"/>
        </div>
        <div id="div-order-lookup" class="container row" style="margin:0 auto"
             <c:if test="${!showLookupOrder}">hidden</c:if> >
            <c:import url="../order/lookup-order.jsp"/>
        </div>
    </div>
</main>
<footer>
    <div class="container-fluid" style=" background-color: #e3f2fd">
        <c:import url="../component/footer.jsp"/>
    </div>
</footer>
</footer>
</body>
</html>