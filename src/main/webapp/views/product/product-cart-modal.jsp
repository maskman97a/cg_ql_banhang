<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ceotungbeo
  Date: 12/8/24
  Time: 19:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div class="modal modal-xl fade" id="modalCart" tabindex="-1" aria-labelledby="modalCartLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="exampleModalLabel">Giỏ hàng</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <table class="table">
                    <thead>
                    <tr>
                        <th>STT</th>
                        <th>Tên sản phẩm</th>
                        <th>Số lượng</th>
                        <th>Đơn giá</th>
                        <th>Thành tiền</th>
                    </tr>
                    </thead>
                    <tbody id="tbody-table-cart">

                    </tbody>
                </table>
                <span>${(cartProductList == null || cartProductList.size == 0 ) ?'Không có sản phẩm nào được chọn': ''}</span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Trở lại</button>
                <button type="button"
                        class="btn btn-primary" ${(cartProductList == null || cartProductList.size == 0 ) ?'disabled': ''}>
                    Tiến hành đặt hàng
                </button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
