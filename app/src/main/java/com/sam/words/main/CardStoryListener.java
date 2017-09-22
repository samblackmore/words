package com.sam.words.main;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sam.words.models.Story;

import java.util.ArrayList;
import java.util.List;

class CardStoryListener implements ValueEventListener {

    private TabFragment frag;

    CardStoryListener(TabFragment frag) {
        this.frag = frag;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<Story> stories = new ArrayList<>();

        for (DataSnapshot child : dataSnapshot.getChildren()) {
            Story story = child.getValue(Story.class);
            if (story != null) {
                story.setId(child.getKey());
                stories.add(story);
            }
        }

        frag.gotStories(stories);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(frag.getContext(), "Failed to get stories! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
    }
}
