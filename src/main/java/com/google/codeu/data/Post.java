package com.google.codeu.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Post extends Article {
    private List<Message> comments;

    public Post(String authors, String header, String body) {
        super(authors, header, body);
        comments = new ArrayList<>();
    }

    public Post(String authors, String tags, String header, String body) {
        super(authors, tags, header, body);
        comments = new ArrayList<>();
    }

    public Post(UUID id, String authors, String tags, String header, String body, long timestamp) {
        super(id, authors, tags, header, body, timestamp);
        comments = new ArrayList<>();
    }

    public List<Message> getComments() {
        return comments;
    }

    public void addComment(Message message){
        comments.add(message);
    }

    public void deleteComment(Message message){
        comments.remove(message);
    }

}
