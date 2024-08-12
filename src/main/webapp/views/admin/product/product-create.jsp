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
    </style>
</head>
<body>
<div class="container">

    <form class="form form-control" method="post"
          action="${pageContext.request.contextPath}/admin/product/product-create"
          enctype="multipart/form-data">
        <div class="row">
            <div class=" col-3">
            </div>
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
                            <label for="category-id">Thể loại</label>
                        </div>
                        <div class="col-9 mb-3">
                            <select class="form-control" id="category-id" name="category-id">
                                <option value="0">--Chọn thể loại--</option>
                                <c:forEach var="category" items="${lstCategory}">
                                    <option value="${category.id}">${category.name}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-3 mb-3">
                            <label for="inp-name">Tên sản phẩm</label>
                        </div>
                        <div class="col-9 mb-3">
                            <input id="inp-product-name" type="text" class="form-control" name="name"
                                   onkeyup="generateProductCode()"/>
                        </div>
                        <script>
                            function removeDiacritics(str) {
                                return str.normalize('NFD').replace(/[\u0300-\u036f]/g, '');
                            }

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
                            <input id="inp-quantity" type="number" oninput="limitLength(this)"
                                   class="form-control"
                                   name="quantity"/>
                        </div>
                        <div class="col-3 mb-3">
                            <label for="inp-price">Giá</label>
                        </div>
                        <div class="col-9 mb-3">
                            <input id="inp-price" type="number" oninput="limitLength(this)" class="form-control"
                                   name="price"/>
                        </div>
                        <div class="col-3 mb-3">
                            <label for="inp-description">Mô tả</label>
                        </div>
                        <div class="col-9 mb-3">
                            <textarea id="inp-description" class="form-control" name="description"></textarea>
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
                               href="${pageContext.request.contextPath}/admin/">Hủy
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
</body>
</html>
