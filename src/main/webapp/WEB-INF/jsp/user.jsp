<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<jsp:useBean id="user" scope="request" type="com.epam.am.whatacat.model.User"/>
<jsp:useBean id="postList" scope="request" type="com.epam.am.whatacat.model.AdminTable"/>
<fmt:setBundle basename="i18n"/>

<my:base>
    <c:set var="avatar" value="${user.avatarUrl}" />
    <c:choose>
        <c:when test="${avatar == null || avatar == ''}">
            <c:set var="avatar" value="/static/noavatar.png"/>
        </c:when>
        <c:otherwise>
            <c:set var="avatar" value="${avatar = 'image/'.concat(avatar)}"/>
        </c:otherwise>
    </c:choose>
    <p><img src="${avatar}" width="100" height="100"/></p>
    <p><c:out value="${user.nickname}"/> (<fmt:message key="user.rating"/>: ${user.rating})</p>
    <p><c:out value="${user.email}"/></p>
    <my:table table="${postList}" paginationUrl="/user?id=${user.id}&"/>
    <c:forEach var="error" items="${errorList}">
        <fmt:message key="${error}"/><br>
    </c:forEach>
</my:base>
