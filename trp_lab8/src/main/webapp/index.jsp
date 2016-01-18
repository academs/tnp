<%! String pageName = "Главная";%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">   		
        <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>       
        <title>Лабораторный проект</title>
    </head>
    <body>
        <div class="container">
            <%@include file="navigation.jspf" %>
        </div>
    </body>
</html>
