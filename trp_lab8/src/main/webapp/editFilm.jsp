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
            <h2>Редактирование</h2>
            <form action="${pageContext.request.contextPath}/films/save" method="POST" role="form">
                <input type="hidden" name="id" value="${editFilmEntity.id}" />
                <br />
                <div class="form-group col-xs-4">
                    <%@include file="messages.jspf" %>
                    <label for="title" class="control-label col-xs-4">Название:</label>
                    <input type="text" name="title" class="form-control" 
                           value="${not empty param.title ? param.title : editFilmEntity.title}" required="true" />
                    <label for="genre" class="control-label col-xs-4">Жанр:</label>
                    <select type="text" name="genre" class="form-control"
                            value="${editFilmEntity.genre eq null ? '' : editFilmEntity.genre.name}" 
                            required="true">
                        <c:forEach var="g" items="${genres}">
                            <c:choose>
                                <c:when test="${g eq editFilmEntity.genre}">
                                    <option value="${g}" selected="selected">${g.name}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${g}">${g.name}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                    <label for="year" class="control-label col-xs-4">Год:</label>
                    <input type="text" name="year" class="form-control"
                           value="${not empty param.year ? param.year : editFilmEntity.year}" />
                    <label for="duration" class="control-label col-xs-4">Длительность:</label>
                    <input type="text" name="duration" class="form-control"
                           value="${not empty param.duration ? param.duration : editFilmEntity.duration}" />
                    <label for="director" class="control-label col-xs-4">Режиссёр</label>
                    <select type="text" name="director" class="form-control"
                            value="${editFilmEntity.directorName}" required="true">
                        <c:forEach var="d" items="${directors}">
                            <c:choose>
                                <c:when test="${d eq editFilmEntity.idDirector}">
                                    <option value="${d.idDirector}" selected="selected">${d.name}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${d.idDirector}">${d.name}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                    <br />
                    <button type="submit" class="btn btn-primary  btn-md">Сохранить</button>
                </div>
            </form>
        </div>
    </body>
</html>