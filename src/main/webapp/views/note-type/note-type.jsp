<%--
  Created by IntelliJ IDEA.
  User: ceotungbeo
  Date: 1/8/24
  Time: 15:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>iNotes</title>
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
<div class="container">
    <div class="row">
        <a href="${pageContext.request.contextPath}/order">Quản lý Ghi chú</a>
    </div>
    <div class="row">
        <div class="col-2">

        </div>
        <div class="col-8">
            <div class="container form-control">
                <div class="row">
                    <div class="col-12 text-center">
                        <h1>Các thể loại</h1>
                    </div>
                    <div class="col-12 mb-3">
                        <a href="${pageContext.request.contextPath}/order-type/create" class="btn btn-primary">Thêm
                            mới</a>
                    </div>
                    <div class="col-3  mb-3">

                    </div>
                    <div class="col-9 mb-3">
                        <form class="form row" method="get"
                              action="${pageContext.request.contextPath}/order-type/search?size=10&page=1&keyword=${keyword}">
                            <div class="col-9">
                                <input type="text" class="form-control" placeholder="Tên thể loại" name="keyword"
                                       value="${keyword}">
                            </div>
                            <div class="col-3">
                                <input type="submit" class="btn btn-success form-control" value="Tìm"/>
                            </div>

                        </form>
                    </div>

                    <div class="col-12">
                        <table class="table table-hover">
                            <thead>
                            <tr>
                                <td>STT</td>
                                <td>Thể loại</td>
                                <td>Mô tả</td>
                                <td></td>
                            </tr>
                            </thead>
                            <tbody>
;
                            <c:forEach var="order" items="${lstData}">
                                <tr>
                                    <td>${order.index}</td>
                                    <td>${order.name}</td>
                                    <td>${order.description}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/order-type/update?id=${order.id}">Sửa</a>
                                        |
                                        <a href="${pageContext.request.contextPath}/order-type/delete?id=${order.id}">Xóa</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="4" class="text-right">

                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>

                </div>
            </div>
        </div>
        <div class="col-2">

        </div>
    </div>
</div>
</body>
</html>
