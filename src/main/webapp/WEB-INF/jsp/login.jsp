<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Whatacat</title>
</head>
<body>
Currently logged in with '${sessionScope.login}' <br>
<form method="post">
    <input name="login"/><br/>
    <input name="password" type="password"><br/>
    <input type="submit"/>
</form>
</body>
</html>
