<%--
  Created by IntelliJ IDEA.
  User: ceotungbeo
  Date: 21/8/24
  Time: 13:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div class="modal" id="confirmDialog" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="exampleModalToggleLabel2">Xác nhận</h1>
                <button type="button" class="btn-close" data-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <span id="confirm-message"></span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-secondary" data-dismiss="modal">Trở về</button>
                <button type="button"
                        class="btn btn-primary" id="confirm-button">
                    Đồng ý
                </button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
