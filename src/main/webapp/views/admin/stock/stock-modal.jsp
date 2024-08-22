<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="${pageContext.request.contextPath}/js/stock/stock.js"></script>
</head>
<body>
<div class="modal modal-xl fade" id="modalStock" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="form-create-order-batch" class="form form-control p-2"
                  action="${pageContext.request.contextPath}/stock/confirm-import" method="post">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="exampleModalLabel">Kho</h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="stockForm">
                        <div class="form-group">
                            <label class="label-modal" for="productCode">Mã sản phẩm</label>
                            <input type="text" class="form-control" id="productCode" name="productCode" readonly>
                        </div>
                        <div class="form-group">
                            <label for="productName">Tên sản phẩm</label>
                            <input type="text" class="form-control" id="productName" name="productName" readonly>
                        </div>
                        <div class="form-group">
                            <label for="availableQuantity">Số lượng tồn kho</label>
                            <input type="number" class="form-control" id="availableQuantity" name="availableQuantity">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <span id="order-validate-message" class="text-danger"></span>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Trở lại</button>
                    <button type="button"
                            class="btn btn-primary" id="btn-prepare-create-order" onclick="prepareStock()">
                        Nhập hàng
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
