package com.sam.words.models;

import java.util.ArrayList;
import java.util.List;

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
}
