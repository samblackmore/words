package com.sam.words.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // For Firebase
    public void setPosts(HashMap<String, Post> map) {
        List<Post> posts = new ArrayList<>();
        for (Map.Entry<String, Post> mapEntry : map.entrySet()) {
            posts.add(mapEntry.getValue());
        }
        this.posts = posts;
    }

    public void setPostsList(List<Post> posts) {
        this.posts = posts;
    }
}
