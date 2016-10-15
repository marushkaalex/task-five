<%@ taglib  prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setBundle basename="i18n"/>
<%--<fmt:setLocale value="$Э--%>
<my:base>
    <form method="post" action="login">
        <input type="hidden" value="${param.fromUrl}" name="fromUrl" />
        <input name="email" class="form-control form-group form-group-sm input-sm" type="text"
               placeholder="<fmt:message key="login.hint.email"/>">
        <input name="password" class="form-group form-group-sm form-control input-sm" type="password"
               placeholder="<fmt:message key="login.hint.password"/>">
        <button type="submit" class="btn btn-success btn-sm"><fmt:message key="login.sign-in"/></button>
    </form><br>
<a href="register">Register</a>
    <%--<fmt:message key="${login.error.email}"/><br><br>--%>
<c:forEach var="error" items="${errorList}">
    <c:out value="${error}"/><br>
    <fmt:message key="${error}" />
</c:forEach>
</my:base>
