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
    <style>
        body {
            background-color: #f8f9fa;
        }
        .card {
            border: none;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        .card-header {
            background-color: #e3b942;
            color: #ffffff;
            border-radius: 10px 10px 0 0;
        }
        .table-hover tbody tr:hover {
            background-color: #f1f1f1;
        }
        .page-link {
            color: #343a40;
        }
        .page-item.active .page-link {
            background-color: #343a40;
            border-color: #343a40;
        }
        .btn-delete {
            color: #dc3545;
        }
        .btn-delete:hover {
            color: #c82333;
        }
        .form-control, .btn {
            border-radius: 5px;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="card" ${!renderCategoryList ? 'hidden': ''} >
        <div class="card-header text-center">
            <h1>Quản lý Loại sản phẩm</h1>
        </div>
        <div class="card-body">
            <div class="col-12 mb-3">
                <% if (request.getAttribute("errorMsg") != null) { %>
                <div class="alert alert-danger">
                    <%= request.getAttribute("errorMsg") %>
                </div>
                <% } %>
                <% if (request.getAttribute("successMsg") != null) { %>
                <div class="alert alert-success">
                    <%= request.getAttribute("successMsg") %>
                </div>
                <% } %>
            </div>
            <div class="row mb-3">
                <div class="col-12">
                    <form class="form" method="get"
                          action="${pageContext.request.contextPath}/admin/category/search">
                        <input type="text" class="form-control" name="size" value="5" hidden/>
                        <input type="text" class="form-control" name="page" value="1" hidden/>
                        <div class="row justify-content-center">
                            <div class="col-7">
                                <input type="text" class="form-control" placeholder="Nhập Loại sản phẩm"
                                       name="keyword"
                                       value="${keyword}">
                            </div>
                            <div class="col-2">
                                <input type="submit" class="btn btn-success col-12" value="Tìm"/>
                            </div>
                        </div>

                    </form>
                </div>
                <div class="col-2">
                </div>
                <div class="col-12 mb-3">
                    <a href="${pageContext.request.contextPath}/admin/category/category-create"
                       class="btn btn-primary">Thêm Loại sản phẩm</a>
                </div>
                <div class="col-12">
                    <table class="table table-hover">
                        <thead>
                        <tr>
                            <th>STT</th>
                            <th>Tên Loại sản phẩm</th>
                            <th>Sửa</th>
                            <th>Xóa</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="category" items="${lstCategory}">
                            <tr>
                                <td>${category.index}</td>
                                <td>${category.name}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/admin/category/update?id=${category.id}"
                                       title="Sửa">
                                        <i class="fas fa-edit"></i>
                                    </a>
                                </td>
                                <td>
                                    <a class="btn-delete" onclick="return confirm('Bạn muốn xóa thể loại này')"
                                       href="${pageContext.request.contextPath}/admin/category/delete?id=${category.id}"
                                       title="Xóa">
                                        <i class="fas fa-trash-alt"></i>
                                    </a>
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
    <div  ${!renderCreateCategory ? 'hidden': ''}>
        <c:import url="/views/admin/category/category-create.jsp"/>
    </div>
    <div class="row" ${!renderUpdateCategory ? 'hidden': ''}>
        <c:import url="/views/admin/category/category-update.jsp"/>
    </div>
</div>
</body>
</html>
