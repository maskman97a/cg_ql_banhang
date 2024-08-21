<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Tạp hóa anh béo</title>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-Fy6S3B9q64WdZWQUiU+q4/2Lc9npb8tCaSX9FK7E8HnRr0Jz8D6OP9dO5Vg3Q9ct"
            crossorigin="anonymous"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

    <script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js"
            integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"
            integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.min.js"
            integrity="sha384-+sLIOodYLS7CIrQpBjl+C7nPvqq+FbNUBDunl/OZv93DB7Ln/533i8e/mZXLi/P+"
            crossorigin="anonymous"></script>

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
<c:import url="/views/component/confirm-dialog.jsp"/>
</body>
<script>

</script>
</html>