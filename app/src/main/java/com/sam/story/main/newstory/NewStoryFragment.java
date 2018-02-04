package com.sam.story.main.newstory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sam.story.R;
import com.sam.story.components.SimpleDialog;
import com.sam.story.main.MainActivity;
import com.sam.story.utils.TextUtil;

/**
 * New story popup
 */

public class NewStoryFragment extends DialogFragment {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private NewStoryFragment newStoryFragment;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        newStoryFragment = this;

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
                String title = ((TextInputEditText) d.findViewById(R.id.new_story_title)).getText().toString();
                String author = ((TextInputEditText) d.findViewById(R.id.new_story_author)).getText().toString();
                String content = ((TextInputEditText) d.findViewById(R.id.new_story_content)).getText().toString();

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

                String inputs = title + " " + author + " " + content;

                PostStoryCallback callback = new PostStoryCallback(newStoryFragment, title, author, content);
                BadWordsCheck badWordsCheck = new BadWordsCheck(inputs, callback);
                badWordsCheck.execute();
            }
        });
    }

    private void showError(String message) {
        SimpleDialog dialog = SimpleDialog.newInstance(message, R.string.try_again);
        dialog.show(((MainActivity) getActivity()).getSupportFragmentManager(), "form-error");
    }
}
