package com.sam.words.models;

/**
 * A list of chapters lets us know how to divide up pages since each new chapter should start
 * on a new page
 */

public class Chapter {
    private int number;
    private String title;

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
}
