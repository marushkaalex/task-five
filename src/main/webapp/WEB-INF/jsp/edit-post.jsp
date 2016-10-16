<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setBundle basename="i18n"/>
<my:base>
    <h3 class="text-center"><fmt:message key="admin.posts.edit-post"/></h3>
    <form method="post" id="post-form" class="form-horizontal">
        <my:form-input type="text"
                       name="title"
                       labelKey="post.hint.title"
                       value="${post.title}"
                       error="${errorMap.title}"/>
        <div class="form-group">
            <label class="control-label col-xs-2" for="content">
                <fmt:message key="post.hint.content"/>
            </label>
            <div class="col-xs-7">
                <textarea id="content" form="post-form" class="form-control" name="content" rows="10">${post.content}</textarea>
            </div>
            <div class="col-xs-3"><my:print key="${errorMap.content}"/></div>
        </div>
        <my:form-input
                type="number"
                name="rating"
                labelKey="admin.posts.header.rating"
                value="${post.rating}"
                error="${sessionScope.errorMap.rating}"/>
        <div class="form-group">
            <label class="control-label col-xs-2" for="status">
                <fmt:message key="admin.posts.header.status"/>
            </label>
            <div class="col-xs-7">
                <select class="form-control" id="status" name="status">
                    <c:forEach items="${statuses}" var="status">
                        <option value="${status}"
                                <c:if test="${status == post.status}">selected</c:if> ><fmt:message
                                key="${status.titleKey}"/></option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-success"><fmt:message key="admin.save"/></button><my:print key="${errorMap.success}"/>
            </div>
        </div>
    </form>
    <br>
    <form method="post" action="<c:url value="/admin/delete-post"/>" class="form-horizontal">
        <input type="hidden" name="id" value="${post.id}"/>
        <div class="form-group">
            <label class="control-label col-xs-2">
                <fmt:message key="admin.posts.delete"/>
            </label>
            <div class="col-xs-7">
                <button type="submit" class="btn btn-danger"><fmt:message key="admin.delete"/></button>
            </div>
        </div>
    </form>
</my:base>
