<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setBundle basename="i18n"/>
<jsp:useBean id="table" scope="request" type="com.epam.am.whatacat.model.AdminTable"/>
<my:base>

    <c:forEach var="error" items="${errorList}">
        <fmt:message key="${error}"/>
    </c:forEach>
    <my:table table="${table}" paginationUrl="/admin?type=user&" />
</my:base>
