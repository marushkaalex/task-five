<%@ tag pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="i18n"/>

<html>
<head>
    <title><fmt:message key="base.title"/></title>
    <link href="<c:url value="/static/my.css"/>" rel="stylesheet">
    <script src="<c:url value="/webjars/jquery/1.11.1/jquery.js"/>"></script>
    <script src="<c:url value="/webjars/bootstrap/3.3.7/js/bootstrap.js"/>"></script>
    <script src="<c:url value="/static/my.js"/>"></script>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <!-- Brand and toggle base grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                    data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/"><fmt:message key="base.title"/></a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <c:if test="${sessionScope.user.role == 'ADMIN'}">
                    <li><a href="<c:url value="/admin"/>"><fmt:message key="base.admin"/><span
                            class="sr-only">(current)</span></a></li>
                </c:if>
                <c:if test="${sessionScope.user.role == 'ADMIN' || sessionScope.user.role == 'MODERATOR'}">
                    <li><a href="<c:url value="/moderator"/>"><fmt:message key="base.moderator"/><span class="sr-only">(current)</span></a>
                    </li>
                </c:if>
                <form method="post" action="<c:url value="/set-locale"/>" id="locale-form"></form>
                <div class="btn-group">
                    <button class="btn btn-xs btn-default" form="locale-form" value="en" name="locale" type="submit">
                        <fmt:message
                                key="base.lang.en"/></button>
                    <button class="btn btn-xs btn-primary" form="locale-form" value="ru" name="locale" type="submit">
                        <fmt:message
                                key="base.lang.ru"/></button>
                </div>
            </ul>
            <div class="navbar-form navbar-right">
                <c:choose>
                    <c:when test="${sessionScope.user != null}">
                        <a href="<c:url value="/create-post"/>">
                            <button class="btn btn-success"><fmt:message key="base.create-post"/></button>
                        </a>
                        <a href="<c:url value="/logout"/>">
                            <button class="btn btn-success"><fmt:message key="base.log-out"/></button>
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a href="/login">
                            <button class="btn btn-success"><fmt:message key="base.sign-in"/></button>
                        </a>
                        <a href="/register">
                            <button class="btn btn-success"><fmt:message key="base.sign-up"/></button>
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
            <c:if test="${sessionScope.user != null}">
                <ul class="nav navbar-nav navbar-right">
                    <li><a class="navbar-header" href="/profile">${sessionScope.user.nickname}</a></li>
                </ul>
            </c:if>
        </div>
    </div>
</nav>
<div class="container">
    <jsp:doBody/>
</div>
</body>
</html>
<c:if test="${sessionScope.errorMap != null && !sessionScope.errorMap.isEmpty()}">
    <c:set var="errorMapShown" value="true" scope="session"/>
</c:if>