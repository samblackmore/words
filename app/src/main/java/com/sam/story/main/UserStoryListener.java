package com.sam.story.main;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

class UserStoryListener implements ValueEventListener {

    private TabFragment frag;

    UserStoryListener(TabFragment tabFragment) {
        frag = tabFragment;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        frag.gotUserStoryCount(dataSnapshot.getChildrenCount());
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(frag.getContext(), "Failed to get story count! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
    }
}
