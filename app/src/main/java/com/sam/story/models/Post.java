package com.sam.story.models;

import java.util.ArrayList;
import java.util.List;

/**
 * A POJO to store and retrieve from the backend
 */

public class Post {

    private String storyId;
    private String userId;
    private String authorName;
    private String message;
    private String path;
    private long dateCreated;
    private long dateUpdated;
    private int voteCount = 0;
    private int likeCount = 0;
    private List<String> votes = new ArrayList<>();
    private List<String> likes = new ArrayList<>();

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

    public long getDateCreated() {
        return dateCreated;
    }

    public long getDateUpdated() {
        return dateUpdated;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getVotes() {
        return votes;
    }

    public void addVote(String userId) {
        votes.add(userId);
        voteCount = votes.size();
    }

    public void removeVote(String userId) {
        votes.remove(userId);
        voteCount = votes.size();
    }

    public List<String> getLikes() {
        return likes;
    }

    public void addLike(String userId) {
        likes.add(userId);
        likeCount = likes.size();
    }

    public void removeLike(String userId) {
        likes.remove(userId);
        likeCount = likes.size();
    }

    public int getVoteCount() {
        return voteCount;
    }

    public int getLikeCount() {
        return likeCount;
    }
}
