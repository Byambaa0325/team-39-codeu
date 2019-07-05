package com.google.codeu.servlets;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Article;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Forum;
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

        String title = Jsoup.clean(request.getParameter("title"), Whitelist.none());

        String keywordString = Jsoup.clean(request.getParameter("keywords"), Whitelist.none());
        List<String> keywords = Arrays.asList(keywordString.split(","));

        String owner = userService.getCurrentUser().getEmail();
        List<String> owners = new ArrayList<>();
        owners.add(owner);

        List<Article> articles = datastore.getAllArticles();
        List<String> articleIds = new ArrayList<>();
        for (Article article : articles) {
            articleIds.add(article.getId().toString());
        }

        Forum forum = new Forum(UUID.randomUUID(), title, owners, new ArrayList<>(), keywords, articleIds);
        datastore.storeForum(forum);
    }

}
