package com.google.codeu.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@WebServlet("/chat/requests")
public class ChatRequestsServlet extends HttpServlet{
  /*
  * Returns current user's conversation requests
  */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException{
    
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    } 
  }

  /*
  * Creates new conversation requests for every invitee
  */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException{
    
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    } 
  }
}