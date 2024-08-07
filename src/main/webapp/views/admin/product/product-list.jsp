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
    <div class="row">
        <div class="col-2 bg-light">
            <div class="list-group">
                <a href="${pageContext.request.contextPath}/admin/product"
                   class="list-group-item list-group-item-action">Quản lý sản phẩm</a>
                <a href="${pageContext.request.contextPath}/admin/transaction"
                   class="list-group-item list-group-item-action">Quản lý đơn hàng</a>
            </div>
        </div>
        <div class="col-10">
            <div class="container">
                <div class="container form-control">
                    <div class="row">
                        <div class="col-12 text-center">
                            <h1>Danh sách sản phẩm</h1>
                        </div>
                        <div class="col-12 mb-3">
                            <a href="${pageContext.request.contextPath}/admin/product/product-create"
                               class="btn btn-primary">Thêm mới</a>
                        </div>
                        <div class="col-3  mb-3">

                        </div>
                        <div class="col-9 mb-3">
                            <form class="form row" method="get"
                                  action="${pageContext.request.contextPath}/admin/search">
                                <input type="text" class="form-control" name="size" value="10" hidden/>
                                <input type="text" class="form-control" name="page" value="1" hidden/>
                                <div class="col-9">
                                    <input type="text" class="form-control" placeholder="Mã/Tên sản phẩm" name="keyword"
                                           value="${keyword}">
                                </div>
                                <div class="col-3">
                                    <input type="submit" class="btn btn-success" value="Tìm"/>
                                </div>

                            </form>
                        </div>
                        <div class="col-2">
                        </div>
                        <div class="col-12">
                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <td>STT</td>
                                    <td>Ảnh</td>
                                    <td>Mã sản phẩm</td>
                                    <td>Tên sản phẩm</td>
                                    <td>Giá</td>
                                    <td>Số lượng</td>
                                    <td>Mô tả</td>
                                    <td>Action</td>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="product" items="${lstData}">
                                    <tr>
                                        <td>${product.index}</td>
                                        <td><img src="${pageContext.request.contextPath}/image/${product.imageUrl}"
                                                 id="product-image-${product.id}"
                                                 class="img-fluid col-12"
                                                 style="max-width: 50%;"
                                                 alt="${product.productName}" width="100%" height="100%">
                                        </td>
                                        <td>${product.productCode}</td>
                                        <td>${product.productName}</td>
                                        <td>${product.price}</td>
                                        <td>${product.quantity}</td>
                                        <td>${product.description}</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/admin/product/update?id=${product.id}">Sửa</a>
                                            |
                                            <a  class="btn-delete" onclick="return confirm('Bạn muốn xóa sản phẩm này')" href="${pageContext.request.contextPath}/admin/product/delete?id=${product.id}">Xóa</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                                <tfoot>
                                <tr>
                                    <td colspan="6" class="text-right">
                                        <div class="container row text-center col-12"
                                             style="position: fixed; bottom: 5px;">
                                            <nav aria-label="Page navigation example">
                                                <ul class="pagination" style="justify-content: center">
                                                    <c:if test="${!firstTab}">
                                                        <li class="page-item"><a class="page-link"
                                                                                 href="${pageContext.request.contextPath}/admin/search?page=${currentPage-1}&size=10">
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
                                    </td>
                                </tr>
                                </tfoot>
                            </table>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
