package com.sam.story.main.newstory;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

class WordsFoundListener implements ValueEventListener {

    private BadWordsCheck badWordsCheck;

    WordsFoundListener(BadWordsCheck badWordsCheck) {
        this.badWordsCheck = badWordsCheck;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        String word = dataSnapshot.getKey();
        Boolean found = dataSnapshot.getValue(Boolean.class);
        badWordsCheck.gotWord(found == null ? null : word);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        badWordsCheck.onDatabaseError(databaseError);
    }
}
