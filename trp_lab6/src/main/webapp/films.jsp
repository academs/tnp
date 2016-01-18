<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml"  prefix="x" %>

<%@ page contentType="text/html" pageEncoding="UTF-8" errorPage="error.jsp"%>
<%! String pageName = "Фильмы";%>
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
            <form action="${pageContext.request.contextPath}/films/find" method="GET" id="seachFilmForm" role="form">
                <div class="form-group col-xs-5">
                    <input type="text" name="filmTitle" class="form-control" 
                           value="${filmTitle}"
                           required="true" placeholder="Введите часть названия" />
                </div>
                <button type="submit" class="btn btn-info">
                    <span class="glyphicon glyphicon-search" /> Поиск
                </button>
            </form>
            <form action="${pageContext.request.contextPath}/films/new" method="GET">
                <br />
                <button type="submit" class="btn btn-primary  btn-md">Добавить</button> 
            </form>
            <%@include file="messages.jspf" %>
            <c:import url="films.xsl" var="filmsXSLT" />
            <x:transform xml="${foundFilm}" xslt="${filmsXSLT}">
                <x:param name="contextPath" value="${pageContext.request.contextPath}" />
            </x:transform>
        </div>
    </body>
</html>