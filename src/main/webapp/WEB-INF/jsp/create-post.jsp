<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setBundle basename="i18n"/>
<my:base>
    <form method="post" id="post-form">
        <input name="title" placeholder="<fmt:message key='post.hint.title'/>"/><br/>
        <textarea form="post-form" name="content" placeholder="<fmt:message key='post.hint.content'/>"></textarea><br>
        <input type="submit"/>
    </form>
    <c:forEach var="error" items="${errorList}">
        <c:out value="${error}"/><br>
    </c:forEach>
</my:base>
