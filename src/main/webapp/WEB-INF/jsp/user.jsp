<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<my:base><jsp:useBean id="user" scope="request" type="com.epam.am.whatacat.model.User"/>

    <fmt:setBundle basename="i18n"/>
    <p><img src="image/${user.avatarUrl}" width="100" height="100"/></p>
    <p><c:out value="${user.nickname}"/> (<fmt:message key="user.rating"/>: ${user.rating})</p>
    <p><c:out value="${user.email}"/></p>
    <c:forEach var="error" items="${errorList}">
        <fmt:message key="${error}"/><br>
    </c:forEach>
</my:base>
