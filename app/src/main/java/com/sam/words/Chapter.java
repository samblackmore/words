package com.sam.words;

/**
 * A list of chapters lets us know how to divide up pages since each new chapter should start
 * on a new page
 */

public class Chapter {
    private String title;
    private String content;

    public Chapter(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
