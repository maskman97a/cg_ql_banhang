<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
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
    <title>Cập nhật sản phẩm</title>
    <style>
        #preview {
            max-width: 100%;
            max-height: 300px;
            margin-top: 20px;
        }

        .error {
            color: red;
            font-size: 0.875em;
        }
    </style>
</head>
<body>
<div class="container">
    <form class="form form-control" method="post"
          action="${pageContext.request.contextPath}/admin/product/update"
          enctype="multipart/form-data" onsubmit="return validateFormUpdate()">
        <input type="text" class="form-control" name="id"
               value="${product.id}"
               hidden/>
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
            <div class=" col-3">
            </div>
            <div class=" col-6">
                <div class="row">
                    <div class="col-12 text-center">
                        <h1>Cập nhật sản phẩm</h1>
                    </div>
                    <div class="row col-12 align-items-center mb-3">
                        <div class="col-3 mb-3">
                            <label for="file">Chọn ảnh:</label>
                        </div>
                        <div class="col-9">
                            <input type="file" id="file" name="${product.imageUrl}"
                                   accept="multipart/form-data"/>
                            <br>
                            <img src="${pageContext.request.contextPath}/image/${product.imageUrl}"
                                 id="product-image-${product.id}"
                                 class="img-fluid col-12"
                                 alt="${product.name}" width="100%" height="100%">
                            <div id="file-error-update" class="error"></div>
                        </div>
                        <script>
                            document.getElementById('file').addEventListener('change', function (event) {
                                const file = event.target.files[0];
                                if (file) {
                                    const reader = new FileReader();
                                    reader.onload = function (e) {
                                        const preview = document.getElementById('preview');
                                        preview.src = e.target.result;
                                        preview.style.display = 'block';
                                    }
                                    reader.readAsDataURL(file);
                                }
                            });
                        </script>
                        <div class="col-3 mb-3">
                            <label for="inp-category-name-update">Thể loại</label>
                        </div>
                        <div class="col-9">
                            <input type="hidden" class="form-control" name="category-id"
                                   id="category-id-update"
                                   value="${product.categoryId}"/>
                            <input id="inp-category-name-update" type="text" value="${product.categoryName}"
                                   class="form-control"
                                   disabled
                                   name="category-name"/>
                            <div id="category-id-error-update" class="error"></div>
                        </div>
                        <div class="col-3 mb-3">
                            <label for="inp-code">Mã sản phẩm</label>
                        </div>
                        <div class="col-9">
                            <input id="inp-code" type="text" readonly value="${product.productCode}"
                                   class="form-control" name="code"/>
                        </div>
                        <div class="col-3 mb-3">
                            <label for="inp-name-update">Tên sản phẩm</label>
                        </div>
                        <div class="col-9">
                            <input id="inp-name-update" type="text" value="${product.productName}"
                                   class="form-control"
                                   name="name"/>
                            <div id="product-name-error-update" class="error"></div>
                        </div>
                        <script>
                            function limitLength(input) {
                                if (input.value.length > 15) {
                                    input.value = input.value.slice(0, 15);
                                }
                            }
                            function removeDiacritics(str) {
                                return str.normalize('NFD').replace(/[\u0300-\u036f]/g, '');
                            }
                        </script>
                        <div class="col-3 mb-3">
                            <label for="inp-price-update">Giá</label>
                        </div>
                        <div class="col-9">
                            <input id="inp-price-update" type="number" oninput="limitLength(this)"
                                   class="form-control"
                                   value="${product.price}"
                                   name="price"/>
                            <div id="price-error-update" class="error"></div>
                        </div>
                        <div class="col-3 mb-3">
                            <label for="inp-description-update">Mô tả</label>
                        </div>
                        <div class="col-9">
                                <textarea id="inp-description-update" class="form-control"
                                          maxlength="500"
                                          name="description">${product.description}</textarea>
                            <div id="description-error" class="error"></div>
                        </div>
                        <script>
                            function limitLength(input) {
                                if (input.value.length > 15) {
                                    input.value = input.value.slice(0, 15);
                                }
                            }
                        </script>
                    </div>
                    <div class="row col-12">
                        <div class="col-3">
                        </div>
                        <div class="col-3  d-grid gap-2">
                            <a class="btn btn-secondary"
                               href="${pageContext.request.contextPath}/admin/product">Hủy
                            </a>
                        </div>
                        <div class="col-6  d-grid gap-2">
                            <input type="submit" class="btn btn-primary" value="Lưu"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class=" col-3">
            </div>
        </div>
    </form>
</div>
<script>
    function validateFormUpdate() {
        let isValid = true;
        // Validate product name
        const productName = document.getElementById('inp-name-update').value.trim();
        const productNameError = document.getElementById('product-name-error-update');
        if (productName === '' || productName.isEmpty()) {
            productNameError.textContent = 'Vui lòng nhập tên sản phẩm.';
            return false;
        } else {
            productNameError.textContent = '';
        }

        // Validate quantity
        // const quantity = document.getElementById('inp-quantity-update').value;
        // const quantityError = document.getElementById('quantity-error-update');
        // if (quantity === '' || parseInt(quantity) <= 0) {
        //     quantityError.textContent = 'Số lượng không hợp lệ. Giá trị hợp lệ là số nguyên dương!';
        //     return false;
        // } else {
        //     quantityError.textContent = '';
        // }

        // Validate price
        const price = document.getElementById('inp-price-update').value;
        const priceError = document.getElementById('price-error-update');
        if (price === '' || parseInt(price) <= 0) {
            priceError.textContent = 'Số lượng không hợp lệ. Giá trị hợp lệ là số nguyên dương!';
            return false;
        } else {
            priceError.textContent = '';
        }

        // Validate description
        const description = document.getElementById('inp-description-update').value.trim();
        const descriptionError = document.getElementById('description-error');
        if (description !== '' && description.toString().length > 500) {
            descriptionError.textContent = 'Mô tả nhập quá 500 ký tự';
            return false;
        } else {
            descriptionError.textContent = '';
        }

        return isValid;
    }

    document.getElementById('file').addEventListener('change', function (event) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                const preview = document.getElementById('preview');
                preview.src = e.target.result;
                preview.style.display = 'block';
            }
            reader.readAsDataURL(file);
        }
    });
</script>
</body>
</html>
