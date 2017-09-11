package com.sam.words;

import java.util.ArrayList;
import java.util.List;

/**
 * A POJO to store and retrieve from the backend
 */

public class Story {

    private int id = 0;
    private String author;
    private String title;
    private List<Chapter> chapters = new ArrayList<>();
    private long dateCreated;
    private long dateUpdated;
    private int likes = 0;

    public Story() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Story(String author, String title) {
        this.author = author;
        this.title = title;
        dateCreated = System.currentTimeMillis();
        dateUpdated = System.currentTimeMillis();
    }

    public String getAuthor() {
        return author;
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

    public int getId() {
        return id;
    }
}
