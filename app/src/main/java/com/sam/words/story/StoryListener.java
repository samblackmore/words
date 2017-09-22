package com.sam.words.story;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sam.words.models.Story;

class StoryListener implements ValueEventListener {

    private StoryActivity activity;

    StoryListener(StoryActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Story story = dataSnapshot.getValue(Story.class);
        if (story != null) {
            story.setId(dataSnapshot.getKey());
            activity.gotStory(story);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(activity, "Failed to get story! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
    }
}
