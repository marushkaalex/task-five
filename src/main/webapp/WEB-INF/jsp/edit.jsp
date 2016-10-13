<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setBundle basename="i18n"/>
<jsp:useBean id="user" scope="request" type="com.epam.am.whatacat.model.User"/>
<my:base>

    <c:forEach var="error" items="${errorList}">
        <fmt:message key="${error}"/>
    </c:forEach>
    <form method="post" action="admin/edit/user">
        <input type="hidden" name="id" value="${user.id}" />
        <input type="text" name="nickname" value="${user.nickname}" /><br>
        <input type="email" name="email" value="${user.email}" /><br>
        <input type="number" name="rating" value="${user.rating}" /><br>
        <input type="text" name="avatarUrl" value="${user.avatarUrl}" /><br>
        <input type="datetime" name="date" value="<fmt:formatDate value="${user.registrationDate}"/>"/>
    </form>
</my:base>
