/*
* Chat Conversation
*/
package com.google.codeu.data;

import java.util.UUID;
import com.google.codeu.data.Datastore;

public class Conversation {
  private String nickname;
  private Long latestTime;
  private UUID id;

  public Conversation( String nickname ){
    this.nickname = nickname;
    this.latestTime = System.currentTimeMillis();
    this.id = UUID.randomUUID();
  }

  public Conversation( String nickname, Long latestTime, String id ){
    this.nickname = nickname;
    this.latestTime = latestTime;
    this.id = UUID.fromString(id);
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

  public String getIdAsString(){
    return id.toString();
  }
}