<%--
  Created by IntelliJ IDEA.
  User: ceotungbeo
  Date: 5/8/24
  Time: 20:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%--<h1><c:out value="${message.equals('') || message == null ? 'Lỗi hệ thống': message}"/></h1>--%>
<h1>
    <c:choose>
        <c:when test="${empty message}">
            Lỗi hệ thống
        </c:when>
        <c:otherwise>
            <c:out value="${message}"/>
        </c:otherwise>
    </c:choose>
</h1>
</body>
</html>
