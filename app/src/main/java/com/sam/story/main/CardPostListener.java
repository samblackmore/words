package com.sam.story.main;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sam.story.models.Post;

import java.util.ArrayList;
import java.util.List;

class CardPostListener implements ValueEventListener {

    private CardHolder holder;

    CardPostListener(CardHolder holder) {
        this.holder = holder;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<Post> posts = new ArrayList<>();

        for (DataSnapshot child : dataSnapshot.getChildren()) {
            Post post = child.getValue(Post.class);
            posts.add(post);
        }

        holder.mWordsView.setPreview(posts);
        holder.mWordsView.animate().alpha(1.0f);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        //Toast.makeText(holder.get, "Failed to get posts! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
    }
}
