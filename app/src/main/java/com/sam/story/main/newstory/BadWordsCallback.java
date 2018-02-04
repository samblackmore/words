package com.sam.story.main.newstory;

/**
 * Pass an instance implementing this callback to the bad-words check
 */

public interface BadWordsCallback {
    void allWordsClean();
    void badWordsFound(BadWordsDialog badWordsDialog);
    void databaseError(String databaseError);
}
