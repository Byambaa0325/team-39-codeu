package com.google.codeu.servlets;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Article;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Forum;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Handles fetching and saving {@link Forum} instances.
 */
@WebServlet("/forumJSON")
public class ForumServlet extends HttpServlet {

    private Datastore datastore;

    @Override
    public void init() {
        datastore = new Datastore();
    }

    /**
     * Return JSON {@link Forum. Responds with
     * error if forum is not found.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      String country = request.getParameter("country");
      Forum forum = null;
      try {
        forum = datastore.getForumByName(country);
      } catch (EntityNotFoundException e) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }
      List<Article> articles = null;
      try {
        articles = datastore.getArticlesOfForum(forum);
      } catch (EntityNotFoundException e) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }
      Gson gson = new Gson();
      String json = gson.toJson(articles);

      response.getOutputStream().println(json);

    }
}
