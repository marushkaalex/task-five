<%@ tag pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ attribute name="comment" type="com.epam.am.whatacat.model.Comment" required="true" %>
<%@ attribute name="showReply" type="java.lang.Boolean" required="true" %>

<fmt:setBundle basename="i18n"/>
<div id="comment-${comment.id}" class="panel panel-default">
    <c:if test="${comment.parent != null}">
        <i>${comment.parent.author.nickname}: <a href="#comment-${comment.parent.id}">${comment.parent.text}</a></i><br>
    </c:if>
    ${comment.author.nickname}<br>
    ${comment.text}<br>
    <c:if test="${showReply}">
        <button type="button" class="btn btn-info btn-xs" onclick="replyToComment(${comment.id}, '${comment.text}')">
            <fmt:message key="comment.reply"/>
        </button>
    </c:if>
</div>