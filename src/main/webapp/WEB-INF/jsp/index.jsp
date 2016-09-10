<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<my:base>
    <fmt:setBundle basename="i18n"/>
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
    <c:forEach items="${postList}" var="item">
        <h3>${item.title}</h3>
        <p>${item.content}</p>
    </c:forEach>
</my:base>
