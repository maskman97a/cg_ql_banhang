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
    <title>Thêm mới sản phẩm</title>
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
          action="${pageContext.request.contextPath}/admin/product/product-create"
          enctype="multipart/form-data" onsubmit="return validateForm()">
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
            <div class=" col-3"></div>
            <div class=" col-6">
                <div class="row">
                    <div class="col-12 text-center">
                        <h1>Thêm mới sản phẩm</h1>
                    </div>
                    <div class="row col-12 align-items-center mb-3">
                        <div class="col-3 mb-3">
                            <label for="file">Chọn ảnh:</label>
                        </div>
                        <div class="col-9 mb-3">
                            <input type="file" id="file" name="file" accept="multipart/form-data"/>
                            <br>
                            <img id="preview" src="#" alt="Image Preview" style="display:none;">
                            <div id="file-error" class="error"></div>
                        </div>
                        <div class="col-3 mb-3">
                            <label for="category-id">Thể loại</label>
                        </div>
                        <div class="col-9 mb-3">
                            <select class="form-control" id="category-id" name="category-id">
                                <option value="0">--Chọn thể loại--</option>
                                <c:forEach var="category" items="${lstCategory}">
                                    <option value="${category.id}">${category.name}</option>
                                </c:forEach>
                            </select>
                            <div id="category-id-error" class="error"></div>
                        </div>

                        <div class="col-3 mb-3">
                            <label for="inp-product-name">Tên sản phẩm</label>
                        </div>
                        <div class="col-9 mb-3">
                            <input id="inp-product-name" type="text" class="form-control" name="name"
                                   onkeyup="generateProductCode()"/>
                            <div id="product-name-error" class="error"></div>
                        </div>
                        <script>
                            function generateProductCode() {
                                const name = document.getElementById('inp-product-name').value.trim().toUpperCase();
                                let newProductCode = removeDiacritics(name);
                                newProductCode = newProductCode.replaceAll(" ", "_");
                                document.getElementById('inp-code').value = newProductCode;
                            }
                        </script>
                        <div class="col-3 mb-3">
                            <label for="inp-code">Mã sản phẩm</label>
                        </div>
                        <div class="col-9 mb-3">
                            <input id="inp-code" type="text" class="form-control" name="code" readonly/>
                        </div>
                        <div class="col-3 mb-3">
                            <label for="inp-quantity">Số lượng</label>
                        </div>
                        <div class="col-9 mb-3">
                            <input id="inp-quantity" type="number" oninput="limitLength(this)" class="form-control"
                                   min="0"
                                   value="0" name="quantity"/>
                            <div id="quantity-error" class="error"></div>
                        </div>
                        <div class="col-3 mb-3">
                            <label for="inp-price">Giá</label>
                        </div>
                        <div class="col-9 mb-3">
                            <input id="inp-price" type="number" oninput="limitLength(this)" class="form-control" min="0"
                                   value="0" name="price"/>
                            <div id="price-error" class="error"></div>
                        </div>
                        <div class="col-3 mb-3">
                            <label for="inp-description">Mô tả</label>
                        </div>
                        <div class="col-9 mb-3">
                            <textarea id="inp-description" maxlength="500" class="form-control" name="description"
                                      style="width: 100%; height: 200px;"></textarea>
                            <div id="description-error" class="error"></div>
                        </div>
                    </div>
                    <div class="row col-12">
                        <div class="col-3"></div>
                        <div class="col-3  d-grid gap-2">
                            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/product">Hủy</a>
                        </div>
                        <div class="col-6  d-grid gap-2">
                            <input type="submit" class="btn btn-primary" value="Lưu"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class=" col-3"></div>
        </div>
    </form>
</div>
<script>
    function validateForm() {
        let isValid = true;

        // Validate file input
        const fileInput = document.getElementById('file');
        const fileError = document.getElementById('file-error');
        if (fileInput.files.length === 0) {
            fileError.textContent = 'Vui lòng chọn một ảnh.';
            return false;
        } else {
            fileError.textContent = '';
        }

        // Validate category
        const categoryId = document.getElementById('category-id').value;
        const categoryError = document.getElementById('category-id-error');
        if (categoryId === 0) {
            categoryError.textContent = 'Vui lòng chọn thể loại.';
            return false;
        } else {
            categoryError.textContent = '';
        }

        // Validate product name
        const productName = document.getElementById('inp-product-name').value.trim();
        const productNameError = document.getElementById('product-name-error');
        if (productName === '') {
            productNameError.textContent = 'Vui lòng nhập tên sản phẩm.';
            return false;
        } else {
            productNameError.textContent = '';
        }

        // Validate quantity
        const quantity = document.getElementById('inp-quantity').value;
        const quantityError = document.getElementById('quantity-error');
        if (quantity === '' || parseInt(quantity) < 0) {
            quantityError.textContent = 'Số lượng không hợp lệ. Giá trị hợp lệ là số nguyên dương!';
            return false;
        } else {
            quantityError.textContent = '';
        }

        // Validate price
        const price = document.getElementById('inp-price').value;
        const priceError = document.getElementById('price-error');
        if (price === '' || parseInt(price) <= 0) {
            priceError.textContent = 'Số lượng không hợp lệ. Giá trị hợp lệ là số nguyên dương!';
            return false;
        } else {
            priceError.textContent = '';
        }

        // Validate description
        const description = document.getElementById('inp-description').value.trim();
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
