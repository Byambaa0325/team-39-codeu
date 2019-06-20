/*
* Chat Conversation
*/
package com.google.codeu.data;

import java.util.UUID;

public class Conversation {
  private String nickname;
  private Long latestTime;
  private UUID id;

  public Conversation( String nickname ){
    this.nickname = nickname;
    this.latestTime = System.currentTimeMillis();
    this.id = UUID.randomUUID();
  }

  public String getNickname(){
    return nickname;
  }

  public Long getLatestTime(){
    return latestTime;
  }

  public UUID getId(){
    return id;
  }
}