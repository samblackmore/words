package com.sam.words.browse;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sam.words.R;
import com.sam.words.dictionary.DictionaryActivity;
import com.sam.words.models.Word;

import java.util.ArrayList;

public class AddWordsDialog extends DialogFragment {

    private static final FirebaseAuth auth = FirebaseAuth.getInstance();
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference storiesRef = database.getReference("words").child("pending");
    private static final String key = "MESSAGE";

    static AddWordsDialog newInstance(ArrayList<String> wordsToAdd) {
        AddWordsDialog f = new AddWordsDialog();
        Bundle args = new Bundle();
        args.putStringArrayList(key, wordsToAdd);
        f.setArguments(args);
        return f;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final ArrayList<String> wordsToAdd = getArguments().getStringArrayList(key);
        String message = "Couldn't find: " + TextUtils.join(", ", wordsToAdd);

        return new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton(R.string.add_words, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseUser user = auth.getCurrentUser();

                        if (user != null && wordsToAdd != null) {
                            for (String word : wordsToAdd)
                                storiesRef.child(word).setValue(new Word(user.getDisplayName()));
                        }

                        Intent intent = new Intent(getActivity(), DictionaryActivity.class);
                        getActivity().startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.try_again, null)
                .create();
    }
}
