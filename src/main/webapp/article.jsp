<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  <script defer src="https://use.fontawesome.com/releases/v5.0.6/js/all.js"></script>
    <title><%= request.getAttribute("header") %></title>
</head>
<body>

<h1><%= request.getAttribute("header") %></h1>
<h2><%= request.getAttribute("authors") %></h2>
<sub><%= request.getAttribute("tags") %></sub>
<hr>
<div class = "container">
<main>
    <p><%= request.getAttribute("body") %></p>
</main>
</div>

<h3><%= request.getAttribute("timestamp") %></h3>
<sub><%= request.getAttribute("id") %></sub>

</body>
</html>
