package com.google.codeu.data;

import java.util.UUID;

public class Post extends Article {
    public Post(String authors, String header, String body) {
        super(authors, header, body);
    }

    public Post(String authors, String tags, String header, String body) {
        super(authors, tags, header, body);
    }

    public Post(UUID id, String authors, String tags, String header, String body, long timestamp) {
        super(id, authors, tags, header, body, timestamp);
    }
}
