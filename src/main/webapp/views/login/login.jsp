<%--
  Created by IntelliJ IDEA.
  User: ceotungbeo
  Date: 15/8/24
  Time: 21:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>

</head>
<body>
<div class="container">
    <form class="form p-5 " method="post" action="${pageContext.request.contextPath}/login"
          onsubmit="return validateLogin()">
        <div class="container">
            <div class="row">
                <div class="col-6 offset-3 bg-dark bg-gradient rounded p-3 text-white">
                    <c:if test="${loginMessage != null && !loginMessage.isEmpty()}">
                        <div class="alert alert-danger">${loginMessage}</div>
                    </c:if>
                    <h1 class="text-center">Đăng nhập</h1>
                    <div class="form-group mb-3">
                        <label for="username">Tên đăng nhập:</label>
                        <input type="text" class="form-control" id="username" name="username"
                               placeholder="Tối thiểu 5 ký tự" required>
                    </div>
                    <div class="form-group mb-3 row">
                        <div class="col-12">
                            <label for="pwd-plain-text">Mật khẩu:</label>
                            <input type="password" class="form-control" id="pwd-plain-text"
                                   placeholder="Tối thiểu 5 ký tự" name="password"
                                   required>
                        </div>
                        <a href="${pageContext.request.contextPath}/forgot" class="col-12 link text-white text-end"
                           data-toggle="tooltip" data-placement="top" data-title="Tài khoản của bạn là: admin/admin">
                            <span>Quên mật khẩu?</span>
                        </a>
                    </div>
                    <div class="d-grid mb-3 col-12">
                        <input type="submit" value="Đăng nhập" class="btn btn-danger">
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>

<script>
    async function validateLogin() {
        let username = document.getElementById("username").value;
        if (username.length < 5) {
            window.alert("Tên đăng nhập không được nhỏ hơn 5 ký tự!");
            return false;
        }
        let passwordPlainText = document.getElementById("pwd-plain-text").value;
        if (!(passwordPlainText != null && passwordPlainText.length >= 5)) {
            window.alert("Mật không được nhỏ hơn 6 ký tự!");
        }
    }

    // async function encryptPassword() {
    //     let passwordPlainText = document.getElementById("pwd-plain-text").value;
    //     document.getElementById("pwd-encrypted").value = await sha1(passwordPlainText);
    // }
    //
    // async function sha1(message) {
    //     // Encode the message as a UTF-8 string and convert it to a byte array
    //     const msgBuffer = new TextEncoder().encode(message);
    //
    //     // Hash the message using SHA-1
    //     const hashBuffer = await crypto.subtle.digest('SHA-1', msgBuffer);
    //
    //     // Convert the hash to a hexadecimal string
    //     const hashArray = Array.from(new Uint8Array(hashBuffer));
    //     const hashHex = hashArray.map(b => b.toString(16).padStart(2, '0')).join('');
    //
    //     return hashHex;
    // }
</script>
</body>
</html>
