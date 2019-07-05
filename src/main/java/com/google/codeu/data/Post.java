package com.google.codeu.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Post extends Article {
    private List<String> comments;

    public Post(String authors, String header, String body) {
        super(authors, header, body);
        comments = new ArrayList<>();
    }

    public Post(String authors, String tags, String header, String body) {
        super(authors, tags, header, body);
        comments = new ArrayList<>();
    }

    public Post(String authors, String tags, String header, String body, String coordinates) {
        super(authors, tags, header, body, coordinates);
        comments = new ArrayList<>();
    }

    public Post(UUID id, String authors, String tags, String header, String body, long timestamp, String coordinates) {
        super(id, authors, tags, header, body, timestamp, coordinates);
        comments = new ArrayList<>();
    }

    public List<String> getComments() {
        return comments;
    }

    public void addComment(String message){
        comments.add(message);
    }

    public void deleteComment(String message){
        comments.remove(message);
    }

}
