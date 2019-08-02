<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<script defer src="https://use.fontawesome.com/releases/v5.0.6/js/all.js"></script>
<link rel="stylesheet" href="/css/main.css">
<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
      </button>
      <a href="#" class="navbar-brand"></a>
    </div><!---End of navbar header-->
    <div class="collapse navbar-collapse" id="myNavbar">
      <ul class="nav navbar-nav navbar-right">
        <li><a href="/">Home</a></li>
        <li><a href="/forums">Forums</a></li>
        <li><a href="/explore.html">Explore</a></li>
        <li><a href="/chat">Chat</a></li>
        <%UserService userService = UserServiceFactory.getUserService();
          if (userService.isUserLoggedIn()) {
        %>
          <li><a href="/user-page.html?user=<%=userService.getCurrentUser().getEmail()%>">Your Page</a></li>
          <li><a href="/logout">Logout</a></li>
        <% } else {   %>
          <li><a href="/login">Login</a></li>
        <% } %>
      </ul>
    </div><!--End of navbar collapse--->
  </div><!---End of container-->
</nav><!-- end of nav-->
