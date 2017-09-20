package com.sam.words.story;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sam.words.models.Vote;

import java.util.ArrayList;
import java.util.List;

class VoteListener implements ValueEventListener {

    private StoryFragment frag;

    VoteListener(StoryFragment frag) {
        this.frag = frag;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        List<Vote> votes = new ArrayList<>();

        for (DataSnapshot child : dataSnapshot.getChildren()) {
            Vote vote = child.getValue(Vote.class);
            vote.setId(child.getKey());
            votes.add(vote);
        }

        if (votes.size() > 0) {
            Vote currentVote = votes.get(votes.size() - 1);
            frag.gotVote(currentVote);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(frag.getContext(), "Failed to get words! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
    }
}
