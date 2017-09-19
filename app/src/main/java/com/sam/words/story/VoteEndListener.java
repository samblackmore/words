package com.sam.words.story;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sam.words.models.Post;

import java.util.ArrayList;
import java.util.List;

class VoteEndListener implements ValueEventListener {

    private StoryFragment frag;

    VoteEndListener(StoryFragment frag) {
        this.frag = frag;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Integer time = (Integer) dataSnapshot.getValue();
        frag.gotTimer(time);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(frag.getContext(), "Failed to get timer! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
    }
}
