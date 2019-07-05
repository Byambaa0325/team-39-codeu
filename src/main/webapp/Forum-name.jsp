<%@ page import="com.google.codeu.data.Forum" %>
<%@ page import="com.google.codeu.data.Datastore" %>
<%@ page import="com.google.codeu.data.Article" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.datastore.EntityNotFoundException" %><%--
  Created by IntelliJ IDEA.
  User: Byambaa Bayarmandakh
  Date: 7/4/2019
  Time: 5:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% Datastore datastore = new Datastore();%>
<%  String country = request.getParameter("country");
    Forum forum = null;
    try {
        forum = datastore.getForumByName(country);
    } catch (EntityNotFoundException e) {
        e.printStackTrace();
    }
    List<Article> articles = null;
    try {
        articles = datastore.getArticlesOfForum(forum);
    } catch (EntityNotFoundException e) {
        e.printStackTrace();
    }%>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  <script defer src="https://use.fontawesome.com/releases/v5.0.6/js/all.js"></script>
  <link rel="stylesheet" href="css/forum.css">
    <title><%=forum.getTitle()%></title>
</head>
<body>
<h1><%=forum.getTitle()%></h1>
<hr>
<div class="container">
    <div id="article-container">
      <% if(!articles.isEmpty()){
      for (Article article : articles){%>
          <div class="article">
              <a href = "article?id=<%=article.getId().toString()%>">
                  <h3><%= article.getHeader()%></h3>
              </a>

              <sub><%=article.getAuthors()%></sub>
              <%=article.getTimestamp()%>
          </div>
      <%}}
      else{%>
        <h1>No Posts Here Yet!</h1>
      <%}%>
    </div>
    <div id = "chat-container"></div>
</div>

</body>
</html>
