package com.sam.story.story;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sam.story.models.Post;

import java.util.ArrayList;
import java.util.List;

class PostsListener implements ValueEventListener {

    private StoryFragment fragment;

    PostsListener(StoryFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<Post> posts = new ArrayList<>();

        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            Post post = postSnapshot.getValue(Post.class);
            posts.add(post);
        }

        fragment.gotPosts(posts);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(fragment.getContext(), "Failed to get posts for round! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
    }
}
