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
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
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
                <li class="active"><a href="#">Link <span class="sr-only">(current)</span></a></li>
                <li><a href="#">Link</a></li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">Dropdown <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="#">Action</a></li>
                        <li><a href="#">Another action</a></li>
                        <li><a href="#">Something else here</a></li>
                        <li role="separator" class="divider"></li>
                        <li><a href="#">Separated link</a></li>
                        <li role="separator" class="divider"></li>
                        <li><a href="#">One more separated link</a></li>
                    </ul>
                </li>
            </ul>
            <div class="navbar-form navbar-right">
                <c:choose>
                    <c:when test="${sessionScope.user != null}">
                        <%--Welcome, ${sessionScope.user.nickname}!<br>--%>
                        <a href="/create-post">
                            <button class="btn btn-success"><fmt:message key="base.create-post"/></button>
                        </a>
                        <a href="/logout">
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
        </div>
    </div>
</nav>
<div class="container">
    <div class="row">
        <div class="col-xs-8">
            <jsp:doBody/>
        </div>
        <div class="col-xs-4">
            Message: <fmt:message key="test"/><br>
            Var: ${test}
            <c:choose>
                <c:when test="${sessionScope.user != null}">
                    Welcome, ${sessionScope.user.nickname}!<br>
                    <a href="create-post">Create post</a>
                    <a href="logout">Log out</a>
                </c:when>
                <c:otherwise>
                    <a href="login">Log in</a><br>
                    <a href="register">Register</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
</body>
</html>