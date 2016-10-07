<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<my:base>
    <fmt:setBundle basename="i18n"/>
    <my:post post="${post}"/>
    <c:forEach items="${comments}" var="comment">
        <my:comment comment="${comment}"/>
    </c:forEach>
    <my:comment-form post_id="${post.id}"/>
</my:base>
