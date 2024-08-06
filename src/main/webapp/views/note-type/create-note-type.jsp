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
    <form class="form" method="post" action="${pageContext.request.contextPath}/note-type/create-note-type">
        <div class="row">
            <div class="col-3">

            </div>
            <div class=" col-6">
                <div class="container form-control">
                    <div class="row">
                        <div class="col-12 text-center">
                            <h1>Thêm mới thể loại</h1>
                        </div>
                        <div class="row col-12 align-items-center mb-3">
                            <div class="col-3 mb-3">
                                <label for="inp-name">Tên thể loại</label>
                            </div>
                            <div class="col-9">
                                <input id="inp-name" type="text" class="form-control" name="name"/>
                            </div>
                            <div class="col-3 mb-3">
                                <label for="inp-description">Mô tả</label>
                            </div>
                            <div class="col-9">
                                <input id="inp-description" type="text" class="form-control" name="description"/>
                            </div>
                        </div>
                        <div class="row col-12">
                            <div class="col-3">
                            </div>
                            <div class="col-3  d-grid gap-2">
                                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/note-type">Hủy</a>
                            </div>
                            <div class="col-6  d-grid gap-2">
                                <input type="submit" class="btn btn-primary" value="Lưu"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row align-items-center mb-3">

                </div>
            </div>
        </div>
</div>
</form>
</div>
</body>
</html>
