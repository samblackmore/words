package com.sam.words.main.newstory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sam.words.R;
import com.sam.words.components.SimpleDialog;
import com.sam.words.main.MainActivity;
import com.sam.words.models.Chapter;
import com.sam.words.models.Story;

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
    private List<String> foundWords;
    private String title;
    private String author;
    private String content;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_new_story, null);
        TextInputEditText authorView = (TextInputEditText) view.findViewById(R.id.new_story_author);

        FirebaseUser user = auth.getCurrentUser();

        if (user != null)
            authorView.setText(user.getDisplayName());

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(getString(R.string.new_story))
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
                title = ((TextInputEditText) d.findViewById(R.id.new_story_title)).getText().toString();
                author = ((TextInputEditText) d.findViewById(R.id.new_story_author)).getText().toString();
                content = ((TextInputEditText) d.findViewById(R.id.new_story_content)).getText().toString();

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

                List<String> words = new ArrayList<>();
                words.addAll(Arrays.asList(title.toLowerCase().split(" ")));
                words.addAll(Arrays.asList(content.toLowerCase().split(" ")));

                query = new LinkedHashSet<>(words);
                foundWords = new ArrayList<>();

                for (String word : query)
                    ref.child(word).addListenerForSingleValueEvent(new WordsFoundListener(NewStoryFragment.this));
            }
        });
    }

    private void postStory() {
        List<Chapter> chapters = new ArrayList<>();
        chapters.add(new Chapter("Chapter One", content));

        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(getActivity(), "Not signed in!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference storiesRef = database.getReference("stories");
        DatabaseReference newRef = storiesRef.push();

        Story tmp = new Story(newRef.getKey(), user.getUid(), title, author, chapters);

        newRef.setValue(tmp);
        
        getDialog().dismiss();
    }

    public void foundWord(String word) {
        foundWords.add(word);

        if (foundWords.size() == query.size()) {
            /*query.removeAll(foundWords);
            if (query.size() > 0)
                showError(query);
            else*/
                postStory();
        }
    }

    private void showError(String message) {
        SimpleDialog dialog = SimpleDialog.newInstance(message);
        dialog.show(((MainActivity) getActivity()).getSupportFragmentManager(), "wordserror");
    }

    private void showError(LinkedHashSet<String> query) {
        WordsNotFoundDialog dialog = WordsNotFoundDialog.newInstance(new ArrayList<>(query));
        dialog.show(((MainActivity) getActivity()).getSupportFragmentManager(), "addwords");
    }
}
