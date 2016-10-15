<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<my:base>
    <fmt:setBundle basename="i18n"/>
    <c:set var="avatar" value="${sessionScope.user.avatarUrl}" />
    <c:choose>
        <c:when test="${avatar == null || avatar == ''}">
            ${avatar = '/static/noavatar.png'}
        </c:when>
        <c:otherwise>
            ${avatar = 'image/'.concat(avatar)}
        </c:otherwise>
    </c:choose>
    <p><img src="${avatar}" width="100" height="100"/></p>
    <p>${sessionScope.user.nickname}</p>
    <p>${sessionScope.user.email}</p>
    <a href="/user?id=${sessionScope.user.id}"><fmt:message key="profile.my-posts"/></a>
    <form method="post">
        <input name="old" type="password" placeholder="<fmt:message key="profile.hint.old-password"/>"/><br>
        <input name="new" type="password" placeholder="<fmt:message key="profile.hint.new-password"/>"/><br>
        <input type="submit"/>
    </form><br>
    <form method="post" action="upload/photo" enctype="multipart/form-data">
        <input type="file" name="file" accept="image/jpeg,image/png"/><br>
        <input type="submit">
    </form>
    <c:forEach var="error" items="${errorList}">
        <fmt:message key="${error}"/><br>
    </c:forEach>
</my:base>
