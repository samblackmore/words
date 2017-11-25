package com.sam.words.main;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sam.words.models.Notifications;
import com.sam.words.models.Story;

import java.util.HashMap;

class UserActivityListener implements ValueEventListener {

    private TabFragment frag;
    private boolean setStoryListeners;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    UserActivityListener(TabFragment tabFragment, boolean setStoryListeners) {
        frag = tabFragment;
        this.setStoryListeners = setStoryListeners;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        HashMap<String, Notifications> activity = new HashMap<>();

        for (DataSnapshot child : dataSnapshot.getChildren()) {

            String storyId = child.getKey();
            activity.put(storyId, child.getValue(Notifications.class));

            if (setStoryListeners) {
                database.getReference("stories").child(storyId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String storyId = dataSnapshot.getKey();
                        Story story = dataSnapshot.getValue(Story.class);

                        if (story != null) {

                            story.setId(storyId);

                            frag.gotStory(story);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(frag.getContext(), "Failed to get story! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        if (!setStoryListeners)
            frag.gotActivity(activity);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(frag.getContext(), "Failed to get activity! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
    }
}
