package com.sam.words.main;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sam.words.models.Notifications;
import com.sam.words.models.Story;

import java.util.HashMap;

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
