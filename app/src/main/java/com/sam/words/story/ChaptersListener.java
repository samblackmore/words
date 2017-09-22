package com.sam.words.story;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sam.words.models.Chapter;
import com.sam.words.models.Post;

import java.util.ArrayList;
import java.util.List;

class ChaptersListener implements ValueEventListener {

    private StoryActivity activity;

    ChaptersListener(StoryActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<Chapter> chapters = new ArrayList<>();

        for (DataSnapshot child : dataSnapshot.getChildren()) {
            Chapter chapter = child.getValue(Chapter.class);
            DataSnapshot posts = child.child("posts");

            List<Post> postList = new ArrayList<>();

            for (DataSnapshot post : posts.getChildren()) {
                Post p = post.getValue(Post.class);
                postList.add(p);
            }

            chapter.setPostsList(postList);
            chapters.add(chapter);
        }

        activity.gotChapters(chapters);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(activity, "Failed to get story! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
    }
}
