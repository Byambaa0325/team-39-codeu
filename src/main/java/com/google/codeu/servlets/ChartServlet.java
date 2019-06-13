package com.google.codeu.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.util.Scanner;


@WebServlet("/chart")
public class ChartServlet extends HttpServlet {
  
  private JsonArray hikingParticipantsArray;

  private static class hikingParticipants {
    int year;
    Double participants;

    private hikingParticipants(int year, Double participants){
      this.year = year;
      this.participants = participants;
    }
  }

  /*
  * Reading chart's CSV file from WEB-INF
  */
  @Override
  public void init(){
    hikingParticipantsArray = new JsonArray();
    Gson gson = new Gson();
    Scanner scanner = new Scanner(getServletContext().getResourceAsStream("/WEB-INF/hiking_participants.csv"));
    scanner.nextLine();
    while( scanner.hasNextLine() ){
      String line = scanner.nextLine();
      String[] cells = line.split(",");

      int curYear = Integer.valueOf(cells[0]);
      Double curParticipants = Double.valueOf(cells[1]);

      hikingParticipantsArray.add( gson.toJsonTree( new hikingParticipants(curYear, curParticipants) ) );
    }
    scanner.close();
  }

  /*
  * Serving CSV char as JSON
  */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.getOutputStream().println(hikingParticipantsArray.toString());
  }
}