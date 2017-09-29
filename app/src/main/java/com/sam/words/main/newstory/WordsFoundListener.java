package com.sam.words.main.newstory;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

class WordsFoundListener implements ValueEventListener {

    private NewStoryFragment frag;

    WordsFoundListener(NewStoryFragment frag) {
        this.frag = frag;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        String word = dataSnapshot.getKey();
        Boolean found = dataSnapshot.getValue(Boolean.class);
        frag.foundWord(found == null ? null : word);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(frag.getActivity(), "Failed to look up word! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
    }
}
