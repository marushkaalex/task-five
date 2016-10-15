<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setBundle basename="i18n"/>

<my:base>
    <form method="post" action="login" class="form-horizontal">
        <input type="hidden" value="${param.fromUrl}" name="fromUrl"/>
        <my:form-input type="email" name="email" labelKey="login.hint.email" value="${email}"
                       error="${sessionScope.errorMap.email}"/>
        <my:form-input type="password" name="password" labelKey="login.hint.password"
                       error="${sessionScope.errorMap.password}"/>
        <div class="col-xs-offset-2 col-xs-10">
            <button type="submit" class="btn btn-success"><fmt:message key="login.sign-in"/></button><my:print key="${sessionScope.errorMap.error}"/>
        </div>
    </form>
    <br>
</my:base>
