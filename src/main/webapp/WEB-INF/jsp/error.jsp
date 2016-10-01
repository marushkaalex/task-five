<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<my:base>
    <fmt:setBundle basename="i18n"/>
    <h1>Error</h1>
    <img src="static/${pageContext.errorData.statusCode}.jpg"/>
</my:base>
