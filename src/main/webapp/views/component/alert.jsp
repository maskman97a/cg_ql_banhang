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
<button type="button" class="btn btn-primary" id="btn-show-alert" hidden>Show live alert</button>
<div id="div-alert" class="" role="alert" style="position: fixed; top: 15px; right:15px"
     hidden>
</div>

<script>
    const alertPlaceholder = document.getElementById('div-alert')

    function showAlert(message, type) {
        document.getElementById("btn-show-alert").click();
        alertPlaceholder.hidden = false;
        appendAlert(message, type)

        setTimeout(() => {
            alertPlaceholder.hidden = true;
        }, 3000);
    }

    const appendAlert = (message, type) => {
        const wrapper = document.createElement('div')
        alertPlaceholder.innerHTML = [
            `<div class="alert alert-` + type + ` alert-dismissible" role="alert" tabindex="-1">`,
            ` <div>` + message + `</div>`,
            '   <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>',
            '</div>'
        ].join('')

    }
</script>
</body>
</html>
