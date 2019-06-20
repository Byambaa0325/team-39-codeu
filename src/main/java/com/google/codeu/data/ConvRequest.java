/*
* Single conversation request
*/
package com.google.codeu.data;

import java.util.UUID;

public class ConvRequest {
  private String to;
  private String convId;

  public ConvRequest( String to, UUID id){
    this.to = to;
    this.convId = id.toString();
  }

  public String getTo(){
    return to;
  }

  public String getConvId(){
    return convId;
  }
}