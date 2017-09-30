package com.sam.words.main.newstory;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Check the bad-words endpoint and then call the given allWordsClean
 */

public class BadWordsCheck {

    private final LinkedHashSet<String> query;
    private final ArrayList<String> results;
    private BadWordsCallback callback;

    public BadWordsCheck(List<String> words, BadWordsCallback callback) {
        this.callback = callback;

        // Convert to set to remove duplicates, Linked preserves order for error message
        query = new LinkedHashSet<>(words);
        results = new ArrayList<>();
    }

    public void execute() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        for (String word : query) {
            String sanitized = word.toLowerCase().replaceAll("[^a-zA-Z0-9\\-]", "");

            database.getReference("bad-words").child(sanitized)
                    .addListenerForSingleValueEvent(new WordsFoundListener(this));
        }
    }

    public void gotWord(String word) {

        results.add(word);

        if (results.size() == query.size()) {

            ArrayList<String> badWords = filterNotNull(results);

            if (badWords.size() > 0)
                callback.badWordsFound(BadWordsDialog.newInstance(badWords));
            else
                callback.allWordsClean();
        }
    }

    public void onDatabaseError(DatabaseError databaseError) {
        callback.databaseError("Failed to look up word! " + databaseError.toString());
    }

    private ArrayList<String> filterNotNull(ArrayList<String> input) {
        ArrayList<String> output = new ArrayList<>();
        for (String s : input)
            if (s != null)
                output.add(s);
        return output;
    }
}
