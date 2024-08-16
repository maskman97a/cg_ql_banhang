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
<div>
    <div class="row" ${!renderCategoryList ? 'hidden': ''}>
        <div class="container">
            <div class="container form-control">
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
                <div class="row">
                    <div class="col-12 text-center">
                        <h1>Quản lý Loại sản phẩm</h1>
                    </div>
                    <div class="col-12 mb-3">
                        <form class="form" method="get"
                              action="${pageContext.request.contextPath}/admin/category/search">
                            <input type="text" class="form-control" name="size" value="10" hidden/>
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
                                <th>Tác động</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="category" items="${lstCategory}">
                                <tr>
                                    <td>${category.index}</td>
                                    <td>${category.name}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/admin/category/update?id=${category.id}">Sửa</a>
                                        |
                                        <a class="btn-delete" onclick="return confirm('Bạn muốn xóa thể loại này')"
                                           href="${pageContext.request.contextPath}/admin/category/delete?id=${category.id}">Xóa</a>
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
    <div  ${!renderCreateCategory ? 'hidden': ''}>
        <c:import url="/views/admin/category/category-create.jsp"/>
    </div>
    <div class="row" ${!renderUpdateCategory ? 'hidden': ''}>
        <c:import url="/views/admin/category/category-update.jsp"/>
    </div>
</div>
</body>
</html>
