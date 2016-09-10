<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setBundle basename="i18n"/>
<my:base>
<form method="post">
    <input name="title"/><br/>
    <input name="content"/><br/>
    <input type="submit"/>
</form>
    <%--<fmt:message key="${login.error.email}"/><br><br>--%>
<c:forEach var="error" items="${errorList}">
    <c:out value="${error}"/><br>
</c:forEach>
</my:base>
