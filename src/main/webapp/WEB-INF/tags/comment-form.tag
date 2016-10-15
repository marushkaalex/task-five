<%@ tag pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ attribute name="post_id" type="java.lang.Long" required="true" %>
<%@ attribute name="parent_comment" type="com.epam.am.whatacat.model.Comment" required="false" %>
<fmt:setBundle basename="i18n"/>

<div class="panel panel-success">
    <div class="panel-heading"><fmt:message key="comment.leave-comment"/></div>
    <c:if test="${parent_comment != null}">
        <div class="panel-heading post-heading">
            <c:out value="${parent_comment.author.nickname}"/><br>
            <c:out value="${parent_comment.text}"/>
        </div>
    </c:if>
    <div class="panel-body">
        <p><i id="comment-parent-text"></i>
            <button class="btn btn-xs btn-danger" id="comment-parent-clear" style="display:none;"
                    onclick="clearParentComment()"><span class="glyphicon glyphicon-remove"></span>
            </button>
        </p>
        <form method="post" action="send-comment" class="post-rate" id="comment-form">
            <input type="hidden" name="post_id" value="${post_id}">
            <input id="comment-parent-id" type="hidden" name="parent_id" value="${parent_comment.id}">
            <textarea form="comment-form" class="form-control" name="text"
                      placeholder="<fmt:message key='comment.hint.text'/>"></textarea><br>
            <button type="submit" class="btn btn-info btn-xs"><fmt:message key="comment.send"/></button>
        </form>
    </div>
</div>