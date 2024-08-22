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
    <script src="${pageContext.request.contextPath}/js/order/order.js"></script>
</head>
<body>
<div class="modal modal-xl fade" id="modalCart" tabindex="-1" aria-labelledby="modalCartLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="form-create-order-batch" class="form form-control p-2"
                  action="${pageContext.request.contextPath}/order/create-order-batch" method="post">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="exampleModalLabel">Giỏ hàng</h1>
                    <button type="button" class="btn-close" data-dismiss="modal" aria-label="Đóng"></button>
                </div>
                <div class="modal-body">

                    <table class="table">
                        <thead>
                        <tr>
                            <th>STT</th>
                            <th>Tên sản phẩm</th>
                            <th>Số lượng</th>
                            <th>Đơn giá (VNĐ)</th>
                            <th>Thành tiền (VNĐ)</th>
                        </tr>
                        </thead>
                        <tbody id="tbody-table-cart">

                        </tbody>
                        <tfoot id="tfoot-table-cart">
                        </tfoot>
                    </table>
                    <span id="cart-empty-message"></span>
                    <input type="hidden" id="cart-row-count" name="rowCount"/>
                    <div class="col-12" id="cart-order-customer-info-input" hidden>
                        <c:import url="/views/order/order-customer-info-input.jsp"/>
                    </div>

                </div>
                <div class="modal-footer">
                    <span id="order-validate-message" class="text-danger"></span>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Trở lại</button>
                    <button type="button"
                            class="btn btn-primary" id="btn-prepare-create-order" onclick="prepareCreateOrder()">
                        Tiến hành đặt hàng
                    </button>
                    <button type="button" data-target="#confirmDialog" data-toggle="modal"
                            class="btn btn-primary" id="btn-create-order-confirm"
                            hidden>1234
                    </button>

                </div>
            </form>
        </div>
    </div>
</div>

</body>
<script>

</script>
</html>
