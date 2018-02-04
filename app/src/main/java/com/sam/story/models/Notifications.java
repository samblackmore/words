package com.sam.story.models;

public class Notifications {
    int chapterCount;
    int contributorsCount;
    int postCount;

    public Notifications() {
        // Default constructor required for calls to DataSnapshot.getValue(Notifications.class)
    }

    public Notifications(int chapterCount, int contributorsCount, int postCount) {
        this.chapterCount = chapterCount;
        this.contributorsCount = contributorsCount;
        this.postCount = postCount;
    }

    public int getChapterCount() {
        return chapterCount;
    }

    public int getContributorsCount() {
        return contributorsCount;
    }

    public int getPostCount() {
        return postCount;
    }
}
