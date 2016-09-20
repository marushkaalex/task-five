<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<my:base>
    <fmt:setBundle basename="i18n"/>
    <p>${sessionScope.user.nickname}</p>
    <p>${sessionScope.user.email}</p>
    <form method="post">
        <input name="old" type="password" placeholder="<fmt:message key="profile.hint.old-password"/>"/><br>
        <input name="new" type="password" placeholder="<fmt:message key="profile.hint.new-password"/>"/><br>
        <input type="submit"/>
    </form><br>
    <c:forEach var="error" items="${errorList}">
        <fmt:message key="${error}"/><br>
    </c:forEach>
</my:base>
