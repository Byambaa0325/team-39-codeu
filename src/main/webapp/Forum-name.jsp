<%@ page import="com.google.codeu.data.Forum" %>
<%@ page import="com.google.codeu.data.Datastore" %>
<%@ page import="com.google.codeu.data.Article" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.google.appengine.api.datastore.EntityNotFoundException" %>
<%@ page errorPage="error404.jsp" %>
<%--
  Created by IntelliJ IDEA.
  User: Byambaa Bayarmandakh
  Date: 7/4/2019
  Time: 5:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% Datastore datastore = new Datastore();%>
<%  String country = request.getParameter("country").replace("%20"," ");
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
  <link rel="stylesheet" href="css/landing-style.css">
  <script src="js/navigation-loader.js"></script>
    <title><%=forum.getTitle()%></title>
</head>
<body onload="addLoginOrLogoutLinkToNavigation();">
  <nav class="navbar navbar-inverse">
    <div class="container-fluid">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a href="#" class="navbar-brand"></a>
      </div><!---End of navbar header-->
      <div class="collapse navbar-collapse" id="myNavbar">
        <ul id="navigation" class="nav navbar-nav navbar-right">
          <li><a href="index.html">Home</a></li>
          <li><a href="explore.html">Explore</a></li>
        </ul>
      </div><!--End of navbar collapse--->
    </div><!---End of container-->
  </nav><!-- end of nav-->
<div id="forum-title"><h1><%=forum.getTitle().toUpperCase()%></h1></div>
<hr>
<form action="/createTrail.html" method="GET" id="AddArticle">
<input type="hidden" name="forum" value="<%= forum.getTitle()%>">
<button type = "submit" form="AddArticle" value="Submit"><h4>+Add Article</h4></button>
</form>
<div class="container">
    <div id="article-container">
      <% if(!articles.isEmpty()){
      for (Article article : articles){
        Date date = new Date((long)article.getTimestamp());%>
          <div <% if(article.getCoords().length() != 0) {%> class="article"<%} else{%> class="post"<%}%>>
              <a href = "article?id=<%=article.getId().toString()%>">
                  <h2><%= article.getHeader()%></h2>
              </a>
              <div class="details">
              <b class="flex-item">Posted by: <a href="user-page.html?user=<%=article.getAuthors().split(",")[0]%>"><%=article.getAuthors().split(",")[0]%></a></b>
              <i class="flex-item">Date: <%=date.toString()%></i>
            </div>
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
