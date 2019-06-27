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


    public Forum(UUID id, String title, List<User> owners, List<User> members, List<String> keywords, List<ForumSection> sections) {
        this.id = id;
        this.title = title;
        this.owners = owners;
        this.members = members;
        this.keywords = keywords;
        this.sections = sections;
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

    public List<ForumSection> getSections() {
        return sections;
    }

    public void setSections(List<ForumSection> sections) {
        this.sections = sections;
    }


}
