package com.sam.words.dictionary;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sam.words.browse.NewStoryFragment;
import com.sam.words.models.Word;

public class WordListener implements ValueEventListener {

    private NewStoryFragment frag;

    public WordListener(NewStoryFragment frag) {
        this.frag = frag;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        String word = dataSnapshot.getKey();
        Word wordData = dataSnapshot.getValue(Word.class);
        frag.foundWord(wordData == null ? null : word);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(frag.getActivity(), "Failed to get word! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
    }
}
