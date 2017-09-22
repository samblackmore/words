package com.sam.words.dictionary;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sam.words.models.Word;

import java.util.ArrayList;
import java.util.List;

class DictionaryListener implements ValueEventListener {

    private DictionaryActivity activity;

    DictionaryListener(DictionaryActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<Word> words = new ArrayList<>();

        for (DataSnapshot child : dataSnapshot.getChildren()) {
            Word word = child.getValue(Word.class);
            word.setWord(child.getKey());
            words.add(word);
        }

        activity.gotWords(words);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(activity, "Failed to get words! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
    }
}
