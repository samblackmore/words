package com.sam.words.story;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sam.words.models.Poll;

import java.util.ArrayList;
import java.util.List;

class PollCountListener implements ValueEventListener {

    private StoryFragment frag;

    PollCountListener(StoryFragment frag) {
        this.frag = frag;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Long val = (Long) dataSnapshot.getValue();
        if (val != null)
            frag.gotPollCount(val);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(frag.getContext(), "Failed to get poll! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
    }
}
