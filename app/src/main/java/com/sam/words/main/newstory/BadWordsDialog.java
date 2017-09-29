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

        final ArrayList<String> badWords = getArguments().getStringArrayList(key);
        String message = "Sorry, the words " + niceList(badWords) + " are not allowed in your story!";

        return new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton(R.string.try_again, null)
                .create();
    }

    private String quote(String word) {
        return "\"" + word + "\"";
    }

    private String niceList(ArrayList<String> words) {

        if (words.size() == 1)
            return quote(words.get(0));

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < words.size(); i++) {

            builder.append(quote(words.get(i)));

            if (i < words.size() - 2)
                builder.append(", ");
            else if (i < words.size() - 1)
                builder.append(" and ");
        }

        return builder.toString();
    }
}
