<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Danh sách đơn hàng</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .container {
            margin-top: 20px;
        }
        .card {
            border: none;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        .card-header {
            background-color: #e3b942;
            color: #ffffff;
            border-radius: 10px 10px 0 0;
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
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="card" ${renderOrderAdmin ? 'hidden': ''} >
        <div class="card-header text-center">
            <h1>Danh sách đơn hàng</h1>
        </div>
        <div class="card-body">
            <div class="col-12 mb-3">
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
            <div class="row mb-3">
                <div class="col-12">
                    <form class="form row" method="get" action="${pageContext.request.contextPath}/admin/transaction/search">
                        <input type="hidden" name="size" value="5"/>
                        <input type="hidden" name="page" value="1"/>
                        <div class="col-2">
                            <select class="form-control" name="status-order-id">
                                <option value="">--Trạng thái--</option>
                                <option value="0">Tạo mới, chờ xác nhận</option>
                                <option value="1">Hoàn thành</option>
                                <option value="2">Đã hủy</option>
                                <option value="3">Đã xác nhận</option>
                            </select>
                        </div>
                        <div class="col-7">
                            <input type="text" class="form-control" placeholder="Mã đơn hàng/Số điện thoại" name="keyword" id="inp-order-code" value="${keyword}">
                        </div>
                        <div class="col-3">
                            <input type="submit" class="btn btn-success w-100" value="Tìm"/>
                        </div>
                    </form>
                </div>
            </div>
                <div class="col-12">
<%--                    <table class="table table-hover table-striped">--%>
                        <table class="table table-hover">
                        <thead>
                        <tr>
                            <th>STT</th>
                            <th>Mã đơn hàng</th>
                            <th>Trạng thái đơn hàng</th>
                            <th>Tên khách hàng</th>
                            <th>Email</th>
                            <th>Số điện thoại</th>
                            <th>Địa chỉ</th>
                            <th>Ngày đặt hàng</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="item" items="${lstOrder}">
                            <tr>
                                <td>${item.index}</td>
                                <td><a href="${pageContext.request.contextPath}/admin/transaction/detail?orderCode=${item.code}" title="Xem chi tiết">${item.code}</a></td>
                                <td>${item.statusName}</td>
                                <td>${item.customerName}</td>
                                <td>${item.email}</td>
                                <td>${item.phone}</td>
                                <td>${item.address}</td>
                                <td>${item.orderDateStr}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/admin/transaction/confirm?id=${item.id}" title="Xác nhận đơn hàng">
                                        <i class="fas fa-check-circle"></i>
                                    </a>
                                    |
                                    <a href="${pageContext.request.contextPath}/admin/transaction/complete?id=${item.id}" disabled="${item.status != 3}" title="Hoàn thành">
                                        <i class="fas fa-check"></i>
                                    </a>
                                    |
                                    <a class="btn-delete" onclick="return confirm('Bạn muốn hủy đơn hàng này không?')" disabled="${item.status != 0}" href="${pageContext.request.contextPath}/admin/transaction/delete?id=${item.id}" title="Hủy">
                                        <i class="fas fa-trash-alt"></i></a>
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
                                <li class="page-item"><a class="page-link" href="${pageContext.request.contextPath}/admin/transaction/search?page=${currentPage-1}&size=5">Previous</a></li>
                            </c:if>
                            <c:forEach begin="${beginPage}" end="${endPage}" var="page">
                                <li class="page-item ${currentPage == page ? 'active' : ''}">
                                    <a class="page-link" href="${pageContext.request.contextPath}/admin/transaction/search?page=${page}&size=5">${page}</a>
                                </li>
                            </c:forEach>
                            <c:if test="${!lastTab}">
                                <li class="page-item"><a class="page-link" href="${pageContext.request.contextPath}/admin/transaction/search?page=${currentPage+1}&size=5">Next</a></li>
                            </c:if>
                        </ul>
                    </nav>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="row" ${!renderOrderAdmin ? 'hidden': ''}>
    <c:import url="/views/admin/transaction/transaction-detail.jsp"/>
</div>
</body>
</html>