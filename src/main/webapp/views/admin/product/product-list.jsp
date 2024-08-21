<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Danh sách sản phẩm</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>

    <style>
        body {
            background-color: #f4f7f9; /* Softer light grey background */
            font-family: 'Roboto', sans-serif; /* Modern font for better readability */
        }

        h1 {
            color: #212529; /* Slightly darker grey for headings */
            font-weight: 700; /* Bolder heading for emphasis */
            margin-bottom: 25px;
        }

        .form-control, .table, .btn {
            border-radius: 8px; /* Slightly rounded corners */
        }

        .btn-primary {
            background-color: #007bff;
            border-color: #007bff;
            font-weight: 600; /* Slightly bolder button text */
            padding: 10px 20px; /* Added padding for a more prominent button */
            transition: background-color 0.3s ease, transform 0.2s ease;
        }

        .btn-primary:hover {
            background-color: #0056b3;
            border-color: #0056b3;
            transform: translateY(-2px); /* Slight lift on hover */
        }

        .table-hover tbody tr:hover {
            background-color: #e9ecef; /* Softer hover color */
        }

        .pagination .page-item.active .page-link {
            background-color: #007bff;
            border-color: #007bff;
        }

        .pagination .page-link {
            color: #007bff;
            transition: color 0.3s ease, background-color 0.3s ease;
        }

        .pagination .page-link:hover {
            color: #0056b3;
            background-color: #e9ecef; /* Light grey background on hover */
        }

        .alert {
            font-size: 1rem; /* Slightly larger font size for alerts */
            margin-top: 20px;
        }

        .table thead th {
            color: white;
            text-align: center; /* Centered text in the header */
        }

        .table td, .table th {
            padding: 20px; /* Increased padding for better spacing */
            vertical-align: middle;
            text-align: center; /* Center text in table cells */
        }

        .table-hover tbody tr {
            transition: all 0.3s ease;
        }

        .table-hover tbody tr:hover {
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            transform: translateY(-3px);
        }

        .form-control {
            padding: 12px; /* Increased padding for better input field size */
            border: 1px solid #ced4da;
        }

        .form .col-2, .form .col-7, .form .col-1 {
            margin-bottom: 15px; /* Increased margin for better spacing */
        }

        .pagination {
            margin-top: 25px;
        }

        /* Enhanced button styles */
        .btn {
            transition: all 0.3s ease;
        }

        .btn-success {
            background-color: #28a745;
            border-color: #28a745;
            font-weight: 600;
            padding: 10px 20px;
        }

        .btn-success:hover {
            background-color: #218838;
            border-color: #1e7e34;
            transform: translateY(-2px);
        }

        /* Custom styles for the search form */
        .form {
            margin-bottom: 20px; /* Spacing between form and other elements */
        }

        .card {
            border: none;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="card" ${!renderProductList ? 'hidden': ''} >
        <div class="card-header text-center">
            <h1>Danh sách sản phẩm</h1>
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
                    <form class="form row justify-content-center" method="get"
                          action="${pageContext.request.contextPath}/admin/product/search">
                        <input type="text" class="form-control" name="size" value="5" hidden/>
                        <input type="text" class="form-control" name="page" value="1" hidden/>
                        <div class="col-2">
                            <select class="form-control" name="category-id">
                                <option value="0">--Chọn thể loại--</option>
                                <c:forEach var="category" items="${lstCategory}">
                                    <option value="${category.id}">${category.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-7">
                            <input type="text" class="form-control" placeholder="Mã/Tên sản phẩm" name="keyword"
                                   value="${keyword}">
                        </div>
                        <div class="col-2">
                            <input type="submit" class="btn btn-success col-12" value="Tìm"/>
                        </div>

                    </form>
                </div>
                <div class="col-2">
                </div>
                <div class="col-12 mb-3">
                    <a href="${pageContext.request.contextPath}/admin/product/product-create"
                       class="btn btn-primary">Thêm mới</a>
                </div>
                <div class="col-12">
                    <table class="table table-hover">
                        <thead>
                        <tr>
                            <th>STT</th>
                            <th>Mã sản phẩm</th>
                            <th>Tên sản phẩm</th>
                            <th>Thể loại</th>
                            <th>Giá</th>
                            <th>Số lượng</th>
                            <th>Mô tả</th>
                            <th>Sửa</th>
                            <th>Xóa</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="product" items="${lstData}">
                            <tr>
                                <td>${product.index}</td>
                                <td>${product.productCode}</td>
                                <td>${product.productName}</td>
                                <td>${product.categoryName}</td>
                                <td style="text-align: right">${product.strPrice}</td>
                                <td style="text-align: right">${product.strQuantity}</td>
                                <td>${product.description}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/admin/product/update?id=${product.id}"
                                       title="Sửa">
                                        <i class="fas fa-edit"></i>
                                    </a>
                                </td>
                                <td>
                                    <a class="btn-delete" onclick="return confirm('Bạn muốn xóa sản phẩm này')"
                                       href="${pageContext.request.contextPath}/admin/product/delete?id=${product.id}"
                                       title="Xóa">
                                        <i class="fas fa-trash-alt"></i></a>
                                    </a>
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
                                                         href="${pageContext.request.contextPath}/admin/search?page=${currentPage-1}&size=5">
                                    Previous</a></li>
                            </c:if>

                            <c:forEach begin="${beginPage}" end="${endPage}" var="page">
                                <li class="page-item ${currentPage == page ? 'active' : ''}">
                                    <a class="page-link"
                                       href="${pageContext.request.contextPath}/admin/search?page=${page}&size=5">${page}</a>
                                </li>
                            </c:forEach>
                            <c:if test="${!lastTab}">
                                <li class="page-item">
                                    <a class="page-link"
                                       href="${pageContext.request.contextPath}/admin/search?page=${currentPage+1}&size=5">
                                        Next</a></li>
                            </c:if>
                        </ul>
                    </nav>
                </div>
            </div>
        </div>
        <%--            </div>--%>
        <%--        </div>--%>
    </div>
    <div class="row" ${!renderProductCreate ? 'hidden': ''}>
        <c:import url="/views/admin/product/product-create.jsp"/>
    </div>
    <div class="row" ${!renderProductUpdate ? 'hidden': ''}>
        <c:import url="/views/admin/product/product-update.jsp"/>
    </div>
</div>
</body>
</html>
