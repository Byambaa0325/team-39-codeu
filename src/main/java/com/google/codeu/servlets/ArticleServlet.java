package com.google.codeu.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Article;
import com.google.codeu.data.Datastore;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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

        String id = request.getParameter("id");

        List<Article> articles = datastore.getArticleById(id);

        if (articles.size() == 0) {
            response.getOutputStream().println("<h1>");
            response.getOutputStream().println("Article Not Found");
            response.getOutputStream().println("</h1>");
            return;
        }

        Article article = articles.get(0);

        request.setAttribute("id", article.getId());
        request.setAttribute("authors", article.getAuthors());
        request.setAttribute("tags", article.getTags());
        request.setAttribute("header", article.getHeader());
        request.setAttribute("body", article.getBody());
        request.setAttribute("timestamp", article.getTimestamp());
        request.setAttribute("coordinates", article.getCoords());

        try {
            request.getRequestDispatcher("/article.jsp").forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }

    }

    /**
     * Stores a new {@link Article}.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

         UserService userService = UserServiceFactory.getUserService();
         if (!userService.isUserLoggedIn()) {
             response.sendRedirect("/index.html");
             return;
         }

        String authors = userService.getCurrentUser().getEmail();
        if(!request.getParameter("authors").equals("")) {
             authors += ","+Jsoup.clean(request.getParameter("authors"), Whitelist.none());
        }
        String tags = Jsoup.clean(request.getParameter("tags"), Whitelist.none());
        String header = Jsoup.clean(request.getParameter("header"), Whitelist.none());
        String body = Jsoup.clean(request.getParameter("body"), Whitelist.none());
        String coordinates = Jsoup.clean(request.getParameter("coordinates"), Whitelist.none());
        System.out.println(body + coordinates);
        Article article = new Article(authors, tags, header, body, coordinates);
        String id = article.getId().toString();
        datastore.storeArticle(article);

        if(request.getParameter("forum") != null){
          datastore.updateFieldForum(request.getParameter("forum").replace("%20"," "),"articleIds",id,false);
        }
        else{
          datastore.updateFieldForum("","articleIds",id,true);
        }

        

    }
}
