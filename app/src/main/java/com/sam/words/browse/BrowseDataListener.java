package com.sam.words.browse;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sam.words.models.Story;

import java.util.ArrayList;
import java.util.List;

class BrowseDataListener implements ValueEventListener {

    private BrowseTabFragment frag;

    BrowseDataListener(BrowseTabFragment frag) {
        this.frag = frag;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<Story> stories = new ArrayList<>();

        for (DataSnapshot child : dataSnapshot.getChildren()) {
            Story story = child.getValue(Story.class);
            stories.add(story);
        }

        frag.setStories(stories);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(frag.getContext(), "Failed to get stories! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
    }
}
