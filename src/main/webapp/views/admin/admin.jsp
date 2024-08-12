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
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"
            integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js"
            integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy"
            crossorigin="anonymous"></script>
</head>
<body style="height: 100%;
    min-height: 100vh;
    display: flex;
    flex-direction: column;
    justify-content: space-between;">
<main>
    <div class="container-fluid">
        <div class="row">
            <div class="col-2 bg-light">
                <div class="list-group">
                    <a href="${pageContext.request.contextPath}/admin/product"
                       class="list-group-item list-group-item-action">Quản lý sản phẩm</a>
                    <a href="${pageContext.request.contextPath}/admin/transaction"
                       class="list-group-item list-group-item-action">Quản lý đơn hàng</a>
                    <a href="${pageContext.request.contextPath}/admin/category"
                       class="list-group-item list-group-item-action">Quản lý thể loại</a>
                </div>
            </div>
            <div class="col-10" ${!renderProduct? 'hidden': ''}>
                <c:import url="product/product-list.jsp"/>
            </div>
            <div class="col-10" ${!renderCategory? 'hidden': ''}>
                <c:import url="category/category-list.jsp"/>
            </div>

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
