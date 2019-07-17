<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Date" %>
<%@ page errorPage="error404.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <% Date date = new Date((long)request.getAttribute("timestamp"));
  %>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  <script defer src="https://use.fontawesome.com/releases/v5.0.6/js/all.js"></script>
    <title><%= request.getAttribute("header") %></title>
</head>
<body>

<h1><%= request.getAttribute("header") %></h1>
<sub>Authors: <%= request.getAttribute("authors") %></sub><br/>
<i><sub>Tags: <%= request.getAttribute("tags") %></sub></i>
<hr>
<div class = "container">
<main>
    <%= request.getAttribute("body") %>
</main>
</div>

<h6>Posted on: <%= date.toString() %></h6>

</body>
</html>
