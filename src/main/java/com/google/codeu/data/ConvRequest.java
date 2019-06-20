/*
* Single conversation request
*/
package com.google.codeu.data;

import java.util.UUID;

public class ConvRequest {
  private String to;
  private String from;
  private String convId;
  
  public ConvRequest( String to, String from, UUID id){
    this.to = to;
    this.from = from;
    this.convId = id.toString();
  }

  public ConvRequest( String to, String from, String id){
    this.to = to;
    this.from = from;
    this.convId = id;
  }

  public String getTo(){
    return to;
  }

  public String getFrom(){
    return from;
  }

  public String getConvId(){
    return convId;
  }
}