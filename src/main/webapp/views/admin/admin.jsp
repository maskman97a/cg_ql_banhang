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

    <script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js"
            integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"
            integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.min.js"
            integrity="sha384-+sLIOodYLS7CIrQpBjl+C7nPvqq+FbNUBDunl/OZv93DB7Ln/533i8e/mZXLi/P+"
            crossorigin="anonymous"></script>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css"/>
    <script src="../../js/index.js"></script>
</head>
<body style="height: 100%;
    min-height: 100vh;
    display: flex;
    flex-direction: column;
    justify-content: space-between;">
<main>

    <div class="container-fluid">
        <div class="row text-white align-items-center" style="background-color: #102C57">
            <div class="col-2 p-3 row align-items-center">
                <div class="col-3">
                    <img src="${pageContext.request.contextPath}/images/logo.png" class="img-fluid rounded-circle "/>
                </div>
                <div class="col-9">
                    <div class="col-12">
                        <span class="fs-5">Trang quản trị</span>
                    </div>
                    <div class="col-12">
                        <span class="fs-5">Tạp hóa Anh Béo</span>
                    </div>
                </div>
            </div>
            <div class="col-10 p-3 row align-items-center">
                <span>Xin chào: <span style="color: green">${userInfo.fullName}</span>  - <a class=""
                                                                                             href="${pageContext.request.contextPath}/logout">Đăng xuất</a></span>
            </div>
        </div>
        <div class="row" style="background-color:#758694">
            <div class="col-2 p-3">
                <div class="col-12" style="border-bottom: 1px solid #202020">
                    <span class="fs-3"><b>Danh mục</b></span>
                </div>
                <div id="category-list" class="container-fluid">
                    <div class=" text-white">
                        <div class="col-12" style="border-bottom: 1px solid black">
                            <button class="btn" type="button"
                                    onclick="toggleMenu('div-product-manager-list')"
                                    aria-expanded="false"
                                    aria-controls="div-product-manager-list">
                                Sản phẩm
                            </button>
                        </div>
                        <div id="div-product-manager-list" class="collapse">
                            <div class="ps-3">
                                <div class="col-12" style="text-align: left; border-bottom: 1px solid black">
                                    <a href="${pageContext.request.contextPath}/admin/category" type="button"
                                       style="text-align:left"
                                       class="btn">Quản lý loại sản phẩm</a>
                                </div>
                                <div class="col-12" style="text-align: left; border-bottom: 1px solid black">
                                    <a href="${pageContext.request.contextPath}/admin/product" type="button"
                                       style="text-align:left"
                                       class="btn">Quản lý sản phẩm</a>
                                </div>
                                <div class="col-12" style="text-align: left; border-bottom: 1px solid black">
                                    <a href="${pageContext.request.contextPath}/admin/stock" type="button"
                                       style="text-align:left"
                                       class="btn">Quản lý tồn kho</a>
                                </div>
                            </div>
                        </div>
                        <div class="col-12" style="text-align: left; border-bottom: 1px solid black">
                            <button class="btn" type="button"
                                    onclick="toggleMenu('div-order-manager-list')"
                            <%--                                            onclick="toggleMenuV2('div-order-manager-list',this)"--%>
                                    aria-expanded="false"
                                    aria-controls="div-order-manager-list">
                                Đơn hàng
                            </button>
                        </div>
                        <div id="div-order-manager-list" class="collapse">
                            <div class="ps-3">
                                <div class="col-12" style="text-align: left; border-bottom: 1px solid black">
                                    <a href="${pageContext.request.contextPath}/admin/transaction" type="button"
                                       style="text-align:left"
                                       class="btn">Quản lý đơn hàng</a>
                                </div>
                            </div>
                        </div>

                        <script>
                            function toggleMenu(id) {
                                var submenu = document.getElementById(id);
                                // submenu.style.display = submenu.style.display === "none" ? "block" : "none";
                                submenu.style.display = (submenu.style.display === "none" || !submenu.style.display) ? "block" : "none";
                                if (submenu.style.display === "block") {
                                    $(submenu).slideUp(300).fadeOut(200);
                                } else {
                                    $(submenu).slideDown(300).fadeIn(200);
                                }
                            }

                            function toggleMenuV2(id, button) {
                                var submenu = document.getElementById(id);
                                $(submenu).slideToggle(300);

                                // Change background and icon color
                                $(button).toggleClass("active");

                                // Rotate the icon
                                $(button).find("i").toggleClass("rotate");
                            }
                        </script>
                    </div>
                </div>
            </div>
            <div class="col-10 pt-3"
                 style="background-color: #F7E7DC; min-height: calc(100vh - 150px)">
                <div class="col-12">
                    <a href="${pageContext.request.contextPath}/" class="link"><i class="fa-solid fa-house"></i>Trang
                        chủ</a>
                    <c:forEach var="urlLevel" items="${urlLevelList}">
                        >
                        <a class="link" href="${urlLevel.url}"> ${urlLevel.name}</a>
                    </c:forEach>
                </div>
                <div class=" pt-5">
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
                    <div ${!renderStock? 'hidden': ''}>
                        <c:import url="stock/stock-list.jsp"/>
                    </div
                </div>
            </div>
            <div class="col-2">

            </div>
        </div>
    </div>
</main>
<footer>
    <c:import url="/views/component/footer.jsp"/>
</footer>
<c:import url="/views/component/confirm-dialog-v2.jsp"/>
<style>
    .footer-div {
        background-color: #102C57;
        color: white
    }
</style>
</body>
</html>
