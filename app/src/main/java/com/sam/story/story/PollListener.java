package com.sam.story.story;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sam.story.models.Poll;

import java.util.ArrayList;
import java.util.List;

class PollListener implements ValueEventListener {

    private StoryFragment frag;

    PollListener(StoryFragment frag) {
        this.frag = frag;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Poll poll = dataSnapshot.getValue(Poll.class);

        if (poll != null) {
            frag.gotPoll(poll);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(frag.getContext(), "Failed to get poll! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
    }
}
