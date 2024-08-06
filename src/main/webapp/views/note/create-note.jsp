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
</head>
<body>
<div class="container">
    <form class="form" method="post" action="${pageContext.request.contextPath}/order/create-order">
        <div class="row">
            <div class="col-3">

            </div>
            <div class=" col-6">
                <div class="container form-control">
                    <div class="row col-12 text-center">
                        <h1>Thêm mới ghi chú</h1>
                    </div>
                    <div class="row align-items-center mb-3">
                        <div class="col-3">
                            <label for="inp-title">Tiêu đề</label>
                        </div>
                        <div class="col-9">
                            <input id="inp-title" type="text" class="form-control" name="title"/>
                        </div>
                    </div>
                    <div class="row align-items-center mb-3">
                        <div class="col-3">
                            <label for="inp-content">Nội dung</label>
                        </div>
                        <div class="col-9">
                            <textarea id="inp-content" class="form-control" name="content"></textarea>
                        </div>

                    </div>
                    <div class="row align-items-center mb-3">
                        <div class="col-3">
                        </div>
                        <div class="col-3  d-grid gap-2">
                            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/order">Hủy</a>
                        </div>
                        <div class="col-6  d-grid gap-2">
                            <input type="submit" class="btn btn-primary" value="Lưu"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
</body>
</html>
