<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setBundle basename="i18n"/>
<html>
<head>
    <title>Whatacat</title>
</head>
<body>
<form method="post">
    <input name="email" placeholder="<fmt:message key="regigister.hint.email"/>"/><br/>
    <input name="nickname" placeholder="<fmt:message key="regigister.hint.nickname"/>"/><br/>
    <input name="password" type="password" placeholder="<fmt:message key="regigister.hint.password"/>"><br/>
    <input type="submit"/>
</form>
    <%--<fmt:message key="${login.error.email}"/><br><br>--%>
<c:forEach var="error" items="${errorList}">
    <c:out value="${error}"/><br>
</c:forEach>
</body>
</html>