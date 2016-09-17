<%@ tag pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ attribute name="post" type="com.epam.am.whatacat.model.Post" required="true" %>
<%@ attribute name="user" type="com.epam.am.whatacat.model.User" required="false" %>

<div class="panel panel-primary">
    <div class="panel-heading post-heading">
        <c:if test="${sessionScope.user != null}">
            <form method="post" action="rate-post" class="post-rate">
                <input type="hidden" name="id" value="${post.userPostRating.id}">
                <input type="hidden" name="post_id" value="${post.id}">
                <input type="hidden" name="delta" value="1">
                <button type="submit" class="btn btn-default btn-xs">+</button>
            </form>
        </c:if>
        ${post.rating}
        <c:if test="${sessionScope.user != null}">
            <form method="post" action="rate-post" class="post-rate">
                <input type="hidden" name="id" value="${post.userPostRating.id}">
                <input type="hidden" name="post_id" value="${post.id}">
                <input type="hidden" name="delta" value="-1">
                <button type="submit" class="btn btn-default btn-xs">-</button>
            </form>
        </c:if>
        <h3><a href="post?id=${post.id}">${post.title}</a></h3>
    </div>
    <div class="panel-body">
        ${post.content}
    </div>
</div>