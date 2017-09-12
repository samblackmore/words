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

import java.util.ArrayList;
import java.util.List;

/**
 * New story popup
 */

public class NewStoryFragment extends DialogFragment {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("stories");
    private FirebaseAuth auth = FirebaseAuth.getInstance();

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

        List<Chapter> chapters = new ArrayList<>();
        chapters.add(new Chapter("Chapter One", content));

        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(getActivity(), "Not signed in!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference newRef = ref.push();

        newRef.setValue(new Story(newRef.getKey(), user.getUid(), title, author, chapters));
    }
}
