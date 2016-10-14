<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<jsp:useBean id="postList" scope="request" type="com.epam.am.whatacat.model.PaginatedList"/>
<my:base>
    <fmt:setBundle basename="i18n"/>
    <c:forEach items="${postList}" var="item">
        <my:post post="${item}"/>
    </c:forEach>
    <my:pagination pageNumber="${postList.getPage()}" pageCount="${postList.getPageCount()}" url="/?"/>
</my:base>
