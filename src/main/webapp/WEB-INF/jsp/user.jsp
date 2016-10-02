<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<my:base>
    <fmt:setBundle basename="i18n"/>
    <p><img src="image/${user.avatarUrl}" width="100" height="100"/></p>
    <p>${user.nickname}</p>
    <p>${user.email}</p>
    <c:forEach var="error" items="${errorList}">
        <fmt:message key="${error}"/><br>
    </c:forEach>
</my:base>
