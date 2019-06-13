package com.google.codeu.servlets;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Article;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles fetching and saving {@link Article} instances.
 */
@WebServlet("/article-feed/*")
public class ArticleFeedServlet extends HttpServlet {

    private Datastore datastore;

    @Override
    public void init() {
        datastore = new Datastore();
    }

    /**
     * Renders list of all {@link Article} in html. Responds with
     * an empty page if no article was posted.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String id = request.getParameter("id");

        List<Article> articles = datastore.getAllArticles();

        response.setContentType("text/html;");
        response.getOutputStream().println("<!DOCTYPE html>");
        response.getOutputStream().println("<html>");
        response.getOutputStream().println("<head>");
        response.getOutputStream().println("<title>Servlet HTML Example</title>");
        response.getOutputStream().println("</head>");
        response.getOutputStream().println("<body>");

        for (Article article : articles) {
            response.getOutputStream().println("<div class='article'>");

            response.getOutputStream().println("<a href = \"/article?id=" + article.getId().toString() + "\">");
            response.getOutputStream().println("<h1>");
            response.getOutputStream().println(article.getHeader());
            response.getOutputStream().println("</h1>");
            response.getOutputStream().println("</a>");

            response.getOutputStream().println("<sub>");
            response.getOutputStream().println(article.getAuthors());
            response.getOutputStream().println("</sub>");

            response.getOutputStream().println(article.getTimestamp());

            response.getOutputStream().println("</div>");
        }

        response.getOutputStream().println("</body>");
        response.getOutputStream().println("</html>");
    }
}
