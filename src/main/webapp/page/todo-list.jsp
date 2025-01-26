<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link href="../css/styles.css" rel="stylesheet">

    <style>
        <%@include file="/css/styles.css"%>
    </style>
</head>
<body>

<h1> Hello <%=request.getSession().getAttribute("username")%> !</h1>

<div class="header-buttons">
    <form action="about-me" method="GET">
        <button type="submit" id="button-about-me">About me</button>
    </form>
    <form action="logout" method="POST">
        <button type="submit" id="logout-button">Logout</button>
    </form>
</div>

<form action="todo" method="POST">
    <input type="text" id="input-task" name="task" placeholder="Enter the task" required>
    <button type="submit" id="task-btn">Add task</button>
</form>

<h2>Task list:</h2>

<c:if test="${tasks == null || tasks.isEmpty()}">
    <h4>There are no active tasks!</h4>
</c:if>

<ol>
    <c:forEach var="task" items="${tasks}">
        <li>
            <span id="task-text">${task}</span>
            <form action="todo" method="POST">
                <input type="hidden" name="deletedTask" value="${task}">
                <input type="submit" value="Delete" id="delete-button">
            </form>
        </li>
    </c:forEach>
</ol>
</body>
</html>