package com.google.codeu.data;

import java.util.List;
import java.util.UUID;

public class Forum {
    private UUID id;
    private String title;
    private List<User> owners;
    private List<User> members;
    private List<String> keywords;
    private List<UUID> articleIds;

    public Forum(UUID id, String title, List<User> owners, List<User> members, List<String> keywords, List<UUID> articleIds) {
        this.id = id;
        this.title = title;
        this.owners = owners;
        this.members = members;
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

    public List<User> getOwners() {
        return owners;
    }

    public void setOwners(List<User> owners) {
        this.owners = owners;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<UUID> getArticleIds() {
        return articleIds;
    }

    public void setArticleIds(List<UUID> articleIds) {
        this.articleIds = articleIds;
    }

    public void addArticle(UUID id){articleIds.add(id);}

    public void removeArticle(UUID id){articleIds.remove(id);}
}
