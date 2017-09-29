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
import com.google.firebase.database.FirebaseDatabase;
import com.sam.words.R;
import com.sam.words.components.SimpleDialog;
import com.sam.words.main.MainActivity;
import com.sam.words.models.Chapter;
import com.sam.words.models.Poll;
import com.sam.words.models.Post;
import com.sam.words.models.Story;
import com.sam.words.utils.TextUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * New story popup
 */

public class NewStoryFragment extends DialogFragment {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
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
        final TextInputEditText titleView = (TextInputEditText) view.findViewById(R.id.new_story_title);
        TextInputEditText authorView = (TextInputEditText) view.findViewById(R.id.new_story_author);

        titleView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    titleView.setText(TextUtil.capitalize(titleView.getText().toString()));
                }
            }
        });

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

                // Convert to set to remove duplicates, Linked preserves order for error message
                query = new LinkedHashSet<>(words);
                foundWords = new ArrayList<>();

                for (String word : query) {
                    String sanitized = word.replaceAll("[^a-zA-Z0-9\\-]", "");
                    database.getReference("bad-words").child(sanitized)
                            .addListenerForSingleValueEvent(new WordsFoundListener(NewStoryFragment.this));
                }
            }
        });
    }

    private void postStory() {
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(getActivity(), "Not signed in!", Toast.LENGTH_SHORT).show();
            return;
        }

        int chapter = 0;
        int pollRound = 0;
        String chapterId = String.valueOf(chapter);
        String newPollId = String.valueOf(pollRound);

        String userId = user.getUid();
        String newStoryId = database.getReference("stories").push().getKey();
        String newPostId = database.getReference("posts").child(newStoryId).child(chapterId).push().getKey();

        Story newStory = new Story(title, userId, author);
        Post newPost = new Post(newStoryId, userId, user.getDisplayName(), content);
        Poll newPoll = new Poll(pollRound);
        Chapter firstChapter = new Chapter(chapter, null);
        newStory.addChapter(firstChapter);
        newStory.addLike(userId);
        newStory.addContributor(userId);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/stories/" + newStoryId, newStory);
        childUpdates.put("/posts/" + newStoryId + "/" + chapterId + "/" + newPostId, newPost);
        childUpdates.put("/poll/" + newStoryId + "/" + chapterId + "/" + newPollId, newPoll);
        childUpdates.put("/users/" + userId + "/activity/" + newStoryId + "/postCount", 1);
        childUpdates.put("/users/" + userId + "/activity/" + newStoryId + "/chapterCount", 1);
        childUpdates.put("/users/" + userId + "/activity/" + newStoryId + "/contributorsCount", 1);
        childUpdates.put("/users/" + userId + "/stories/" + newStoryId, true);
        childUpdates.put("/users/" + userId + "/posts/" + newPostId, true);

        database.getReference().updateChildren(childUpdates);
        
        getDialog().dismiss();
    }

    public void foundWord(String word) {
        foundWords.add(word);

        if (foundWords.size() == query.size()) {

            ArrayList<String> badWords = new ArrayList<>();
            for (String foundWord : foundWords)
                if (foundWord != null)
                    badWords.add(foundWord);

            if (badWords.size() > 0)
                showError(badWords);
            else
                postStory();
        }
    }

    private void showError(String message) {
        SimpleDialog dialog = SimpleDialog.newInstance(message);
        dialog.show(((MainActivity) getActivity()).getSupportFragmentManager(), "form-error");
    }

    private void showError(ArrayList<String> badWords) {
        BadWordsDialog dialog = BadWordsDialog.newInstance(badWords);
        dialog.show(((MainActivity) getActivity()).getSupportFragmentManager(), "bad-words");
    }
}
