<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Danh sách thể loại</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
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
    <div class="row" ${renderOrderAdmin ? 'hidden': ''} >
        <div class="col-12" >
                <div class="form-control" style="min-height: 100%">
                    <div class="col-12 mb-3">
                        <span>${response}</span>
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
                    <div class="row">
                        <div class="col-12 text-center">
                            <h1>Danh sách đơn hàng</h1>
                        </div>

                        <div class="col-3  mb-3">

                        </div>

                        <div class="col-9 mb-3">
                            <form class="form row" method="get"
                                  action="${pageContext.request.contextPath}/admin/transaction/search">
                                <input type="text" class="form-control" name="size" value="5" hidden/>
                                <input type="text" class="form-control" name="page" value="1" hidden/>
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
                                    <input type="text" class="form-control" placeholder="Mã đơn hàng/Số điện thoại"
                                           name="keyword"
                                           id="inp-order-code"
                                           value="${keyword}">
                                </div>
                                <div class="col-3">
                                    <input type="submit" class="btn btn-success" value="Tìm"/>
                                </div>

                            </form>
                        </div>
                        <div class="col-2">
                        </div>
                        <div class="col-12">
                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <td>STT</td>
                                    <td>Mã đơn hàng</td>
                                    <td>Trạng thái đơn hàng</td>
                                    <td>Tên khách hàng</td>
                                    <td>Email</td>
                                    <td>Số điện thoại</td>
                                    <td>Địa chỉ</td>
                                    <td>Ngày đặt hàng</td>
                                    <td>Action</td>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="item" items="${lstOrder}">
                                    <tr>
                                        <td>${item.index}</td>
                                        <td>${item.code}</td>
                                        <td>${item.statusName}</td>
                                        <td>${item.customerName}</td>
                                        <td>${item.email}</td>
                                        <td>${item.phone}</td>
                                        <td>${item.address}</td>
                                        <td>${item.orderDateStr}</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/admin/transaction/detail?orderCode=${item.code}"
                                               title="Xem chi tiết">
                                                <i class="fas fa-search"></i>
                                            </a>
                                            |
                                            <a href="${pageContext.request.contextPath}/admin/transaction/confirm?id=${item.id}"
                                               title="Xác nhận đơn hàng">
                                                <i class="fas fa-check-circle"></i>
                                            </a>
                                            |
                                            <a href="${pageContext.request.contextPath}/admin/transaction/complete?id=${item.id}"
                                               disabled="${item.status != 3}"
                                               title="Hoàn thành">
                                                <i class="fas fa-check"></i>
                                            </a>
                                            |
                                            <a class="btn-delete"
                                               onclick="return confirm('Bạn muốn hủy đơn hàng này không?')"
                                               disabled="${item.status != 0}"
                                               href="${pageContext.request.contextPath}/admin/transaction/delete?id=${item.id}"
                                               title="Hủy">
                                                <i class="fas fa-trash-alt"></i></a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        <div class="text-center col-12">
                            <nav aria-label="Page navigation example">
                                <ul class="pagination" style="justify-content: center">
                                    <c:if test="${!firstTab}">
                                        <li class="page-item"><a class="page-link"
                                                                 href="${pageContext.request.contextPath}/admin/transaction/search?page=${currentPage-1}&size=5">
                                            Previous</a></li>
                                    </c:if>

                                    <c:forEach begin="${beginPage}" end="${endPage}" var="page">
                                        <li class="page-item ${currentPage == page ? 'active' : ''}">
                                            <a class="page-link"
                                               href="${pageContext.request.contextPath}/admin/transaction/search?page=${page}&size=5">${page}</a>
                                        </li>
                                    </c:forEach>
                                    <c:if test="${!lastTab}">
                                        <li class="page-item">
                                            <a class="page-link"
                                               href="${pageContext.request.contextPath}/admin/transaction/search?page=${currentPage+1}&size=5">
                                                Next</a></li>
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
