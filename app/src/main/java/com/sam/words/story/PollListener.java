package com.sam.words.story;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sam.words.models.Poll;

import java.util.ArrayList;
import java.util.List;

class PollListener implements ValueEventListener {

    private StoryFragment frag;

    PollListener(StoryFragment frag) {
        this.frag = frag;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        List<Poll> polls = new ArrayList<>();

        for (DataSnapshot child : dataSnapshot.getChildren()) {
            Poll poll = child.getValue(Poll.class);
            if (poll != null) {
                poll.setId(child.getKey());
                polls.add(poll);
            }
        }

        if (polls.size() > 0) {
            Poll currentPoll = polls.get(polls.size() - 1);
            frag.gotPoll(currentPoll);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(frag.getContext(), "Failed to get poll! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
    }
}
