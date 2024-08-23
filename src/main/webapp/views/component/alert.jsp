<%--
  Created by IntelliJ IDEA.
  User: ceotungbeo
  Date: 7/8/24
  Time: 08:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<div id="div-alert" class="" role="alert" style="position: fixed; top: 160px; right:15px; opacity: 0">
    <div class="alert alert-danger alert-dismissible" role="alert" tabindex="-1">
        <div id="alert-message"></div>
        <button type="button" class="btn-close" data-dismiss="alert" aria-label="Đóng"></button>
    </div>
</div>

<script>
    const alertPlaceholder = document.getElementById('div-alert')

    function showAlert(message, type) {
        alertPlaceholder.style.opacity = '1';
        document.getElementById("alert-message").innerHTML = message
        fadeOut(alertPlaceholder, 100)
        return false;
    }

    function fadeOut(element, duration) {
        let opacity = 1;
        let timer = setInterval(() => {
            element.style.opacity = opacity;
            opacity -= 0.05;
            if (opacity <= 0) {
                clearInterval(timer);
            }
        }, duration);

    }
</script>
</body>
</html>
