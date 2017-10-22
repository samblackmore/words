package com.sam.words.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A POJO to store and retrieve from the backend
 */

public class Story {

    private String id;
    private String title;
    private String userId;
    private String authorAlias;
    private List<Chapter> chapters = new ArrayList<>();
    private int chapterSize = 20;
    private int chapterCount = 1;
    private int chapterLimit = 3;
    private long dateCreated;
    private long dateUpdated;
    private int likeCount = 0;
    private int postCount = 1;
    private int contributorsCount = 0;
    private boolean finished = false;
    private boolean hasUpdates = false;
    private HashMap<String, Boolean> likes = new HashMap<>();
    private HashMap<String, Boolean> contributors = new HashMap<>();

    public Story() {
        // Default constructor required for calls to DataSnapshot.getValue(Story.class)
    }

    public Story(String title, String userId, String authorAlias) {
        this.title = title;
        this.userId = userId;
        this.authorAlias = authorAlias;

        dateCreated = System.currentTimeMillis();
        dateUpdated = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public long getDateCreated() {
        return dateCreated;
    }

    public long getDateUpdated() {
        return dateUpdated;
    }

    public HashMap<String, Boolean> getLikes() {
        return likes;
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

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void addChapter(Chapter chapter) {
        chapters.add(chapter);
    }

    public int getChapterSize() {
        return chapterSize;
    }

    public int getChapterLimit() {
        return chapterLimit;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean hasUpdates() {
        return hasUpdates;
    }

    public void setHasUpdates(boolean hasUpdates) {
        this.hasUpdates = hasUpdates;
    }

    public int getChapterCount() {
        return chapterCount;
    }

    public int getPostCount() {
        return postCount;
    }

    public int getContributorsCount() {
        return contributorsCount;
    }

    public HashMap<String, Boolean> getContributors() {
        return contributors;
    }

    public void addContributor(String userId) {
        contributors.put(userId, true);
        contributorsCount = contributors.size();
    }
}
