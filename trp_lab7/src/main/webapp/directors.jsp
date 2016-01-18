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
            <br />
            <form action="${pageContext.request.contextPath}/directors/find" method="GET" id="seachDirectorForm" role="form">
                <div class="form-group col-xs-5">
                    <input type="text" name="directorName" id="directorName" class="form-control" 
                           value="${directorName}"
                           required="true" placeholder="Введите имя режиссёра" />
                </div>
                <button type="submit" class="btn btn-info">
                    <span class="glyphicon glyphicon-search" />Поиск
                </button>
            </form>
            <form action="${pageContext.request.contextPath}/directors/new" method="GET">
                <br />
                <button type="submit" class="btn btn-primary  btn-md">Добавить</button> 
            </form>
            <%@include file="messages.jspf" %>
            <c:import url="directors.xsl" var="directorsXSLT" />
            <x:transform xml="${foundDirector}" xslt="${directorsXSLT}">
                <x:param name="contextPath" value="${pageContext.request.contextPath}" />
            </x:transform>
        </div>
    </body>
</html>