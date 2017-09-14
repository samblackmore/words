package com.sam.words;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * New story popup
 */

public class NewStoryFragment extends DialogFragment {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("words");
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private LinkedHashSet<String> query;
    private List<WordQueryResult> queryResults;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        return new AlertDialog.Builder(getActivity())
                .setView(inflater.inflate(R.layout.dialog_new_story, null))
                .setPositiveButton(R.string.add_story, null)
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    @Override
    public void onResume() {
        super.onResume();

        final AlertDialog d = (AlertDialog) getDialog();

        d.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = ((TextInputEditText) d.findViewById(R.id.new_story_title)).getText().toString();
                String author = ((TextInputEditText) d.findViewById(R.id.new_story_author)).getText().toString();
                String content = ((TextInputEditText) d.findViewById(R.id.new_story_content)).getText().toString();
                postStory(title, author, content);
            }
        });
    }

    private void postStory(String title, String author, String content) {

        String error = null;

        if (title.length() == 0)
            error = "Title can't be empty!";
        if (author.length() == 0)
            error = "Author can't be empty!";
        if (content.length() == 0)
            error = "Content can't be empty!";

        if (error != null) {
            showError(error);
            return;
        }

        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(getActivity(), "Not signed in!", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> words = new ArrayList<>();
        words.addAll(Arrays.asList(title.split(" ")));
        words.addAll(Arrays.asList(content.split(" ")));

        query = new LinkedHashSet<>(words);
        queryResults = new ArrayList<>();

        for (String word : query) {
            Query query = ref.child(word.toLowerCase());
            query.addListenerForSingleValueEvent(new WordListener(this));
        }

        /*List<Chapter> chapters = new ArrayList<>();
        chapters.add(new Chapter("Chapter One", content));

        DatabaseReference ref = database.getReference("stories");
        DatabaseReference newRef = ref.push();

        newRef.setValue(new Story(newRef.getKey(), user.getUid(), title, author, chapters));*/
    }


    public void addQueryResult(WordQueryResult result) {
        queryResults.add(result);

        if (queryResults.size() == query.size()) {
            List<String> notFound = new ArrayList<>();
            for (WordQueryResult word : queryResults)
                if (!word.found())
                    notFound.add(word.word());

            if (notFound.size() > 0)
                showError("Couldn't find: " + TextUtils.join(", ", notFound));
        }
    }

    private void showError(String message) {
        SimpleDialog dialog = SimpleDialog.newInstance(message);
        dialog.show(((BrowseActivity) getActivity()).getSupportFragmentManager(), "newstory");
    }
}
