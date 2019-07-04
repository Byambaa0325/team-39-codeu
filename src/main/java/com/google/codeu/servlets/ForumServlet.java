package com.google.codeu.servlets;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Article;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Forum;

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
@WebServlet("/forum")
public class ForumServlet extends HttpServlet {

    private Datastore datastore;

    @Override
    public void init() {
        datastore = new Datastore();
    }

    /**
     * Renders list of all {@link Forum} in html. Responds with
     * an empty page if no article was posted.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String id = request.getParameter("id");
        Forum forum = null;
        try {
            forum = datastore.getForum(id);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return;
        }

        request.setAttribute("forum",forum);
        try {
            request.getRequestDispatcher("/Forum.jsp").forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }

    }
    /** Stores a new {@link Forum}. */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        UserService userService = UserServiceFactory.getUserService();
        if (!userService.isUserLoggedIn()) {
            response.sendRedirect("/index.html");
            return;
        }

        String[] continents = {"Africa", "America", "Asia", "Europe", "Australia"};

        List<String> testInput = Arrays.asList(continents);
        for( String continent : continents){
            List<Article> articles = datastore.getAllArticles();
            List<String> articleIds = new ArrayList<>();
            for( Article article: articles){
                articleIds.add(article.getId().toString());
            }
            Forum forum = new Forum(UUID.randomUUID(), continent, testInput,testInput,testInput,articleIds);
            datastore.storeForum(forum);
        }
    }

}
