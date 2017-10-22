package com.sam.words.main;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sam.words.models.Story;

class UserActivityListener implements ValueEventListener {

    TabFragment frag;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    UserActivityListener(TabFragment tabFragment) {
        frag = tabFragment;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        for (DataSnapshot child : dataSnapshot.getChildren()) {

            String storyId = child.getKey();

            database.getReference("stories").child(storyId).addListenerForSingleValueEvent(new ValueEventListener() {
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

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(frag.getContext(), "Failed to get activity! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
    }
}
