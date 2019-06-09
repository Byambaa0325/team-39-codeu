package com.google.codeu.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Article;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/** Handles fetching and saving {@link Article} instances. */
@WebServlet("/article")
public class ArticleServlet extends HttpServlet {
    
    private Datastore datastore;

    @Override
    public void init() {
        datastore = new Datastore();
    }

    /**
     * Responds with a JSON representation of {@link Article} data for a specific user. Responds with
     * an empty array if the user is not provided.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");

        String user = request.getParameter("user");

        if (user == null || user.equals("")) {
            // Request is invalid, return empty array
            response.getWriter().println("[]");
            return;
        }

        String id = request.getParameter("id");

        List<Article> articles = datastore.getArticleById(id);
        Gson gson = new Gson();
        String json = gson.toJson(articles);

        response.getWriter().println(json);
    }

    /** Stores a new {@link Article}. */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        UserService userService = UserServiceFactory.getUserService();
        if (!userService.isUserLoggedIn()) {
            response.sendRedirect("/index.html");
            return;
        }

        String authors = Jsoup.clean(request.getParameter("authors"), Whitelist.none());
        String header = Jsoup.clean(request.getParameter("header"), Whitelist.none());
        String body = Jsoup.clean(request.getParameter("body"), Whitelist.none());

        Article article = new Article(authors,header, body);
        datastore.storeArticle(article);

        if("user-page.html".equals(request.getParameter("source-page"))){
            response.sendRedirect("/user-page.html?user=" + userService.getCurrentUser().getEmail());
        }
        else{
            response.sendRedirect("/"+request.getParameter("source-page"));
        }
    }
}
