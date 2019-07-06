<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
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
      <ul class="nav navbar-nav navbar-right">
        <li><a href="/">Home</a></li>
        <li><a href="/aboutus.html">About us</a></li>
        <li><a href="/forums">Forums</a></li>
        <li><a href="/article-feed">Articles</a></li>
        <li><a href="/explore.html">Explore</a></li>
        <li><a href="/chat">Chat</a></li>
        <%UserService userService = UserServiceFactory.getUserService();
          if (userService.isUserLoggedIn()) {
        %>
          <li><a href="/logout">Logout</a></li>
        <% } else {   %>
          <li><a href="/login">Login</a></li>
        <% } %>
      </ul>
    </div><!--End of navbar collapse--->
  </div><!---End of container-->
</nav><!-- end of nav-->