package com.google.codeu.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gson.Gson;
import com.google.codeu.data.Article;
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

/**
 * Handles {@link Article} instances.
 */
@WebServlet("/article")
public class ArticleServlet extends HttpServlet {

    private Datastore datastore;

    @Override
    public void init() {
        datastore = new Datastore();
    }

    /**
     * Renders {@link Article} in html for a specific id. Responds with
     * Not Found page otherwise.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        List<Article> articles = datastore.getAllArticles();
        Gson gson = new Gson();
        String json = gson.toJson(articles);

        response.getOutputStream().println(json);
    }

    /**
     * Stores a new {@link Article}.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // UserService userService = UserServiceFactory.getUserService();
        // if (!userService.isUserLoggedIn()) {
        //     response.sendRedirect("/index.html");
        //     return;
        // }

        //String authors = userService.getCurrentUser().getEmail() + ",";
        String authors = "1234qewrt@gmail.com";
        authors += Jsoup.clean(request.getParameter("authors"), Whitelist.none());
        String tags = Jsoup.clean(request.getParameter("tags"), Whitelist.none());
        String header = Jsoup.clean(request.getParameter("header"), Whitelist.none());
        String body = Jsoup.clean(request.getParameter("body"), Whitelist.none());
        String coordinates = Jsoup.clean(request.getParameter("coordinates"), Whitelist.none());
        System.out.println("qwery : " + coordinates);
        Article article = new Article(authors, tags, header, body, coordinates);
        datastore.storeArticle(article);

        //response.sendRedirect("/article?id=" + article.getId());

    }
}
