<%@ tag pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ attribute name="table" type="com.epam.am.whatacat.model.AdminTable" required="true" %>
<%@ attribute name="paginationUrl" type="java.lang.String" required="true" %>

<fmt:setBundle basename="i18n"/>
<c:if test="${table.title != null}">
<h1><fmt:message key="${table.title}"/></h1>
</c:if>
<table class="table table-striped">
    <tr>
        <c:forEach var="column" items="${table.headers.columns}">
            <td><b><fmt:message key="${column.text}"/></b></td>
        </c:forEach>
    </tr>
    <c:forEach var="row" items="${table.rows}">
        <tr>
            <c:forEach var="column" items="${row.columns}">
                <td>
                    <c:if test="${column.url != null}">
                    <a href="${column.url}">
                        </c:if>
                        <c:choose>
                            <c:when test="${column.key}">
                                <fmt:message key="${column.text}" />
                            </c:when>
                            <c:otherwise>
                                <c:out value="${column.text}"/>
                            </c:otherwise>
                        </c:choose>
                        <c:if test="${column.url != null}">
                    </a>
                    </c:if>
                </td>
            </c:forEach>
        </tr>
    </c:forEach>
</table>
<my:pagination pageNumber="${table.page}" pageCount="${table.pageCount}" url="${paginationUrl}"/>