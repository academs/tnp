<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml"  prefix="x" %>

<%@ page contentType="text/html" pageEncoding="UTF-8" errorPage="error.jsp"%>
<%! String pageName = "Режиссёры";%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">   		
        <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>       
    </head>

    <body>
        <div class="container">
            <%@include file="navigation.jspf" %>
            <h2>Редактирование данных</h2>
            <form action="${pageContext.request.contextPath}/directors/save" method="POST" role="form">
                <input type="hidden" name="id" value="${editDirectorEntity.id}" />
                <br />
                <div class="form-group col-xs-4">
                    <%@include file="messages.jspf" %>
                    <label for="name" class="control-label col-xs-4">Имя:</label>
                    <input type="text" name="name" class="form-control" 
                           value="${editDirectorEntity.name}" required="true" />
                    <label for="phone" class="control-label col-xs-4">Телефон:</label>                   
                    <input type="text" name="phone" class="form-control"
                           value="${editDirectorEntity.phone}" />
                    <br />
                    <button type="submit" class="btn btn-primary  btn-md">Сохранить</button>
                </div>
            </form>
        </div>
    </body>
</html>