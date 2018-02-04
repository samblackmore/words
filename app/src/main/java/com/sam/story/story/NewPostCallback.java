package com.sam.story.story;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.sam.story.main.newstory.BadWordsCallback;
import com.sam.story.main.newstory.BadWordsDialog;
import com.sam.story.models.Post;
import com.sam.story.models.Story;

import java.util.HashMap;
import java.util.Map;

/**
 * Make a new post after bad words check
 */

public class NewPostCallback implements BadWordsCallback {

    private final int COUNTDOWN_LENGTH = 5 * 60 * 1000;
    //private final int COUNTDOWN_LENGTH = 24 * 60 * 60 * 1000;
    //private final int COUNTDOWN_LENGTH = 30 * 1000;

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private StoryActivity storyActivity;
    private Post newPost;
    private String storyId;
    private String chapterId;
    private String pollId;

    public NewPostCallback(StoryActivity storyActivity, Post newPost, String storyId, String chapterId, String pollId) {
        this.storyActivity = storyActivity;
        this.newPost = newPost;
        this.storyId = storyId;
        this.chapterId = chapterId;
        this.pollId = pollId;
    }

    @Override
    public void allWordsClean() {

        String pollPath = "/poll/" + storyId + "/" + chapterId + "/" + pollId;
        DatabaseReference pollRef = database.getReference(pollPath);
        DatabaseReference newPostRef = pollRef.child("posts").push();
        newPost.setPath(newPostRef.toString());
        String postId = newPostRef.getKey();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(pollPath + "/timeEnding", System.currentTimeMillis() + COUNTDOWN_LENGTH);
        childUpdates.put(pollPath + "/posts/" + postId, newPost);

        database.getReference().updateChildren(childUpdates);
        database.getReference("stories").child(storyId).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Story s = mutableData.getValue(Story.class);
                FirebaseUser user = auth.getCurrentUser();

                if (s == null || user == null)
                    return Transaction.success(mutableData);

                if (s.getContributors() == null || !s.getContributors().containsKey(user.getUid()))
                    s.addContributor(user.getUid());

                mutableData.setValue(s);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (databaseError != null)
                    Log.d("Debug", databaseError.toString());
            }
        });
    }

    @Override
    public void badWordsFound(BadWordsDialog badWordsDialog) {
        badWordsDialog.show(storyActivity.getSupportFragmentManager(), "bad-words");
    }

    @Override
    public void databaseError(String message) {
        Toast.makeText(storyActivity, message, Toast.LENGTH_SHORT).show();
    }
}
