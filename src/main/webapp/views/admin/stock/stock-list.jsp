<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Danh sách đơn hàng</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="${pageContext.request.contextPath}/js/order/order.js"></script>
    <style>
        body {
            background-color: #f8f9fa;
        }

        .card {
            border: none;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .table-hover tbody tr:hover {
            background-color: #f1f1f1;
        }

        .page-link {
            color: #343a40;
        }

        .page-item.active .page-link {
            background-color: #343a40;
            border-color: #343a40;
        }

        .btn-delete {
            color: #dc3545;
        }

        .btn-delete:hover {
            color: #c82333;
        }

        .form-control, .btn {
            border-radius: 5px;
        }

        .disabled {
            pointer-events: none;
            color: grey; /* Màu xám để thể hiện rằng liên kết này bị vô hiệu hóa */
            cursor: not-allowed; /* Con trỏ sẽ thay đổi để biểu thị không thể nhấn */
            text-decoration: none; /* Loại bỏ gạch chân nếu muốn */
        }

        .icon-large {
            font-size: 30px; /* Thay đổi kích thước theo ý muốn */
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="card" ${!renderStockAdmin ? 'hidden': ''} >
        <div class="card-header text-center">
            <h1>Quản lý tồn kho</h1>
        </div>
        <div class="card-body">
            <div class="col-12 mb-3">

                <% if (request.getAttribute("errorMsg") != null) { %>
                <div class="alert alert-danger">
                    <%= request.getAttribute("errorMsg") %>
                </div>
                <% } %>
                <% if (request.getAttribute("successMsgStock") != null) { %>
                <div class="alert alert-success">
                    <%= request.getAttribute("successMsgStock") %>
                </div>
                <% } %>
            </div>
            <div class="row mb-3">
                <div class="col-12">
                    <form class="form row" method="get"
                          action="${pageContext.request.contextPath}/admin/stock/search">
                        <input type="hidden" name="size" value="10"/>
                        <input type="hidden" name="page" value="1"/>
                        <div class="col-7">
                            <input type="text" class="form-control" placeholder="Mã/Tên sản phẩm" name="keyword"
                                   value="${keyword}">
                        </div>
                        <div class="col-3">
                            <input type="submit" class="btn btn-success col-12" value="Tìm"/>
                        </div>
                    </form>
                </div>
            </div>
            <div class="col-12">
                <%--                <table class="table table-hover table-striped">--%>
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>STT</th>
                        <th>Mã sản phẩm</th>
                        <th>Tên sản phẩm</th>
                        <th>Số lượng tồn kho</th>
                        <th>Số lượng chờ giao hàng</th>
                        <th>Tổng số lượng</th>
                        <th>Nhập hàng</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${stockList}">
                        <tr>
                            <td>${item.index}</td>
                            <td>${item.productCode}</td>
                            <td>${item.productName}</td>
                            <td>${item.strAvailableQuantity}</td>
                            <td>${item.strPendingQuantity}</td>
                            <td>${item.strTotalQuantity}</td>
                            <td>
                                <div onclick="openStock('${item.productCode}', '${item.productName}', '${item.strAvailableQuantity}', '${item.productId}')">
                                    <i class="fas fa-edit" style="color: #004eff"></i>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="row">
                <div class="col-12 text-center">
                    <nav aria-label="Page navigation example">
                        <ul class="pagination" style="justify-content: center">
                            <c:if test="${!firstTab}">
                                <li class="page-item"><a class="page-link"
                                                         href="${pageContext.request.contextPath}/admin/stock/search?page=${currentPage-1}&size=10&keyword=${keyword}">Previous</a>
                                </li>
                            </c:if>
                            <c:forEach begin="${beginPage}" end="${endPage}" var="page">
                                <li class="page-item ${currentPage == page ? 'active' : ''}">
                                    <a class="page-link"
                                       href="${pageContext.request.contextPath}/admin/stock/search?page=${page}&size=10&keyword=${keyword}">${page}</a>
                                </li>
                            </c:forEach>
                            <c:if test="${!lastTab}">
                                <li class="page-item"><a class="page-link"
                                                         href="${pageContext.request.contextPath}/admin/stock/search?page=${currentPage+1}&size=10&keyword=${keyword}">Next</a>
                                </li>
                            </c:if>
                        </ul>
                    </nav>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal modal-xl fade" id="modalStock" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="exampleModalLabel">Chỉnh sửa tồn kho</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
            </div>
            <div class="modal-body">
                <form id="stockForm" class="form form-control p-2" onsubmit="return validateStock()"
                      action="${pageContext.request.contextPath}/admin/stock/import-stock" method="post">
                    <input type="hidden" name="productId" id="productId" hidden/>
                    <div class="form-group">
                        <strong for="productCode">Mã sản phẩm</strong>
                        <input type="text" class="form-control" id="productCode" name="productCode" readonly>
                    </div>
                    <div class="form-group">
                        <strong for="productName">Tên sản phẩm</strong>
                        <input type="text" class="form-control" id="productName" name="productName" readonly>
                    </div>
                    <div class="form-group">
                        <strong for="availableQuantity">Số lượng tồn kho</strong>
                        <input type="number" class="form-control" id="availableQuantity" name="availableQuantity"
                               readonly>
                    </div>
                    <div class="form-group">
                        <strong for="availableQuantity">Số lượng</strong>
                        <input type="number" class="form-control" id="quantity" name="quantity" value="0" min="0">
                    </div>
                    <div class="modal-footer">
                        <span id="stock-validate-message" class="text-danger"></span>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Trở lại</button>
                        <input type="submit" class="btn btn-primary" value="Nhập hàng"/>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    function openStock(productCode, productName, availableQuantity, productId) {
        $('#productCode').val(productCode);
        $('#productName').val(productName);
        $('#availableQuantity').val(availableQuantity);
        $('#productId').val(productId);
        $('#quantity').val(0);
        $('#modalStock').modal('show');
    }


    function prepareImportStock() {
        if (validateStock())
            openConfirmDialog("btn-import-stock-confirm", "Bạn có chắc chắn muốn tạo đơn hàng?", "cart-order-customer-info")
    }

    function validateStock() {
        let messageElementId = "stock-validate-message";
        let quantity = document.getElementById("quantity").value;
        if (!quantity || quantity === '0') {
            return renderErrorMessage(messageElementId, "Số lượng không hợp lệ!")
        }
        return true;
    }
</script>
</body>
</html>