package com.sam.words.main;

import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sam.words.models.Post;
import com.sam.words.models.Story;

import java.util.ArrayList;
import java.util.List;

class CardActivityListener implements ValueEventListener {

    private CardHolder holder;
    private Story story;

    CardActivityListener(CardHolder holder, Story story) {
        this.holder = holder;
        this.story = story;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        Object oPostCount = dataSnapshot.child("postCount").getValue();
        Object oChapterCount = dataSnapshot.child("chapterCount").getValue();
        Object oContributorsCount = dataSnapshot.child("contributorsCount").getValue();

        if (oPostCount != null) {
            long postCount = (long) oPostCount;
            if (postCount != story.getPostCount()) {
                holder.mNewPostsView.setText(String.valueOf(story.getPostCount() - postCount));
                holder.mNewPostsView.setVisibility(View.VISIBLE);
            }
        }

        if (oChapterCount != null) {
            long chapterCount = (long) oChapterCount;
            if (chapterCount != story.getChapterCount()) {
                holder.mNewChaptersView.setText(String.valueOf(story.getChapterCount() - chapterCount));
                holder.mNewChaptersView.setVisibility(View.VISIBLE);
            }
        }

        if (oContributorsCount != null) {
            long contributorsCount = (long) oContributorsCount;
            if (contributorsCount != story.getContributorsCount()) {
                holder.mNewContributorsView.setText(String.valueOf(story.getContributorsCount() - contributorsCount));
                holder.mNewContributorsView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        //Toast.makeText(holder.get, "Failed to get posts! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
    }
}
