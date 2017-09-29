package com.sam.words.main;

import android.view.View;
import android.widget.TextView;

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

        Long postCount = (Long) dataSnapshot.child("postCount").getValue();
        Long chapterCount = (Long) dataSnapshot.child("chapterCount").getValue();
        Long contributorsCount = (Long) dataSnapshot.child("contributorsCount").getValue();

        showNotification(holder.mNewPostsView, story.getPostCount(), postCount);
        showNotification(holder.mNewChaptersView, story.getChapterCount(), chapterCount);
        showNotification(holder.mNewContributorsView, story.getContributorsCount(), contributorsCount);
    }

    private void showNotification(TextView view, long storyCount, Long userCount) {
        if (userCount != null) {
            long diff = storyCount - userCount;
            view.setText(String.valueOf(diff));
            view.setVisibility(diff == 0 ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        //Toast.makeText(holder.get, "Failed to get posts! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
    }
}
