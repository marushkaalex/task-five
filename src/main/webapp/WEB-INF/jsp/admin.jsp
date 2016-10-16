<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setBundle basename="i18n"/>
<jsp:useBean id="table" scope="request" type="com.epam.am.whatacat.model.AdminTable"/>

<c:set var="usersClass" value="${type == 'users' ? 'active' : ''}"/>
<c:set var="postsClass" value="${type == 'posts' ? 'active' : ''}"/>
<my:base>
    <ul class="nav nav-tabs">
        <li class="${usersClass}"><a href="<c:url value="/admin/users"/>"><fmt:message key="admin.users.edit-user"/></a></li>
        <li class="${postsClass}"><a href="<c:url value="/admin/posts"/>"><fmt:message key="admin.posts.edit-post"/></a></li>
    </ul>
    <my:table table="${table}" paginationUrl="/admin/${type}?" />
</my:base>
