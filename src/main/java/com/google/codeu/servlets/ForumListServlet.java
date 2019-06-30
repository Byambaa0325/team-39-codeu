package com.google.codeu.servlets;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Forum;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Handles fetching and saving {@link Forum} instances.
 */
@WebServlet("/forums")
public class ForumListServlet extends HttpServlet {

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
        List<Forum> forums = datastore.getAllForumList();

        response.setContentType("text/html;");
        response.getOutputStream().println("<!DOCTYPE html>");
        response.getOutputStream().println("<html>");
        response.getOutputStream().println("<head>");
        response.getOutputStream().println("<title>Servlet HTML Example</title>");
        response.getOutputStream().println("</head>");
        response.getOutputStream().println("<body>");

        for (Forum forum: forums) {
            response.getOutputStream().println("<div class='forum'>");

            response.getOutputStream().println("<a href = \"/forum?id=" + forum.getId().toString() + "\">");
            response.getOutputStream().println("<h1>");
            response.getOutputStream().println(forum.getTitle());
            response.getOutputStream().println("</h1>");
            response.getOutputStream().println("</a>");

            response.getOutputStream().println("</div>");
        }

        response.getOutputStream().println("</body>");
        response.getOutputStream().println("</html>");
    }
}