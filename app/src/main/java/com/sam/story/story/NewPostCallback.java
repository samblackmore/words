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

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private StoryActivity storyActivity;
    private Post newPost;
    private String storyId;
    private String roundId;

    public NewPostCallback(StoryActivity storyActivity, Post newPost, String storyId, String roundId) {
        this.storyActivity = storyActivity;
        this.newPost = newPost;
        this.storyId = storyId;
        this.roundId = roundId;
    }

    @Override
    public void allWordsClean() {

        DatabaseReference newPostRef = database.getReference("/posts/" + storyId + "/" + roundId).push();
        newPost.setPath(newPostRef.toString());
        newPostRef.setValue(newPost);

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
