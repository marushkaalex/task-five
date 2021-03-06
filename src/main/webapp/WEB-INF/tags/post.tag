<%@ tag pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="post" type="com.epam.am.whatacat.model.Post" required="true" %>
<%@ attribute name="user" type="com.epam.am.whatacat.model.User" required="false" %>
<fmt:setBundle basename="i18n"/>

<c:set var="plus_class" value="${post.userPostRating.ratingDelta > 0 ? 'btn-success' : 'btn-default'}"/>
<c:set var="minus_class" value="${post.userPostRating.ratingDelta < 0 ? 'btn-danger' : 'btn-default'}"/>
<c:if test="${post.status != 'ALLOWED'}">
    <c:set var="statusKey" value="${post.status.titleKey}"/>
</c:if>
<div class="panel panel-primary">
    <div class="panel-heading post-heading">
        <c:if test="${sessionScope.user != null && post.status == 'ALLOWED'}">
            <form method="post" action="<c:url value="/rate-post"/>" class="post-rate">
                <input type="hidden" name="id" value="${post.userPostRating.id}">
                <input type="hidden" name="post_id" value="${post.id}">
                <input type="hidden" name="delta" value="1">
                <button type="submit" class="btn btn-xs ${plus_class}">+</button>
            </form>
        </c:if>
        ${post.rating}
        <c:if test="${sessionScope.user != null && post.status == 'ALLOWED'}">
            <form method="post" action="<c:url value="/rate-post"/>" class="post-rate">
                <input type="hidden" name="id" value="${post.userPostRating.id}">
                <input type="hidden" name="post_id" value="${post.id}">
                <input type="hidden" name="delta" value="-1">
                <button type="submit" class="btn btn-xs ${minus_class}">-</button>
            </form>
        </c:if>
        <h3><a href="post?id=${post.id}"><c:out value="${post.title}"/></a>
        </h3>
        <c:if test="${statusKey != null}">
            (<fmt:message key="${statusKey}"/>)
        </c:if>
        <a href="user?id=${post.author.id}"><i><c:out value="${post.author.nickname}"/></i></a>
    </div>
    <div class="panel-body">
        ${post.content}
    </div>
</div>