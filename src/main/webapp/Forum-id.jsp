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
<%  String id = request.getParameter("id");
    Forum forum = null;
    try {
        forum = datastore.getForum(id);
        System.out.println("found forum");
    } catch (EntityNotFoundException e) {
        e.printStackTrace();
        return;
    }
    List<Article> articles = null;
    try {
        articles = datastore.getArticlesOfForum(forum);
    } catch (EntityNotFoundException e) {
        e.printStackTrace();
    }%>
<head>


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
