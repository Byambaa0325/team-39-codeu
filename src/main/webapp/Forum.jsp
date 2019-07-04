<%@ page import="com.google.codeu.data.Forum" %>
<%@ page import="com.google.codeu.data.Datastore" %>
<%@ page import="com.google.codeu.data.Article" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Byambaa Bayarmandakh
  Date: 7/4/2019
  Time: 5:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% Forum forum = (Forum) request.getAttribute("forum");%>
<% Datastore datastore = new Datastore();%>
<% List<Article> articles = datastore.getArticlesOfForum(forum);%>
<head>
    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">
    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>
    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">
    <link rel="stylesheet" href="css/forum.css">
    <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script>

    <title><%=forum.getTitle()%></title>
</head>
<body>
<h1><%=forum.getTitle()%></h1>
<hr>
<div class="container">
    <div id="article-container">
        <% for (Article article : articles){%>
            <div class="article">
                <a href = "article?id=<%=article.getId().toString()%>">
                    <h3><%= article.getHeader()%></h3>
                </a>

                <sub><%=article.getAuthors()%></sub>
                <%=article.getTimestamp()%>
            </div>
        <%}%>
    </div>
    <div id = "chat-container"></div>
</div>

</body>
</html>
