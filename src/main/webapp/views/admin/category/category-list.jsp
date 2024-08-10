<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Danh sách thể loại</title>
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
                <a href="${pageContext.request.contextPath}/admin/category"
                   class="list-group-item list-group-item-action">Quản lý thể loại</a>
            </div>
        </div>
        <div class="col-10">
            <div class="container">
                <div class="container form-control">
                    <div class="row">
                        <div class="col-12 text-center">
                            <h1>Danh sách thể loại</h1>
                        </div>

                        <div class="col-3  mb-3">

                        </div>
                        <div class="col-9 mb-3">
                            <form class="form row" method="get"
                                  action="${pageContext.request.contextPath}/admin/category/search">
                                <input type="text" class="form-control" name="size" value="5" hidden/>
                                <input type="text" class="form-control" name="page" value="1" hidden/>
                                <div class="col-7">
                                    <input type="text" class="form-control" placeholder="Tên thể loại" name="keyword"
                                           value="${keyword}">
                                </div>
                                <div class="col-3">
                                    <input type="submit" class="btn btn-success" value="Tìm"/>
                                </div>

                            </form>
                        </div>
                        <div class="col-2">
                        </div>
                        <div class="col-12 mb-3">
                            <a href="${pageContext.request.contextPath}/admin/category/category-create"
                               class="btn btn-primary">Thêm mới</a>
                        </div>
                        <div class="col-12">
                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <td>STT</td>
                                    <td>Tên thể loại</td>
                                    <td>Action</td>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="product" items="${lstCategory}">
                                    <tr>
                                        <td>${product.index}</td>
                                        <td>${product.name}</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/admin/category/update?id=${product.id}">Sửa</a>
                                            |
                                            <a class="btn-delete" onclick="return confirm('Bạn muốn xóa thể loại này')"
                                               href="${pageContext.request.contextPath}/admin/category/delete?id=${product.id}">Xóa</a>
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
                                                                 href="${pageContext.request.contextPath}/admin/category/search?page=${currentPage-1}&size=5">
                                            Previous</a></li>
                                    </c:if>

                                    <c:forEach begin="${beginPage}" end="${endPage}" var="page">
                                        <li class="page-item ${currentPage == page ? 'active' : ''}">
                                            <a class="page-link"
                                               href="${pageContext.request.contextPath}/admin/category/search?page=${page}&size=5">${page}</a>
                                        </li>
                                    </c:forEach>
                                    <c:if test="${!lastTab}">
                                        <li class="page-item">
                                            <a class="page-link"
                                               href="${pageContext.request.contextPath}/admin/category/search?page=${currentPage+1}&size=5">
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
</div>
</body>
</html>
