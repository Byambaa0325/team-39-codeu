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
import com.google.codeu.data.ChatMessage;
import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


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
    if( request.getPathInfo().equals( "/get/conversations/" ) ){
      response.setContentType("application/json");
      String email = userService.getCurrentUser().getEmail();
      List<Conversation> conversations = datastore.getAllConversations( email );

      Gson gson = new Gson();
      response.getOutputStream().println(gson.toJson(conversations));
    }

    // Gets all chat messages of the current user's convid
    if( request.getPathInfo().equals( "/get/messages/" ) ){
      response.setContentType("application/json");
      String email = userService.getCurrentUser().getEmail();
      String convid = (String) request.getParameter("convid");
      List<ChatMessage> messages = datastore.getChatMessages( email, convid );

      Gson gson = new Gson();
      response.getOutputStream().println(gson.toJson(messages));
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
    
    JSONParser jsonParser = new JSONParser();

    if( request.getPathInfo().equals( "/new/conversation/" ) ){
      try {
        JSONObject jsonObject = (JSONObject) jsonParser.parse( request.getReader().readLine() );
        if( jsonObject.get("nickname") == null || jsonObject.get("invitee") == null ){
          return;
        }

        // Nickname of the conversation cleaned
        String nicknameConv = (String) jsonObject.get("nickname");
        nicknameConv = Jsoup.clean(nicknameConv, Whitelist.none());

        // Extracing and cleaning the invitees
        String inviteesString = (String) jsonObject.get("invitee");
        inviteesString = inviteesString.replaceAll(" ", "");
        List<String> inviteesList = new ArrayList<String>();

        for( String invitee : inviteesString.split(",") ) {
          invitee =  Jsoup.clean(invitee, Whitelist.none());
          if( invitee.matches(EMAIL_REGEX) ){
            inviteesList.add( invitee );
          }
        }

        // Storing new Conversation
        Conversation currConv = new Conversation(nicknameConv);
        datastore.storeConversation(currConv);

        // Adding all invitees and the current user to the new Conversation
        inviteesList.add( userService.getCurrentUser().getEmail() );
        for( String invitee : inviteesList ){
          datastore.addPersonToConversation(currConv, invitee);
        }

        System.out.println("Nickname : " + nicknameConv);
        System.out.println("Invitees : " + inviteesList.toString());
      } catch( ParseException e){
        e.printStackTrace();
      }
    }

    if( request.getPathInfo().equals( "/new/message/" ) ){
      try {
        JSONObject jsonObject = (JSONObject) jsonParser.parse( request.getReader().readLine() );
        System.out.println( jsonObject.toString() );
        if( jsonObject.get("convid") == null || jsonObject.get("message") == null ){
          return;
        }

        String userEmail = userService.getCurrentUser().getEmail();
        String convid = (String) jsonObject.get("convid");
        String msg = (String) jsonObject.get("message");

        if( datastore.checkIfConversationIsPublic(convid) == false && datastore.checkUserIsInConversation(userEmail, convid) == false ){
          System.out.println("Wrong conversation.");
          return;
        }

        msg = Jsoup.clean( msg, Whitelist.none() );
        if( msg.length() == 0 ){
          System.out.println("Empty message, skipping");
          return;
        }

        System.out.println("Storing " + convid + " " + msg);
        datastore.storeChatMessage( new ChatMessage( userEmail, msg, convid ) );
      } catch( ParseException e){
        e.printStackTrace();
      }
    }
  }
}