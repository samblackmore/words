package com.sam.story.story;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sam.story.models.Post;

import java.util.ArrayList;
import java.util.List;

class WinnersListener implements ValueEventListener {

    private StoryActivity activity;
    private int chapterSize;

    WinnersListener(StoryActivity activity, int chapterSize) {
        this.activity = activity;
        this.chapterSize = chapterSize;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<List<Post>> winnersByChapter = new ArrayList<>();
        winnersByChapter.add(new ArrayList<Post>());
        int chapter = 0;
        int postNo = 0;

        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            Post post = postSnapshot.getValue(Post.class);
            if (postNo == chapterSize) {
                chapter++;
                postNo = 0;
                winnersByChapter.add(new ArrayList<Post>());
            }
            winnersByChapter.get(chapter).add(post);
            postNo++;
        }

        activity.gotWinnersByChapter(winnersByChapter);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(activity, "Failed to get story! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
    }
}
