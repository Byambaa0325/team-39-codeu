package com.google.codeu.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gson.Gson;
import com.google.codeu.data.Trail;
import com.google.codeu.data.Datastore;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

@WebServlet("/trails")
public class TrailServlet extends HttpServlet {
  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
    response.setContentType("application/json");
    List<Trail> trails = datastore.getTrails();
    Gson gson = new Gson();
    String json = gson.toJson(trails);

    response.getOutputStream().println(json);
  }

  /*Accepts a POST request containing a new path.*/
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    //ArrayList<Double> coordinates = new ArrayList<>();
    String coordinates = Jsoup.clean(request.getParameter("coordinates"), Whitelist.none());
    // System.out.println(coordinates);
    // String[] inner = coordinates.split(",");
    // for (int i = 0; i < inner.length / 2; i++) {
    //   System.out.println(inner[i] + " " + inner[i + 1]);
    // }
    Trail trail = new Trail(coordinates);
    datastore.storeTrail(trail);
  }
}
