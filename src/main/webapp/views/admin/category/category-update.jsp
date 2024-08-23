<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <style>
        #preview {
            max-width: 100%;
            max-height: 300px;
            margin-top: 20px;
        }
    </style>
</head>
<body>
<div class="container">
    <form class="form form-control p-3" method="post" action="${pageContext.request.contextPath}/admin/category/update">
        <input type="text" class="form-control" name="id"
               value="${category.id}"
               hidden/>
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
                        <h1>Cập nhật sản phẩm</h1>
                    </div>
                    <div class="row col-12 align-items-center mb-3">
                        <div class="col-12 mb-3">
                            <label for="inp-name">Tên thể loại</label>
                            <input id="inp-name" type="text" value="${category.name}"
                                   class="form-control" name="name"/>
                        </div>
                        <div class="col-12 mb-3">
                            <label for="inp-sort">Thứ tự sắp xếp</label>
                            <input id="inp-sort" type="number" value="${category.sort}" min="0"
                                   class="form-control" name="sort"/>
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
