package com.sam.story.components;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.sam.story.R;

public class SimpleDialog extends DialogFragment {

    static String key = "MESSAGE";
    static String btn = "BUTTON";

    public static SimpleDialog newInstance(String message, int btnText) {
        SimpleDialog f = new SimpleDialog();
        Bundle args = new Bundle();
        args.putString(key, message);
        args.putInt(btn, btnText);
        f.setArguments(args);
        return f;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setMessage(getArguments().getString(key))
                .setPositiveButton(getArguments().getInt(btn), null)
                .create();
    }
}
