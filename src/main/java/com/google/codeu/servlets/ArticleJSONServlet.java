package com.google.codeu.servlets;

import com.google.codeu.data.Article;
import com.google.codeu.data.Datastore;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Handles {@link Article} instances.
 */
@WebServlet("/articleJSON")
public class ArticleJSONServlet extends HttpServlet {

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
        List<Article> articles = datastore.getArticleById(request.getParameter("id"));
        Gson gson = new Gson();
        String json = gson.toJson(articles.get(0));

        response.getOutputStream().println(json);


    }
}
