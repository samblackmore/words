package com.sam.words.models;

import java.util.ArrayList;
import java.util.List;

public class Vote {

    private String id;
    private long timeCreated;
    private Long timeEnding = null;
    private List<Post> posts = new ArrayList<>();

    public Vote() {
        timeCreated = System.currentTimeMillis();

        posts.add(new Post("123", "456", "Bobn", "Herro"));
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public Long getTimeEnding() {
        return timeEnding;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
