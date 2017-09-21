package com.sam.words.models;

import java.util.List;

/**
 * A list of chapters lets us know how to divide up pages since each new chapter should start
 * on a new page
 */

public class Chapter {
    private int number;
    private String title;
    private List<Post> posts;

    public Chapter() {
        // Default constructor required for calls to DataSnapshot.getValue(Chapter.class)
    }

    public Chapter(int number, String title) {
        this.number = number;
        this.title = title;
    }

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
