package com.sam.words.models;

import java.util.List;

/**
 * A POJO to store and retrieve from the backend
 */

public class Post {

    private String storyId;
    private String userId;
    private String authorName;
    private long dateCreated;
    private long dateUpdated;
    private int votes = 0;
    private int likes = 0;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Vote.class)
    }

    public Post(String storyId, String userId, String authorName) {
        this.storyId = storyId;
        this.userId = userId;
        this.authorName = authorName;

        dateCreated = System.currentTimeMillis();
        dateUpdated = System.currentTimeMillis();
    }

    public String getStoryId() {
        return storyId;
    }

    public String getUserId() {
        return userId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public int getVotes() {
        return votes;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public long getDateUpdated() {
        return dateUpdated;
    }

    public int getLikes() {
        return likes;
    }
}
