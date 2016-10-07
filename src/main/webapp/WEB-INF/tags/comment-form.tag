<%@ tag pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ attribute name="post_id" type="java.lang.Long" required="true" %>
<%@ attribute name="parent_comment" type="com.epam.am.whatacat.model.Comment" required="false" %>
<fmt:setBundle basename="i18n"/>

<div class="panel panel-primary">
    <c:if test="${parent_comment != null}">
        <div class="panel-heading post-heading">
            <c:out value="${parent_comment.author.nickname}"/><br>
            <c:out value="${parent_comment.text}"/>
        </div>
    </c:if>
    <div class="panel-body">
        <form method="post" action="send-comment" class="post-rate" id="comment-form">
            <input type="hidden" name="post_id" value="${post_id}">
            <input type="hidden" name="parent_id" value="${parent_comment.id}">
            <textarea form="comment-form" name="text"
                      placeholder="<fmt:message key='comment.hint.text'/>"></textarea><br>
            <button type="submit" class="btn btn-xs"><fmt:message key="comment.send"/></button>
        </form>
    </div>
</div>