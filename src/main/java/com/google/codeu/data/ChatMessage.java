package com.google.codeu.data;

import java.util.Date;

public class ChatMessage{
  private String user;
  private String message;
  private String convid;
  private Long timestamp;
  
  public ChatMessage(String user, String message, String convid){
    this.user = user;
    this.message = message;
    this.convid = convid;
    this.timestamp = (new Date()).getTime();
  }

  public ChatMessage(String user, String message, String convid, Long timestamp){
    this.user = user;
    this.message = message;
    this.convid = convid;
    this.timestamp = timestamp;
  }

  public String getUser(){
    return user;
  }

  public String getMessage(){
    return message;
  }
  public String getConvid(){
    return convid;
  }
  public Long getTimestamp(){
    return timestamp;
  }
}