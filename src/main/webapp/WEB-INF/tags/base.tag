<%@ tag pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<fmt:setBundle basename="i18n"/>

<html>
<head>
    <title><fmt:message key="base.title"/></title>
    <link href="<c:url value="/webjars/bootstrap/3.3.7/css/bootstrap.css"/>" rel="stylesheet">
</head>
<body>
<jsp:doBody/>
</body>
</html>