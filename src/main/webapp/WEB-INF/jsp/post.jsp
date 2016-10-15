<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<jsp:useBean id="post" scope="request" type="com.epam.am.whatacat.model.Post"/>
<my:base>
    <fmt:setBundle basename="i18n"/>
    <my:post post="${post}"/>
    <c:set var="role" value="${sessionScope.user.role}"/>
    <c:if test="${role == 'MODERATOR' || role == 'ADMIN'}">
        <form method="post" action="<c:url value="/moderator/moderate"/>" id="moderatorForm">
            <input type="hidden" name="id" value="${post.id}"/>
        </form>
        <b><fmt:message key="post.moderate"/></b>
        <c:if test="${post.status == 'ON_MODERATION' || post.status == 'DENIED'}">
            <button class="btn btn-success" form="moderatorForm" value="allow" name="decision" type="submit">
                <fmt:message
                        key="post.moderate.allow"/></button>
        </c:if>
        <c:if test="${post.status == 'ON_MODERATION' || post.status == 'ALLOWED'}">
            <button class="btn btn-danger" form="moderatorForm" value="deny" name="decision" type="submit">
                <fmt:message
                        key="post.moderate.deny"/></button>
        </c:if>

        <br>
        <br>
    </c:if>
    <c:choose>
        <c:when test="${post.status == 'ALLOWED'}">
            <c:if test="${sessionScope.user != null}">
                <c:set var="allowComment" value="${true}"/>
            </c:if>
        </c:when>
        <c:otherwise>
            <c:if test="${sessionScope.user.role == 'MODERATOR' || sessionScope.user.role == 'ADMIN'}">
                <c:set var="allowComment" value="${true}"/>
            </c:if>
        </c:otherwise>
    </c:choose>
    <c:forEach items="${comments}" var="comment">
        <my:comment comment="${comment}" showReply="${allowComment}"/>
    </c:forEach>
    <%--<c:choose>--%>
        <%--<c:when test="${post.status == 'ALLOWED'}">--%>
            <%--<c:if test="${sessionScope.user != null}">--%>
                <%--<my:comment-form post_id="${post.id}"/>--%>
            <%--</c:if>--%>
        <%--</c:when>--%>
        <%--<c:otherwise>--%>
            <%--<c:if test="${sessionScope.user.role == 'MODERATOR' || sessionScope.user.role == 'ADMIN'}">--%>
                <%--<my:comment-form post_id="${post.id}"/>--%>
            <%--</c:if>--%>
        <%--</c:otherwise>--%>
    <%--</c:choose>--%>
    <c:if test="${allowComment}">
        <my:comment-form post_id="${post.id}"/>
    </c:if>
</my:base>
