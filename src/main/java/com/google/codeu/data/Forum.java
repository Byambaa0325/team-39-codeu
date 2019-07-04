package com.google.codeu.data;

import java.util.List;
import java.util.UUID;

public class Forum {
  private UUID id;
  private String title;
  private List<String> ownersId;
  private List<String> membersId;
  private List<String> keywords;
  private List<String> articleIds;

  public Forum(UUID id, String title, List<String> ownersId, List<String> membersId, List<String> keywords, List<String> articleIds) {
    this.id = id;
    this.title = title;
    this.ownersId = ownersId;
    this.membersId = membersId;
    this.keywords = keywords;
    this.articleIds = articleIds;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<String> getOwnersId() {
    return ownersId;
  }

  public void setOwnersId(List<String> ownersId) {
    this.ownersId = ownersId;
  }

  public List<String> getMembersId() {
    return membersId;
  }

  public void setMembersId(List<String> membersId) {
    this.membersId = membersId;
  }

  public List<String> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<String> keywords) {
    this.keywords = keywords;
  }

  public List<String> getArticleIds() {
    return articleIds;
  }

  public void setArticleIds(List<String> articleIds) {
    this.articleIds = articleIds;
  }

  public void addArticle(String id){articleIds.add(id);}

  public void removeArticle(String id){articleIds.remove(id);}
}
