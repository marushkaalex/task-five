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
    <input name="email"/><br/>
    <input name="password" type="password"><br/>
    <input type="submit"/>
</form>
    <%--<fmt:message key="${login.error.email}"/><br><br>--%>
<c:forEach var="error" items="${errorList}">
    <c:out value="${error}"/><br>
</c:forEach>
</body>
</html>
