<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ceotungbeo
  Date: 3/8/24
  Time: 18:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div class="row">
    <div class="col-8"></div>
    <div class="col-3">
        <select id="combobox-sort" class="form-control" name="sort-condition" onchange="onSort()">
            <option value="">--Sắp xếp theo--</option>
            <c:forEach var="sort" items="${sortList}">
                <option value="${sort.columnName}-${sort.sortType}" ${sort.name == selectedSort ? 'selected': ''}>${sort.description}</option>
            </c:forEach>
        </select>
        <a id="link-order" hidden></a>
    </div>
    <script>
        function onSort() {
            let sortCombobox = document.getElementById("combobox-sort").value;
            let comboValue = sortCombobox.split("-");
            let columnName = comboValue[0].trim();
            let sortType = comboValue[1].trim();

            let linkOrder = document.getElementById("link-order");
            linkOrder.href = "${pageContext.request.contextPath}/product/search?page=${currentPage}&size=8&keyword=${keyword}&categoryId=${categoryId}&sortCol=" + columnName + "&sortType=" + sortType;
            linkOrder.click();
        }
    </script>
    <div class="col-12 row">
        <c:forEach var="product" items="${lstProduct}">
            <div class="col-3 p-3">
                <div class="shadow p-3 mb-5 bg-body-tertiary rounded">
                    <a href="${pageContext.request.contextPath}/product/detail?id=${product.id}" class="row">
                        <div class="col-12 ratio ratio-1x1">
                            <img src="${pageContext.request.contextPath}/image/${product.imageUrl}"
                                 id="product-image-${product.id}"
                                 class="img-fluid col-12"
                                 alt="${product.name}" width="100%" height="100%" style="z-index: 0">
                        </div>
                        <span class="col-12" style="font-weight: bold">${product.name}</span>

                        <span id="col-price-${product.id}" class="col-12 formatted-number"
                              style="font-weight: bold; color:red;">
                <script>
                    document.getElementById("col-price-${product.id}").innerHTML = formatNumber(${product.price})
                </script>
                đ</span>
                    </a>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="text-center col-12">
        <nav aria-label="Page navigation example">
            <ul class="pagination" style="justify-content: center">
                <c:if test="${!firstTab}">
                    <li class="page-item">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/product/search?page=${currentPage-1}&size=8&keyword=${keyword}&categoryId=${categoryId}&sortCol=${sortCol}&sortType=${sortType}">
                        Previous</a></li>
                </c:if>

                <c:forEach begin="${beginPage}" end="${endPage}" var="page">
                    <li class="page-item ${currentPage == page ? 'active' : ''}">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/product/search?page=${page}&size=8&keyword=${keyword}&categoryId=${categoryId}&sortCol=${sortCol}&sortType=${sortType}">${page}</a>
                    </li>
                </c:forEach>
                <c:if test="${!lastTab}">
                    <li class="page-item">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/product/search?page=${currentPage+1}&size=8&keyword=${keyword}&categoryId=${categoryId}&sortCol=${sortCol}&sortType=${sortType}">
                            Next</a></li>
                </c:if>
            </ul>
        </nav>
    </div>
</div>

</body>
</html>
