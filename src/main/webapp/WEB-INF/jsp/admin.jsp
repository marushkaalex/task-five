<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setBundle basename="i18n"/>
<jsp:useBean id="table" scope="request" type="com.epam.am.whatacat.model.AdminTable"/>

<c:set var="usersClass" value="${type == 'users' ? 'active' : ''}"/>
<c:set var="postsClass" value="${type == 'posts' ? 'active' : ''}"/>
<c:set var="paginationUrl" value="${type == 'moderation' ? '/moderator?' : '/admin/'.concat(type).concat('?')}"/>
<my:base>
    <c:if test="${type != 'moderation'}">
        <ul class="nav nav-tabs">
            <li class="${usersClass}"><a href="<c:url value="/admin/users"/>"><fmt:message key="admin.users"/></a></li>
            <li class="${postsClass}"><a href="<c:url value="/admin/posts"/>"><fmt:message key="admin.posts"/></a></li>
        </ul>
    </c:if>
    <my:table table="${table}" paginationUrl="${paginationUrl}" />
</my:base>
