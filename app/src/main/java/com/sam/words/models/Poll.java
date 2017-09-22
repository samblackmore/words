package com.sam.words.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Poll {

    private String id;
    private long timeCreated;
    private Long timeEnding = null;
    private boolean finished = false;
    private List<Post> posts = new ArrayList<>();

    public Poll() {
        timeCreated = System.currentTimeMillis();
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

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setPosts(HashMap<String, Post> map) {
        List<Post> posts = new ArrayList<>();
        for (Map.Entry<String, Post> mapEntry : map.entrySet()) {
            posts.add(mapEntry.getValue());
        }
        this.posts = posts;
    }
}
