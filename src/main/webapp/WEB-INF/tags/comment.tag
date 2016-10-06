<%@ tag pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ attribute name="comment" type="com.epam.am.whatacat.model.Comment" required="true" %>

<div>
    ${comment.author.nickname}<br>
    ${comment.text}<br>
</div>