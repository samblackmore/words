package com.sam.words;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class SimpleDialog extends DialogFragment {

    static String key = "MESSAGE";

    static SimpleDialog newInstance(String message) {
        SimpleDialog f = new SimpleDialog();
        Bundle args = new Bundle();
        args.putString(key, message);
        f.setArguments(args);
        return f;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setMessage(getArguments().getString(key))
                .setPositiveButton(R.string.try_again, null)
                .create();
    }
}
