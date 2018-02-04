package com.sam.story.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Poll {

    private int round;
    private long timeCreated;
    private Long timeEnding = null;
    private boolean finished = false;
    private List<Post> posts = new ArrayList<>();

    public Poll() {
        // For firebase
    }

    public Poll(int round) {
        this.round = round;
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

    public int getRound() {
        return round;
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
