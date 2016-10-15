<%@ tag pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>

<%@ attribute name="type" type="java.lang.String" required="true" %>
<%@ attribute name="name" type="java.lang.String" required="true" %>
<%@ attribute name="labelKey" type="java.lang.String" required="true" %>
<%@ attribute name="value" type="java.lang.String" required="false" %>
<%@ attribute name="error" type="java.lang.String" required="false" %>

<fmt:setBundle basename="i18n"/>
<div class="form-group">
    <label class="control-label col-xs-2" for="${name}">
        <fmt:message key="${labelKey}"/>
    </label>
    <div class="col-xs-7">
        <input type="${type}" class="form-control" name="${name}" id="${name}" value="${value}"/>
    </div>
    <div class="col-xs-3"><my:print key="${error}"/></div>
</div>