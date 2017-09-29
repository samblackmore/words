package com.sam.words.main.newstory;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.sam.words.R;

import java.util.ArrayList;

public class BadWordsDialog extends DialogFragment {

    private static final String key = "MESSAGE";

    static BadWordsDialog newInstance(ArrayList<String> badWords) {
        BadWordsDialog f = new BadWordsDialog();
        Bundle args = new Bundle();
        args.putStringArrayList(key, badWords);
        f.setArguments(args);
        return f;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final ArrayList<String> wordsToAdd = getArguments().getStringArrayList(key);
        String message = "Sorry, bad words: " + TextUtils.join(", ", wordsToAdd);

        return new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton(R.string.try_again, null)
                .create();
    }
}
