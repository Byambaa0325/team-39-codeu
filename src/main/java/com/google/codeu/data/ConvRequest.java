/*
* Single conversation request
*/
package com.google.codeu.data;

import java.util.UUID;

public class ConvRequest {
  private String to;
  private UUID id;

  public ConvRequest( String to, UUID id){
    this.to = to;
    this.id = id;
  }

  public String getTo(){
    return to;
  }

  public UUID getId(){
    return id;
  }

  public String getIdAsString(){
    return id.toString();
  }
}