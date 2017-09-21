package com.sam.words.models;

import java.util.HashMap;

/**
 * A POJO to store and retrieve from the backend
 */

public class Story {

    private String title;
    private String userId;
    private String authorAlias;
    private String preview;
    private long dateCreated;
    private long dateUpdated;
    private int likeCount = 0;
    private HashMap<String, Boolean> likes = new HashMap<>();

    public Story() {
        // Default constructor required for calls to DataSnapshot.getValue(Story.class)
    }

    public Story(String title, String userId, String authorAlias, String preview) {
        this.title = title;
        this.userId = userId;
        this.authorAlias = authorAlias;
        this.preview = preview;

        dateCreated = System.currentTimeMillis();
        dateUpdated = System.currentTimeMillis();
    }

    public String getTitle() {
        return title;
    }

    public String getUserId() {
        return userId;
    }

    public String getAuthorAlias() {
        return authorAlias;
    }

    public String getPreview() {
        return preview;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public long getDateUpdated() {
        return dateUpdated;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void addLike(String userId) {
        likes.put(userId, true);
        likeCount = likes.size();
    }

    public void removeLike(String userId) {
        likes.remove(userId);
        likeCount = likes.size();
    }
}
