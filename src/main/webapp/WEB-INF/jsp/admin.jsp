<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setBundle basename="i18n"/>
<jsp:useBean id="table" scope="request" type="com.epam.am.whatacat.model.AdminTable"/>
<my:base>

    <c:forEach var="error" items="${errorList}">
        <fmt:message key="${error}"/>
    </c:forEach>
    <h1><fmt:message key="${table.title}"/></h1>
    <table class="table table-striped">
        <tr>
            <c:forEach var="column" items="${table.headers.columns}">
                <td><b><fmt:message key="${column.text}"/></b></td>
            </c:forEach>
        </tr>
        <c:forEach var="row" items="${table.rows}">
            <tr>
                <c:forEach var="column" items="${row.columns}">
                    <td><c:out value="${column.text}"/></td>
                </c:forEach>
            </tr>
        </c:forEach>
    </table>
    <c:forEach var="item" items="${items}">
        <c:out value="${item}"/><br>
    </c:forEach>
</my:base>
