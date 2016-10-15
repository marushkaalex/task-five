<%@ tag pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="key" required="true" %>

<fmt:setBundle basename="i18n"/>
<c:if test="${key != null && key != ''}">
    <fmt:message key="${key}"/>
</c:if>
