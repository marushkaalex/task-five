<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<fmt:setBundle basename="i18n"/>
<my:base>
<form method="post" class="form-horizontal">
    <my:form-input type="email"
                   name="email"
                   labelKey="register.hint.email"
                   value="${param.email}"
                   error="${errorMap.email}"/>
    <my:form-input type="text"
                   name="nickname"
                   labelKey="register.hint.nickname"
                   value="${param.nickname}"
                   error="${errorMap.nickname}"/>
    <my:form-input type="password"
                   name="password"
                   labelKey="register.hint.password"
                   error="${errorMap.password}"/>
    <my:form-input type="password"
                   name="confirmPassword"
                   labelKey="register.hint.confirm-password"
                   error="${errorMap.confirmPassword}"/>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-success"><fmt:message key="register"/></button>
        </div>
    </div>
</form>
</my:base>