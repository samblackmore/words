package com.sam.story.main;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sam.story.models.Notifications;
import com.sam.story.models.Story;

import java.util.ArrayList;
import java.util.HashMap;

class UserActivityListener implements ValueEventListener {

    private TabFragment frag;
    private ArrayList<String> storiesListenedTo = new ArrayList<>();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    UserActivityListener(TabFragment tabFragment) {
        frag = tabFragment;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        HashMap<String, Notifications> activity = new HashMap<>();

        for (DataSnapshot child : dataSnapshot.getChildren()) {

            String storyId = child.getKey();
            activity.put(storyId, child.getValue(Notifications.class));

            if (!storiesListenedTo.contains(storyId)) {
                storiesListenedTo.add(storyId);
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

        frag.gotActivity(activity);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(frag.getContext(), "Failed to get activity! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
    }
}
