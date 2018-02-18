package com.sam.story.story;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sam.story.models.Post;

import java.util.ArrayList;
import java.util.List;

class PostsListener implements ValueEventListener {

    private StoryActivity activity;
    private int chapterSize;

    PostsListener(StoryActivity activity, int chapterSize) {
        this.activity = activity;
        this.chapterSize = chapterSize;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<List<Post>> postsByChapter = new ArrayList<>();
        postsByChapter.add(new ArrayList<Post>());
        int chapter = 0;
        int postNo = 0;

        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            Post post = postSnapshot.getValue(Post.class);
            if (postNo == chapterSize) {
                chapter++;
                postNo = 0;
                postsByChapter.add(new ArrayList<Post>());
            }
            postsByChapter.get(chapter).add(post);
            postNo++;
        }

        activity.gotPostsByChapter(postsByChapter);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(activity, "Failed to get story! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
    }
}
