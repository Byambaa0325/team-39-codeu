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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Set;
import java.util.HashSet;

/** Provides access to the data stored in Datastore. */
public class Datastore {

  private DatastoreService datastore;

  public Datastore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /** Retrieves all articles and score them. Then, put
  the score in datastore indexed by article's id*/
  public void scoreArticles(){
    List<Article> articles = getAllArticles();

    for (Article article : articles) {
      Document doc = Document.newBuilder()
      .setContent(article.getBody()).setType(Document.Type.PLAIN_TEXT).build();

      LanguageServiceClient languageService;
      try {
        languageService = LanguageServiceClient.create();
        Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();

        float score = sentiment.getScore();
        languageService.close();

        Entity articleScoreEntity = new Entity("SentimentScore", article.getId().toString());
        articleScoreEntity.setProperty("score", score);
        datastore.put(articleScoreEntity);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public float getScoreById(String id){
    Query query =
    new Query("SentimentScore")
    .setFilter(new Query.FilterPredicate("__key__", FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    float score = 0;
    for( Entity entity : results.asIterable()){
      try{
        score = (float) entity.getProperty("score");

      } catch (Exception e) {
        System.err.println("Error reading score.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }
    return score;
  }
  /** Fetches markers from Datastore. */
  public List<Marker> getMarkers() {
    List<Marker> markers = new ArrayList<>();

    Query query = new Query("Marker");
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      double lat = (double) entity.getProperty("lat");
      double lng = (double) entity.getProperty("lng");
      String content = (String) entity.getProperty("content");

      Marker marker = new Marker(lat, lng, content);
      markers.add(marker);
    }
    return markers;
  }

  /** Stores a marker in Datastore. */
  public void storeMarker(Marker marker) {
    Entity markerEntity = new Entity("Marker");
    markerEntity.setProperty("lat", marker.getLat());
    markerEntity.setProperty("lng", marker.getLng());
    markerEntity.setProperty("content", marker.getContent());

    datastore.put(markerEntity);
  }

  /** Stores the Message in Datastore. */
  public void storeMessage(Message message) {
    Entity messageEntity = new Entity("Message", message.getId().toString());
    messageEntity.setProperty("user", message.getUser());
    messageEntity.setProperty("text", message.getText());
    messageEntity.setProperty("timestamp", message.getTimestamp());

    datastore.put(messageEntity);
  }

  public Set<String> getUsers() {
    Set<String> users = new HashSet<>();
    Query query = new Query("Message");
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
      users.add((String)entity.getProperty("user"));
    }
    return users;
  }

  /**
  * Gets messages posted by a specific user.
  *
  * @return a list of messages posted by the user, or empty list if user has
  * never posted a message. List is sorted by time descending.
  */
  public List<Message> getMessages(String user) {

    Query query =
    new Query("Message")
    .setFilter(new Query.FilterPredicate("user", FilterOperator.EQUAL, user))
    .addSort("timestamp", SortDirection.DESCENDING);

    PreparedQuery results = datastore.prepare(query);

    return readMessagesFromQuery(results);
  }

  /**
  * Gets all messages stored in Datastore
  *
  * @return a list of messages posted sorted by timestamp descending, or empty list if
  * there is no messages stored
  */
  public List<Message> getAllMessages(){

    Query query =
    new Query("Message").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    return readMessagesFromQuery(results);
  }

  /**
  * Extracts all messages from query
  *
  * @return a list of messages, or empty list if
  * the query is empty
  */
  private List<Message> readMessagesFromQuery(PreparedQuery results){
    List<Message> messages = new ArrayList<>();

    for( Entity entity : results.asIterable()){
      try{

        String idString = entity.getKey().getName();
        UUID id = UUID.fromString(idString);
        String user = (String) entity.getProperty("user");
        String text = (String) entity.getProperty("text");
        long timestamp = (long) entity.getProperty("timestamp");

        Message message = new Message(id, user, text, timestamp);
        messages.add(message);
      } catch (Exception e) {
        System.err.println("Error reading message.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }
    return messages;
  }

  /** Stores the User in Datastore. */
  public void storeUser(User user) {
    Entity userEntity = new Entity("User", user.getEmail());

    userEntity.setProperty("email", user.getEmail());
    userEntity.setProperty("aboutMe", user.getAboutMe());

    datastore.put(userEntity);
  }

  /**
  * Returns the User owned by the email address, or
  * null if no matching User was found.
  */
  public User getUser(String email) {
    Query query = new Query("User")
    .setFilter(new Query.FilterPredicate("email", FilterOperator.EQUAL, email));

    PreparedQuery results = datastore.prepare(query);
    Entity userEntity = results.asSingleEntity();
    if (userEntity == null) {
      return null;
    }

    String aboutMe = (String) userEntity.getProperty("aboutMe");
    return new User(email, aboutMe);
  }

  /** Stores the Article in Datastore. */
  public void storeArticle(Article article) {
    Entity articleEntity = new Entity("Article", article.getId().toString());
    articleEntity.setProperty("id", article.getId().toString());
    articleEntity.setProperty("authors", article.getAuthors());
    articleEntity.setProperty("tags",article.getTags());
    articleEntity.setProperty("header",article.getHeader());
    articleEntity.setProperty("body", article.getBody());
    articleEntity.setProperty("timestamp", article.getTimestamp());

    datastore.put(articleEntity);
  }

  /**
  * Gets article of specific id.
  *
  * @return a single-element list of article posted with the id, or empty list
  * if article with the id was never posted.
  */
  public List<Article> getArticleById(String id) {

    Query query =
    new Query("Article")
    .setFilter(new Query.FilterPredicate("id", FilterOperator.EQUAL, id));

    PreparedQuery results = datastore.prepare(query);

    return readArticlesFromQuery(results);
  }

  /**
  * Gets articles posted by a specific user.
  *
  * @return a list of articles posted by the user, or empty list if user has
  * never posted a article. List is sorted by time descending.
  */
  public List<Article> getArticles(String user) {

    Query query =
    new Query("Article")
    .setFilter(new Query.FilterPredicate("user", FilterOperator.EQUAL, user))
    .addSort("timestamp", SortDirection.DESCENDING);

    PreparedQuery results = datastore.prepare(query);

    return readArticlesFromQuery(results);
  }

  /**
  * Gets all articles stored in Datastore
  *
  * @return a list of articles posted sorted by timestamp descending, or empty list if
  * there is no articles stored
  */
  public List<Article> getAllArticles(){

    Query query =
    new Query("Article").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    return readArticlesFromQuery(results);
  }

  /**
  * Extracts all articles from query
  *
  * @return a list of articles, or empty list if
  * the query is empty
  */
  private List<Article> readArticlesFromQuery(PreparedQuery results){
    List<Article> articles = new ArrayList<>();

    for( Entity entity : results.asIterable()){
      try{

        String idString = entity.getKey().getName();
        UUID id = UUID.fromString(idString);
        String authors = (String) entity.getProperty("authors");
        String tags = (String) entity.getProperty("tags");
        String header = (String) entity.getProperty("header");
        String body = (String) entity.getProperty("body");
        long timestamp = (long) entity.getProperty("timestamp");

        Article article = new Article(id, authors, tags, header, body, timestamp);
        articles.add(article);
      } catch (Exception e) {
        System.err.println("Error reading article.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }
    return articles;
  }
}
