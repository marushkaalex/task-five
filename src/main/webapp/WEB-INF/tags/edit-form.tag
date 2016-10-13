<%@ tag pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="i18n"/>
<div class="panel panel-default">
    <form method="post" action="edit">
        <input name="type" type="hidden" value="${model.type}"/><br>
        <c:forEach items="${model.fields}" var="field">
            <fmt:message var="hint" key="${field.hintKey}"/>
            <input name="${field.name}" type="${field.type}" placeholder="${hint}" value="${field.value}"/>
            <br>
        </c:forEach>
        <input type="submit"/>
    </form>
</div>