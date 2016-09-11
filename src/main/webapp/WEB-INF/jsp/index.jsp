<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<my:base>
    <fmt:setBundle basename="i18n"/>
    <div class="row">
        <div class="col-md-8">
            <c:forEach items="${postList}" var="item">
                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <form method="post" action="rate-post">
                            <input type="hidden" name="id" value="${item.userPostRating.id}">
                            <input type="hidden" name="post_id" value="${item.id}">
                            <input type="hidden" name="delta" value="1">
                            <button type="submit" class="btn btn-default btn-xs">+</button>
                        </form>
                            ${item.rating}
                        <form method="post" action="rate-post">
                            <input type="hidden" name="id" value="${item.userPostRating.id}">
                            <input type="hidden" name="post_id" value="${item.id}">
                            <input type="hidden" name="delta" value="-1">
                            <button type="submit" class="btn btn-default btn-xs">-</button>
                        </form>
                            ${item.title}
                    </div>
                    <div class="panel-body">
                            ${item.content}
                    </div>
                </div>
            </c:forEach>
        </div>
        <div class="col-md-4">
            Message: <fmt:message key="test"/><br>
            Var: ${test}
            <c:choose>
                <c:when test="${sessionScope.user != null}">
                    Welcome, ${sessionScope.user.nickname}!<br>
                    <a href="create-post">Create post</a>
                    <a href="logout">Log out</a>
                </c:when>
                <c:otherwise>
                    <a href="login">Log in</a><br>
                    <a href="register">Register</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</my:base>
