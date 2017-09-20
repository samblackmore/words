package com.sam.words.models;

/**
 * A POJO to store and retrieve from the backend
 */

public class Post {

    private String storyId;
    private String userId;
    private String authorName;
    private String message;
    private long dateCreated;
    private long dateUpdated;
    private int votes = 0;
    private int likes = 0;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Vote.class)
    }

    public Post(String storyId, String userId, String authorName, String message) {
        this.storyId = storyId;
        this.userId = userId;
        this.authorName = authorName;
        this.message = message;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
