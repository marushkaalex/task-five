<%@ tag pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="comment" type="com.epam.am.whatacat.model.Comment" required="true" %>
<%@ attribute name="showReply" type="java.lang.Boolean" required="true" %>
<%@ attribute name="showDelete" type="java.lang.Boolean" required="true" %>

<fmt:setBundle basename="i18n"/>
<div id="comment-${comment.id}" class="panel panel-default panel-body">
    <c:if test="${comment.parent != null}">
        <span class="glyphicon glyphicon-chevron-right"></span><i>${comment.parent.author.nickname}:
            <a class="text" href="#comment-${comment.parent.id}">${comment.parent.text}</a></i><br>
    </c:if>
    <small><i><a href="<c:url value="/user?id=${comment.author.id}"/>"><c:out
            value="${comment.author.nickname}"/></a></i></small>
    <br>
    <c:out value="${comment.text}"/><br>
    <c:if test="${showReply}">
        <button type="button" class="btn btn-info btn-xs"
                onclick="replyToComment(${comment.id}, '${comment.author.nickname}', '${comment.text}')">
            <fmt:message key="comment.reply"/>
        </button>
    </c:if>
    <c:if test="${showDelete}">
        <br><br>
        <form method="post" action="<c:url value="/moderator/delete-comment"/>">
            <input type="hidden" name="id" value="${comment.id}"/>
            <button type="submit" class="btn btn-danger btn-xs">Delete</button>
        </form>
    </c:if>
</div>