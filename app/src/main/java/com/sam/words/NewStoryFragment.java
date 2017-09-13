package com.sam.words;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * New story popup
 */

public class NewStoryFragment extends DialogFragment {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private String[] query;
    private List<WordQueryResult> queryResults;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_new_story, null))
                .setPositiveButton(R.string.add_story, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog d = (AlertDialog) dialog;
                        String title = ((TextInputEditText) d.findViewById(R.id.new_story_title)).getText().toString();
                        String author = ((TextInputEditText) d.findViewById(R.id.new_story_author)).getText().toString();
                        String content = ((TextInputEditText) d.findViewById(R.id.new_story_content)).getText().toString();
                        postStory(title, author, content);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NewStoryFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void postStory(String title, String author, String content) {

        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(getActivity(), "Not signed in!", Toast.LENGTH_SHORT).show();
            return;
        }

        checkDictionary(title);
        checkDictionary(content);

        /*List<Chapter> chapters = new ArrayList<>();
        chapters.add(new Chapter("Chapter One", content));

        DatabaseReference ref = database.getReference("stories");
        DatabaseReference newRef = ref.push();

        newRef.setValue(new Story(newRef.getKey(), user.getUid(), title, author, chapters));*/
    }

    private void checkDictionary(String string) {
        DatabaseReference ref = database.getReference("words");

        query = string.split(" ");
        queryResults = new ArrayList<>();

        for (String word : query) {
            Query query = ref.child(word.toLowerCase());
            query.addListenerForSingleValueEvent(new WordListener(this));
        }
    }

    public void addQueryResult(WordQueryResult result) {
        queryResults.add(result);

        if (queryResults.size() == query.length) {
            WordQueryDialog dialog = WordQueryDialog.newInstance("Words were not found!");
            dialog.show(dialog.getFragmentManager(), "newstory");
        }
    }
}
