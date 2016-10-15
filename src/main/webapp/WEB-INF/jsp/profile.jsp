<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<my:base>
    <fmt:setBundle basename="i18n"/>
    <c:set var="avatar" value="${sessionScope.user.avatarUrl}"/>
    <c:choose>
        <c:when test="${avatar == null || avatar == ''}">
            <c:set var="avatar" value="/static/noavatar.png"/>
        </c:when>
        <c:otherwise>
            <c:set var="avatar" value="${avatar = 'image/'.concat(avatar)}"/>
        </c:otherwise>
    </c:choose>

    <div class="col-xs-offset-2 col-xs-10">
        <h3><c:out value="${sessionScope.user.nickname}"/></h3>
        <p><c:out value="${sessionScope.user.email}"/></p>
        <a href="/user?id=${sessionScope.user.id}">
            <button class="btn btn-xs btn-info">
                <fmt:message key="profile.my-posts"/>
            </button>
        </a>
        <p><img src="${avatar}" width="100" height="100"/></p>
        <form method="post" action="upload/photo" enctype="multipart/form-data">
            <input type="file" name="file" accept="image/jpeg,image/png"/><br>
            <input type="submit">
        </form>
    </div>

    <form method="post" action="/profile/save" class="form-horizontal">
        <input type="hidden" name="id" value="${user.id}"/>
        <div class="form-group">
            <label class="control-label col-xs-2" for="nickname">
                <fmt:message key="admin.users.header.nickname"/>
            </label>
            <div class="col-xs-7">
                <input type="text" class="form-control" value="${user.nickname}" name="nickname" id="nickname"/>
            </div>
            <div class="col-xs-3"><my:print key="${errorMap.nickname}"/></div>
        </div>
        <div class="form-group">
            <label class="control-label col-xs-2" for="email">
                <fmt:message key="admin.users.header.email"/>
            </label>
            <div class="col-xs-7">
                <input type="text" class="form-control" value="${user.email}" name="email" id="email"/>
            </div>
            <div class="col-xs-3"><my:print key="${errorMap.email}"/></div>
        </div>
        <div class="form-group">
            <label class="control-label col-xs-2" for="gender">
                <fmt:message key="admin.users.header.gender"/>
            </label>
            <div class="col-xs-7">
                <select class="form-control" id="gender" name="gender">
                    <c:forEach items="${genders}" var="gender">
                        <option value="${gender}"
                                <c:if test="${gender == user.gender}">selected</c:if> ><fmt:message
                                key="${gender.titleKey}"/></option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="form-group">
            <div class="col-xs-offset-2 col-xs-10">
                <button type="submit" class="btn btn-success"><fmt:message key="admin.save"/></button>
                <my:print key="${errorMap.success}"/>
            </div>
        </div>
    </form>
    <br>
    <br>
    <form method="post" class="form-horizontal">
        <div class="form-group">
            <label class="control-label col-xs-2" for="oldPassword">
                <fmt:message key="profile.hint.old-password"/>
            </label>
            <div class="col-xs-7">
                <input type="password" class="form-control" name="oldPassword" id="oldPassword"/>
            </div>
            <div class="col-xs-3"><my:print key="${errorMap.oldPassword}"/></div>
        </div>
        <div class="form-group">
            <label class="control-label col-xs-2" for="newPassword">
                <fmt:message key="profile.hint.new-password"/>
            </label>
            <div class="col-xs-7">
                <input type="password" class="form-control" name="newPassword" id="newPassword"/>
            </div>
            <div class="col-xs-3"><my:print key="${errorMap.newPassword}"/></div>
        </div>
        <div class="form-group">
            <label class="control-label col-xs-2" for="retypeNewPassword">
                <fmt:message key="profile.hint.retype-new-password"/>
            </label>
            <div class="col-xs-7">
                <input type="password" class="form-control" name="retypeNewPassword" id="retypeNewPassword"/>
            </div>
            <div class="col-xs-3"><my:print key="${errorMap.retypeNewPassword}"/></div>
        </div>
        <div class="form-group">
            <div class="col-xs-offset-2 col-xs-10">
                <button type="submit" class="btn btn-success"><fmt:message key="profile.change-password"/></button>
                <my:print key="${errorMap.successPassword}"/>
            </div>
        </div>
    </form>
    <br>
</my:base>
