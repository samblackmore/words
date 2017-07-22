package com.sam.words;

/**
 * Created by samhb on 2017-07-19.
 */

public class Story {

    private String author;
    private String title;
    private String content;
    private long dateCreated;
    private long dateUpdated;
    private int likes;

    public Story() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Story(String author, String title) {
        this.author = author;
        this.title = title;
        dateCreated = System.currentTimeMillis();
        dateUpdated = System.currentTimeMillis();
        likes = 0;
        content = "";
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
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
