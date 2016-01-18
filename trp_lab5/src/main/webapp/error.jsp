<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.Collection"%>
<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<%!String pageName = "Ошибка";%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">   		
        <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    </head>
    <body>
        <%@include file="navigation.jspf" %>
        <c:if test="${not empty exception }">
            <p class="text-danger">Oops!</p>
            <p class="text-danger">${exception.stackTrace}</p>
        </c:if>
        <c:if test="${empty exception }">
            <p class="text-danger">При выполнении запроса произошла ошибка</p>
        </c:if>
    </body>
</html>
