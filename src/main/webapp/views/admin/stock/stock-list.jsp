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
    <div class="card" ${renderStockAdmin ? 'hidden': ''} >
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
                <% if (request.getAttribute("successMsg") != null) { %>
                <div class="alert alert-success">
                    <%= request.getAttribute("successMsg") %>
                </div>
                <% } %>
            </div>
            <div class="row mb-3">
                <div class="col-12">
                    <form class="form row" method="get"
                          action="${pageContext.request.contextPath}/admin/transaction/search">
                        <input type="hidden" name="size" value="5"/>
                        <input type="hidden" name="page" value="1"/>
                        <div class="col-2">
                            <select class="form-control" name="category-id">
                                <option value="0">--Chọn thể loại--</option>
                                <c:forEach var="category" items="${lstCategory}">
                                    <option value="${category.id}">${category.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-7">
                            <input type="text" class="form-control" placeholder="Mã đơn hàng/Số điện thoại"
                                   name="keyword" id="inp-order-code" value="${keyword}">
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
                        <th>Mã đơn hàng</th>
                        <th>Trạng thái đơn hàng</th>
                        <th>Tên khách hàng</th>
                        <th>Email</th>
                        <th>Số điện thoại</th>
                        <th>Địa chỉ</th>
                        <th>Ngày đặt hàng</th>
                        <th>Xác nhận đơn</th>
                        <th>Hoàn thành</th>
                        <th>Hủy</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${lstOrder}">
                        <tr>
                            <td>${item.index}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/transaction/detail?orderCode=${item.code}"
                                   title="Xem chi tiết">${item.code}</a></td>
                            <td>${item.statusName}</td>
                            <td>${item.customerName}</td>
                            <td>${item.email}</td>
                            <td>${item.phone}</td>
                            <td>${item.address}</td>
                            <td>${item.orderDateStr}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/transaction/confirm?id=${item.id}"
                                   class="${item.status != 0 ? 'disabled': ''}"
                                   title="Xác nhận đơn hàng">
                                    <i class="fas fa-check-circle icon-large"></i>
                                </a>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/transaction/complete?id=${item.id}"
                                   class=" ${item.status != 3 ? 'disabled': 'btn-delete'}"
                                   title="Hoàn thành">
                                    <i class="fas fa-check icon-large"></i>
                                </a>
                            </td>
                            <td>
                                <a onclick="return confirm('Bạn muốn hủy đơn hàng này không?')"
                                   class=" ${item.status != 0 ? 'disabled': 'btn-delete'}"
                                   href="${pageContext.request.contextPath}/admin/transaction/delete?id=${item.id}"
                                   title="Hủy">
                                    <i class="fas fa-trash-alt icon-large"></i></a>
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
                                                         href="${pageContext.request.contextPath}/admin/transaction/search?page=${currentPage-1}&size=5">Previous</a>
                                </li>
                            </c:if>
                            <c:forEach begin="${beginPage}" end="${endPage}" var="page">
                                <li class="page-item ${currentPage == page ? 'active' : ''}">
                                    <a class="page-link"
                                       href="${pageContext.request.contextPath}/admin/transaction/search?page=${page}&size=5">${page}</a>
                                </li>
                            </c:forEach>
                            <c:if test="${!lastTab}">
                                <li class="page-item"><a class="page-link"
                                                         href="${pageContext.request.contextPath}/admin/transaction/search?page=${currentPage+1}&size=5">Next</a>
                                </li>
                            </c:if>
                        </ul>
                    </nav>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="row" ${!renderStockAdmin ? 'hidden': ''}>
    <c:import url="/views/admin/transaction/transaction-detail.jsp"/>
</div>
</body>
</html>