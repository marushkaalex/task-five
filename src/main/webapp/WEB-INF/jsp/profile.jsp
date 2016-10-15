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
        <my:form-input type="text"
                       name="nickname"
                       labelKey="admin.users.header.nickname"
                       value="${sessionScope.user.nickname}"
                       error="${sessionScope.errorMap.nickname}"/>
        <my:form-input type="email"
                       name="email"
                       labelKey="admin.users.header.email"
                       value="${sessionScope.user.email}"
                       error="${sessionScope.errorMap.email}"/>
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
        <my:form-input type="password"
                       name="oldPassword"
                       labelKey="profile.hint.old-password"
                       error="${sessionScope.errorMap.oldPassword}"/>
        <my:form-input type="password"
                       name="newPassword"
                       labelKey="profile.hint.new-password"
                       error="${sessionScope.errorMap.newPassword}"/>
        <my:form-input type="password"
                       name="retypeNewPassword"
                       labelKey="profile.hint.retype-new-password"
                       error="${sessionScope.errorMap.retypeNewPassword}"/>
        <div class="form-group">
            <div class="col-xs-offset-2 col-xs-10">
                <button type="submit" class="btn btn-success"><fmt:message key="profile.change-password"/></button>
                <my:print key="${sessionScope.errorMap.successPassword}"/>
            </div>
        </div>
    </form>
    <br>
</my:base>