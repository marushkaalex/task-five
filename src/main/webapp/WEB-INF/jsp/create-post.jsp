<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setBundle basename="i18n"/>
<my:base>
    <form method="post" id="post-form" class="form-horizontal">
        <my:form-input type="text"
                       name="title"
                       labelKey="post.hint.title"
                       value="${param.title}"
                       error="${errorMap.title}"/>
        <div class="form-group">
            <label class="control-label col-xs-2" for="content">
                <fmt:message key="post.hint.content"/>
            </label>
            <div class="col-xs-7">
                <textarea id="content" form="post-form" class="form-control" name="content" rows="10">${param.content}</textarea>
            </div>
            <div class="col-xs-3"><my:print key="${errorMap.content}"/></div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-success"><fmt:message key="post.create"/></button>
            </div>
        </div>
    </form>
    <c:forEach var="error" items="${errorList}">
        <c:out value="${error}"/><br>
    </c:forEach>
</my:base>
