package com.sam.words.story;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sam.words.models.Chapter;
import com.sam.words.models.Post;

import java.util.ArrayList;
import java.util.List;

class PostsListener implements ValueEventListener {

    private StoryActivity activity;

    PostsListener(StoryActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<List<Post>> postsByChapter = new ArrayList<>();

        for (DataSnapshot chapterSnapshot : dataSnapshot.getChildren()) {

            List<Post> posts = new ArrayList<>();

            for (DataSnapshot postSnapshot : chapterSnapshot.getChildren()) {

                Post post = postSnapshot.getValue(Post.class);
                posts.add(post);
            }

            postsByChapter.add(posts);
        }

        activity.gotPostsByChapter(postsByChapter);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(activity, "Failed to get story! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
    }
}
