<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
</head>
<body>
<div>
    <form class="form form-control p-4" method="post"
          action="${pageContext.request.contextPath}/admin/category/category-create"
          enctype="multipart/form-data">
        <div class="col-12 mb-3">
            <span>${response}</span>
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
        <div class="row">
            <div class=" col-3">
            </div>
            <div class=" col-6">
                <div class="row">
                    <div class="col-12 text-center">
                        <h1>Thêm mới Loại sản phẩm</h1>
                    </div>
                    <div class="row col-12 align-items-center mb-3">
                        <div class="col-12">
                            <label class="form-label" for="inp-name">Tên Loại sản phẩm:</label>
                            <input id="inp-name" type="text" class="form-control" name="name"
                                   placeholder="Nhập tên Loại sản phẩm"/>
                        </div>
                        <div class="col-12">
                            <label class="form-label" for="inp-name">Thứ tự hiển thị:</label>
                            <input id="inp-sort" type="number" class="form-control" name="sort" min="0"
                                   placeholder="Nhập tên Thứ tự hiển thị"/>
                        </div>
                        <script>
                            function limitLength(input) {
                                if (input.value.length > 15) {
                                    input.value = input.value.slice(0, 15);
                                }
                            }
                        </script>
                    </div>
                    <div class="row col-12">
                        <div class="col-3">
                        </div>
                        <div class="col-3  d-grid gap-2">
                            <a class="btn btn-secondary"
                               href="${pageContext.request.contextPath}/admin/category">Hủy
                            </a>
                        </div>
                        <div class="col-6  d-grid gap-2">
                            <input type="submit" class="btn btn-primary" value="Lưu"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class=" col-3">
            </div>
        </div>
    </form>
</div>
</body>
</html>
