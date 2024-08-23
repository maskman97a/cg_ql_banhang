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
    <div class="col-12 align-items-center mb-3">
        <span class="fw-bold fs-3">Sản phẩm nổi bật </span>
        <div class="col-9">
            <c:forEach var="category" items="${lstCategory}">
                <a class="btn btn-outline-info ml-3 shadow"
                   href="${pageContext.request.contextPath}/product/search?categoryId=${category.id}">${category.name}</a>
            </c:forEach>
        </div>
    </div>
    <c:if test="${categoryId != null}">
        <div class="col-12 align-items-center mb-3">
            <span class="fw-bold fs-3">Sắp xếp theo </span>
            <div class="col-9">
                <c:forEach var="sort" items="${sortList}">
                    <button class="btn border"
                            onclick="onSort('${sort.columnName}', '${sort.sortType}')">
                        <i class="fa-solid ${sort.fontAwesome}"></i>
                            ${sort.description}</button>
                </c:forEach>
            </div>
            <a id="link-order" hidden></a>
        </div>
    </c:if>
    <div class="col-12 row">
        <c:forEach var="productPerCategory" items="${productPerCategoryList}">
            <div class=" col-12 text-left">
                <a href="${pageContext.request.contextPath}/product/search?categoryId=${productPerCategory.categoryId}&keyword=${keyword}"
                   class="btn"> <span class="col-12 fs-4 fw-bold">${productPerCategory.categoryName}</span></a>
            </div>
            <c:forEach var="productPaging" items="${productPerCategory.productPagingList}">
                <div class="row mb-3">
                    <c:forEach var="product" items="${productPaging.productList}">
                        <div class="col-3 p-3">
                            <div class="shadow p-3 rounded mt-3">
                                <div class="col-12 justify-content-end pe-3" style="height:30px">
                                    <c:if test="${product.availableQuantity <= 0}">
                                        <div class="col-5 p-1 rounded text-center" style=" background-color: red"><span
                                                class="text-white"
                                                style="">Hết hàng</span></div>
                                    </c:if>
                                </div>
                                <div class="col-12 ratio ratio-1x1 justify-content-center align-items-center">
                                    <img src="${pageContext.request.contextPath}/image/${product.imageUrl}"
                                         id="product-image-${product.id}"
                                         class="img-fluid rounded product-image"
                                         alt="${product.name}" width="100%"
                                         style="z-index: 0; object-fit: contain; cursor: pointer"
                                         onclick="viewProductDetail('link-for-product-image-${product.id}')">
                                    <script>
                                        function viewProductDetail(linkid) {
                                            document.getElementById(linkid).click();
                                        }
                                    </script>
                                    <a id="link-for-product-image-${product.id}"
                                       href="${pageContext.request.contextPath}/product/detail?id=${product.id}"
                                       hidden>

                                    </a>
                                </div>
                                <div class="col-12">
                                    <a href="${pageContext.request.contextPath}/product/detail?id=${product.id}"
                                       class="col-12" style="text-decoration: none; color:#635c5c">
                                        <div class="col-12" style="height:40px; line-height: 20px">
                                            <span class="col-12" style="font-weight: bold;">${product.name}</span>
                                        </div>
                                    </a>
                                    <div class="row align-items-center mb-3">
                                        <div class="col-10">
                                            <a href="${pageContext.request.contextPath}/product/detail?id=${product.id}"
                                               style="text-decoration: none; color:#635c5c">
                                                <span id="col-price-${product.id}" class="col-12 formatted-number"
                                                      style="font-weight: bold; color:red;"></span>
                                                <script>
                                                    document.getElementById("col-price-${product.id}").innerHTML = formatNumber(${product.price}) + " đ"
                                                </script>
                                            </a>
                                        </div>
                                        <div class="col-2 text-center fs-4"
                                             style="color:green; padding-right:3px">
                                            <i class="fa-solid fa-cart-plus"
                                               onclick="addToCart(${product.id}, 'product-image-${product.id}', ${product.availableQuantity})"></i>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-12 ">
                                    <textarea class="form-control bg-body-secondary" disabled
                                              style="font-size: 11px"
                                              readonly>${product.description != '' ? product.description : 'Chưa mô tả cho sản phẩm này!'}</textarea>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:forEach>
            <c:if test="${productPerCategory.paging != null}">
                <div class="text-center col-12">
                    <nav aria-label="Page navigation example">
                        <ul class="pagination" style="justify-content: center">
                            <c:if test="${!productPerCategory.paging.firstTab}">
                                <li class="page-item">
                                    <a class="page-link"
                                       href="${pageContext.request.contextPath}/product/search?page=${productPerCategory.paging.currentPage-1}&size=8&keyword=${keyword}&categoryId=${categoryId}&sortCol=${sortCol}&sortType=${sortType}">
                                        Previous</a></li>
                            </c:if>
                                <%----%>
                            <c:forEach begin="${productPerCategory.paging.beginPage}" end="${endPage}" var="page">
                                <li class="page-item ${currentPage == page ? 'active' : ''}">
                                    <a class="page-link"
                                       href="${pageContext.request.contextPath}/product/search?page=${page}&size=8&keyword=${keyword}&categoryId=${categoryId}&sortCol=${sortCol}&sortType=${sortType}">${page}</a>
                                </li>
                            </c:forEach>
                            <c:if test="${!productPerCategory.paging.lastTab}">
                                <li class="page-item">
                                    <a class="page-link"
                                       href="${pageContext.request.contextPath}/product/search?page=${productPerCategory.paging.currentPage+1}&size=8&keyword=${keyword}&categoryId=${categoryId}&sortCol=${sortCol}&sortType=${sortType}">
                                        Next</a></li>
                            </c:if>
                        </ul>
                    </nav>
                </div>
            </c:if>
        </c:forEach>
    </div>

</div>
<script>
    function onSort(columnName, sortType) {
        let linkOrder = document.getElementById("link-order");
        linkOrder.href = `${pageContext.request.contextPath}/product/search?page=${currentPage}&size=16&keyword=${keyword}&categoryId=${categoryId}&sortCol=` + columnName + "&sortType=" + sortType;
        linkOrder.click();
    }
</script>
</body>
</html>
