<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title><%= request.getAttribute("header") %></title>
</head>
<body>

<h1><%= request.getAttribute("header") %></h1>
<h2><%= request.getAttribute("authors") %></h2>
<sub><%= request.getAttribute("tags") %></sub>

<main>
    <p><%= request.getAttribute("body") %></p>
</main>

<h3><%= request.getAttribute("timestamp") %></h3>
<sub><%= request.getAttribute("id") %></sub>

</body>
</html>
