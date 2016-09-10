<%@ taglib  prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setBundle basename="i18n"/>
<%--<fmt:setLocale value="$Э--%>
<html>
<head>
    <title>Whatacat</title>
</head>
<body>
<form method="post">
    <input name="email"/><br/>
    <input name="password" type="password"><br/>
    <input type="submit"/>
</form><br>
<a href="register">Register</a>
    <%--<fmt:message key="${login.error.email}"/><br><br>--%>
<c:forEach var="error" items="${errorList}">
    <c:out value="${error}"/><br>
    <fmt:message key="${error}" />
</c:forEach>
</body>
</html>
