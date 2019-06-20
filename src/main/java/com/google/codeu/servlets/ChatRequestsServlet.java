package com.google.codeu.servlets;

import java.util.*;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Conversation;
import com.google.codeu.data.Datastore;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

@WebServlet("/chat/requests")
public class ChatRequestsServlet extends HttpServlet{
  private Datastore datastore;
  @Override
  public void init() {
    datastore = new Datastore();
  }

  private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
  
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
  * Creates new conversation and make requests for every invitee
  */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException{
    
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }

    // Extracing the invitees
    String inviteesString = request.getParameter("invitee");
    inviteesString = inviteesString.replaceAll(" ", "");
    List<String> inviteesList = new ArrayList<String>();

    for( String invitee : inviteesString.split(",") ) {
      invitee =  Jsoup.clean(invitee, Whitelist.none());
      if( invitee.matches(EMAIL_REGEX) ){
        inviteesList.add( invitee );
      }
    }

    // Nickname of the conversation cleaned
    String nicknameConv = request.getParameter("nickname");
    nicknameConv = Jsoup.clean(nicknameConv, Whitelist.none());

    // Creating current new conversation
    Conversation currConv = new Conversation(nicknameConv);
    datastore.storeConversation(currConv);

    response.getOutputStream().println(nicknameConv);
    response.getOutputStream().println(inviteesList.toString());
  }
}