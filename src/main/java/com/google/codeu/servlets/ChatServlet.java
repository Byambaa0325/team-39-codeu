package com.google.codeu.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet{
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException{

    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      String googleLoginUrl = userService.createLoginURL("/login?redirectBack=/chat");
      response.sendRedirect(googleLoginUrl);
      return;
    }

    request.getRequestDispatcher("/chat.jsp").forward(request,response);
  }
}
