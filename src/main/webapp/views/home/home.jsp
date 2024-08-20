<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Tạp hóa anh béo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"/>
    <link href="https://getbootstrap.com/docs/5.3/assets/css/docs.css"
          rel="stylesheet"/>
    <title>Bootstrap Example</title>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="../../js/index.js"></script>
    <link href="../../css/style.css" rel="stylesheet"/>
    <script src="https://kit.fontawesome.com/7fc3852c80.js" crossorigin="anonymous"></script>
    <link rel="icon" type="image/png" href="../../images/logo.png">
</head>
<body style="height: 100%;
    min-height: 100vh;
    display: flex;
    flex-direction: column;
    justify-content: space-between;">
<header>
    <c:import url="../component/navbar.jsp"/>
</header>

<main style=" FLEX-GROW: 1; padding-top: 150px">
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
        <div id="div-order-result" class="container row" style="margin:0 auto"
             <c:if test="${!showOrderError}">hidden</c:if> >
            <c:import url="../order/order-error.jsp"/>
        </div>
        <div id="div-order-lookup" class="container row" style="margin:0 auto"
             <c:if test="${!showLookupOrder}">hidden</c:if> >
            <c:import url="../order/lookup-order.jsp"/>
        </div>
        <div id="div-login" class="container row" style="margin:0 auto"
             <c:if test="${!showLoginPage}">hidden</c:if> >
            <c:import url="../login/login.jsp"/>
        </div>
    </div>
</main>
<footer>
    <c:import url="../component/footer.jsp"/>
</footer>
</body>
<script>

</script>
</html>