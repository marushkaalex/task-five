<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Whatacat</title>
</head>
<body>
<fmt:setBundle basename="i18n"/>
Message: <fmt:message key="test"/><br>
Var: ${test}
</body>
</html>
