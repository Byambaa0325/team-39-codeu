/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.codeu.data;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


/**
 * A single article posted.
 */
public class Article {
    private UUID id;
    private List<String> authors;
    private List<String> tags;
    private String header;
    private String body;
    private long timestamp;

    public Article(String authors, String header, String body) {
        this(UUID.randomUUID(), authors, "", header, body, System.currentTimeMillis());
    }

    public Article(UUID id, String authors, String tags, String header, String body, long timestamp) {
        this.id = id;
        this.authors = Arrays.asList(authors.replace(" ", "").split(","));
        this.tags = Arrays.asList(tags.replace(" ", "").split(","));
        this.header = header;
        this.body = body;
        this.timestamp = timestamp;
    }

    public UUID getId() {
        return id;
    }

    public String getAuthors() {

        String authorsCommaSeparated = String.join(",", authors);
        return authorsCommaSeparated;
    }

    public List<String> getAuthorsList() {
        return authors;
    }

    public String getTags() {

        String tagsCommaSeparated = String.join(",", tags);

        return tagsCommaSeparated;
    }

    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public long getTimestamp() {
        return timestamp;
    }


}
