<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Danh sách sản phẩm</title>
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
<body>
<div class="container-fluid">
    <div class="row"  ${!renderProductList ? 'hidden': ''}>
        <div class="col-12">
            <div class="container">
                <div class="container form-control">
                    <div class="row">
                        <div class="col-12 text-center">
                            <h1>Danh sách sản phẩm</h1>
                        </div>

                        <div class="col-12 mb-3">
                            <form class="form row justify-content-center" method="get"
                                  action="${pageContext.request.contextPath}/admin/product/search">
                                <input type="text" class="form-control" name="size" value="10" hidden/>
                                <input type="text" class="form-control" name="page" value="1" hidden/>
                                <div class="col-2">
                                    <select class="form-control" name="category-id">
                                        <option value="0">--Chọn thể loại--</option>
                                        <c:forEach var="category" items="${lstCategory}">
                                            <option value="${category.id}">${category.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-7">
                                    <input type="text" class="form-control" placeholder="Mã/Tên sản phẩm" name="keyword"
                                           value="${keyword}">
                                </div>
                                <div class="col-1">
                                    <input type="submit" class="btn btn-success" value="Tìm"/>
                                </div>

                            </form>
                        </div>
                        <div class="col-2">
                        </div>
                        <div class="col-12 mb-3">
                            <a href="${pageContext.request.contextPath}/admin/product/product-create"
                               class="btn btn-primary">Thêm mới</a>
                        </div>
                        <div class="col-12">
                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th>STT</th>
                                    <th>Mã sản phẩm</th>
                                    <th>Tên sản phẩm</th>
                                    <th>Thể loại</th>
                                    <th>Giá</th>
                                    <th>Số lượng</th>
                                    <th>Mô tả</th>
                                    <th>Action</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="product" items="${lstData}">
                                    <tr>
                                        <td>${product.index}</td>
                                        <td>${product.productCode}</td>
                                        <td>${product.productName}</td>
                                        <td>${product.categoryName}</td>
                                        <td>${product.price}</td>
                                        <td>${product.quantity}</td>
                                        <td>${product.description}</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/admin/product/update?id=${product.id}">Sửa</a>
                                            |
                                            <a class="btn-delete" onclick="return confirm('Bạn muốn xóa sản phẩm này')"
                                               href="${pageContext.request.contextPath}/admin/product/delete?id=${product.id}">Xóa</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        <div class="text-center col-12">
                            <nav aria-label="Page navigation example">
                                <ul class="pagination" style="justify-content: center">
                                    <c:if test="${!firstTab}">
                                        <li class="page-item"><a class="page-link"
                                                                 href="${pageContext.request.contextPath}/admin/search?page=${currentPage-1}&size=5">
                                            Previous</a></li>
                                    </c:if>

                                    <c:forEach begin="${beginPage}" end="${endPage}" var="page">
                                        <li class="page-item ${currentPage == page ? 'active' : ''}">
                                            <a class="page-link"
                                               href="${pageContext.request.contextPath}/admin/search?page=${page}&size=5">${page}</a>
                                        </li>
                                    </c:forEach>
                                    <c:if test="${!lastTab}">
                                        <li class="page-item">
                                            <a class="page-link"
                                               href="${pageContext.request.contextPath}/admin/search?page=${currentPage+1}&size=5">
                                                Next</a></li>
                                    </c:if>
                                </ul>
                            </nav>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row" ${!renderProductCreate ? 'hidden': ''}>
        <c:import url="/views/admin/product/product-create.jsp"/>
    </div>
    <div class="row" ${!renderProductUpdate ? 'hidden': ''}>
        <c:import url="/views/admin/product/product-update.jsp"/>
    </div>
</div>
</body>
</html>
