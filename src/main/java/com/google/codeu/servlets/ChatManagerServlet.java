package com.google.codeu.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.codeu.data.Conversation;
import com.google.codeu.data.Datastore;
import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

@WebServlet("/chat/*")
public class ChatManagerServlet extends HttpServlet{
  private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
  private Datastore datastore;
  

  @Override
  public void init(){
    datastore = new Datastore();
  }
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException{
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn() ) {
      response.sendRedirect("/index.html");
      return;
    }

    // Gets all conversations of the current user
    if( request.getPathInfo().equals( "/conversations/" ) ){
      String email = userService.getCurrentUser().getEmail();
      Gson gson = new Gson();
      List<Conversation> conversations = datastore.getAllConversations( email );

      response.getOutputStream().print(gson.toJson(conversations));
    }
  }

  @Override 
  public void doPost( HttpServletRequest request, HttpServletResponse response)
    throws IOException{
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn() ) {
      response.sendRedirect("/index.html");
      return;
    }

    if( request.getPathInfo().equals( "/new/" ) ){
      if( request.getParameter("nickname") == null || request.getParameter("invitee") == null ){
        return;
      }
      // Extracing and cleaning the invitees
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

      // Storing new Conversation
      Conversation currConv = new Conversation(request.getParameter("nickname"));
      datastore.storeConversation(currConv);

      // Adding all invitees and the current user to the new Conversation
      inviteesList.add( userService.getCurrentUser().getEmail() );
      for( String invitee : inviteesList ){
        datastore.addPersonToConversation(currConv, invitee);
      }
    }
  }
}