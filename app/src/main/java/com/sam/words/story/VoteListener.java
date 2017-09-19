package com.sam.words.story;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sam.words.models.Post;

import java.util.ArrayList;
import java.util.List;

class VoteListener implements ValueEventListener {

    private StoryFragment frag;

    VoteListener(StoryFragment frag) {
        this.frag = frag;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<Post> votes = new ArrayList<>();

        for (DataSnapshot child : dataSnapshot.getChildren()) {
            Post post = child.getValue(Post.class);
            post.setMessage(child.getKey());
            votes.add(post);
        }

        frag.gotVotes(votes);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(frag.getContext(), "Failed to get words! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
    }
}
