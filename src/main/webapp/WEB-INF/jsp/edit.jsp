<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setBundle basename="i18n"/>
<jsp:useBean id="user" scope="request" type="com.epam.am.whatacat.model.User"/>
<my:base>

    <c:forEach var="error" items="${errorList}">
        <fmt:message key="${error}"/>
    </c:forEach>
    <h3 class="text-center"><fmt:message key="admin.users.edit-user"/></h3>
    <form method="post" action="admin/edit/user" class="form-horizontal">
        <input type="hidden" name="id" value="${user.id}"/>
        <my:form-input
                type="text"
                name="nickname"
                labelKey="admin.users.header.nickname"
                value="${user.nickname}"
                error="${sessionScope.errorMap.nickname}" />
        <my:form-input
                type="email"
                name="email"
                labelKey="admin.users.header.email"
                value="${user.email}"
                error="${sessionScope.errorMap.email}" />
        <my:form-input
                type="number"
                name="rating"
                labelKey="admin.users.header.rating"
                value="${user.rating}"
                error="${sessionScope.errorMap.rating}" />
        <div class="form-group">
            <label class="control-label col-xs-2" for="role">
                <fmt:message key="admin.users.header.gender"/>
            </label>
            <div class="col-xs-7">
                <select class="form-control" id="gender" name="gender">
                    <c:forEach items="${genders}" var="gender">
                        <option <c:if test="${gender == user.gender}">selected</c:if> ><fmt:message key="${gender.titleKey}"/></option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-xs-2" for="role">
                <fmt:message key="admin.users.header.role"/>
            </label>
            <div class="col-xs-7">
                <select class="form-control" id="role" name="role">
                    <c:forEach items="${roles}" var="role">
                        <option <c:if test="${role == user.role}">selected</c:if> ><fmt:message key="${role.titleKey}"/></option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-success"><fmt:message key="admin.save"/></button>
            </div>
        </div>
    </form>
</my:base>
