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
        <div class="form-group">
            <label class="control-label col-xs-2" for="nickname">
                <fmt:message key="admin.users.header.nickname"/>
            </label>
            <div class="col-xs-10">
                <input type="text" class="form-control" value="${user.nickname}" name="nickname" id="nickname"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-xs-2" for="email">
                <fmt:message key="admin.users.header.email"/>
            </label>
            <div class="col-xs-10">
                <input type="text" class="form-control" value="${user.email}" name="email" id="email"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-xs-2" for="nickname">
                <fmt:message key="admin.users.header.rating"/>
            </label>
            <div class="col-xs-10">
                <input type="number" class="form-control" value="${user.rating}" name="rating" id="rating"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-xs-2" for="role">
                <fmt:message key="admin.users.header.role"/>
            </label>
            <div class="col-xs-10">
                <select class="form-control" id="role" name="role">
                    <c:forEach items="${roles}" var="role">
                        <option <c:if test="${role == user.role}">selected</c:if> >${role}</option>
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
