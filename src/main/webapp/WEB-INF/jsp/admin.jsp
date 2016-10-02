<%@ taglib  prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setBundle basename="i18n"/>
<my:base>
    <c:forEach var="item" items="${items}">
        <c:out value="${item}"/><br>
    </c:forEach>
<a href="register">Register</a>
</my:base>
