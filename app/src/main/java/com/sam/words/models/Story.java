package com.sam.words.models;

import com.sam.words.models.Chapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A POJO to store and retrieve from the backend
 */

public class Story {

    private String storyId;
    private String userId;
    private String authorName;
    private String title;
    private List<Chapter> chapters;
    private long dateCreated;
    private long dateUpdated;
    private int likes = 0;
    private List<Vote> votes;

    public Story() {
        // Default constructor required for calls to DataSnapshot.getValue(Story.class)
    }

    public Story(String storyId, String userId, String title, String authorName, List<Chapter> chapters) {
        this.storyId = storyId;
        this.userId = userId;
        this.title = title;
        this.authorName = authorName;
        this.chapters = chapters;

        dateCreated = System.currentTimeMillis();
        dateUpdated = System.currentTimeMillis();

        Vote vote = new Vote();
        votes = new ArrayList<>();
        votes.add(vote);
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

    public String getTitle() {
        return title;
    }

    public List<Chapter> getChapters() {
        return chapters;
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

    public List<Vote> getVotes() {
        return votes;
    }
}
