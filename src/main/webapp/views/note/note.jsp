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
        <a href="${pageContext.request.contextPath}/note-type">Quản lý Thể loại</a>
    </div>
    <div class="row">
        <div class="col-2">

        </div>
        <div class="col-8">
            <div class="container form-control">
                <div class="row">
                    <div class="col-12 text-center">
                        <h1>Các ghi chú</h1>
                    </div>
                    <div class="col-12 mb-3">
                        <a href="${pageContext.request.contextPath}/note/create" class="btn btn-primary">Thêm mới</a>
                    </div>
                    <div class="col-3  mb-3">
                        <select class="form-select" name="note-type">
                            <option value="0">Thể loại</option>
                            <c:forEach var="item" items="${noteTypeList}">
                                <option value="${item.id}">${item.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-9 mb-3">
                        <form class="form row" method="get"
                              action="${pageContext.request.contextPath}/note/search?size=10&page=1&keyword=${keyword}">
                            <div class="col-9">
                                <input type="text" class="form-control" placeholder="Tiêu đề" name="keyword"
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
                                <td>Tiêu đề</td>
                                <td>Phân loại</td>
                                <td></td>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="note" items="${lstData}">
                                <tr>
                                    <td>${note.index}</td>
                                    <td>${note.title}</td>
                                    <td>${note.noteType.name}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/note/update?id=${note.id}">Sửa</a> |
                                        <a href="${pageContext.request.contextPath}/note/delete?id=${note.id}">Xóa</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="4" class="text-right">
                                    <div class="container row text-center col-12" style="position: fixed; bottom: 5px;">
                                        <nav aria-label="Page navigation example">
                                            <ul class="pagination" style="justify-content: center">
                                                <c:if test="${!firstTab}">
                                                    <li class="page-item"><a class="page-link"
                                                                             href="${pageContext.request.contextPath}/note/search?page=${currentPage-1}&size=10">
                                                        Previous</a></li>
                                                </c:if>

                                                <c:forEach begin="${beginPage}" end="${endPage}" var="page">
                                                    <li class="page-item ${currentPage == page ? 'active' : ''}">
                                                        <a class="page-link"
                                                           href="${pageContext.request.contextPath}/note/search?page=${page}&size=10">${page}</a>
                                                    </li>
                                                </c:forEach>
                                                <c:if test="${!lastTab}">
                                                    <li class="page-item">
                                                        <a class="page-link"
                                                           href="${pageContext.request.contextPath}/note/search?page=${currentPage+1}&size=10">
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
        <div class="col-2">

        </div>
    </div>
</div>
</body>
</html>
