<%@ page import="info.pionas.jspexample.UserDto" %>
<%@ page import="java.util.List" %>
<%! int index; %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Hey ${name}!</title>
    </head>

    <body>
        <h2>Hey ${name}!</h2>

        <h3>Users with JSP</h3>
        <ul>
        <%for (index = 0; index < ((List)request.getAttribute("users")).size(); index++){ %>
            <li><%= ((UserDto)((List)request.getAttribute("users")).get(index)).getName() %></li>
        <%}%>
        </ul>

        <h3>Users with JSTL</h3>
        <ul>
            <c:forEach var="user" items="${users}">
                <li>${user.email}</li>
            </c:forEach>
        </ul>
    </body>
</html>