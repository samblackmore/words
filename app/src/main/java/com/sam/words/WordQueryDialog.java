package com.sam.words;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class WordQueryDialog extends DialogFragment {

    static String key = "MESSAGE";

    static WordQueryDialog newInstance(String message) {
        WordQueryDialog f = new WordQueryDialog();

        Bundle args = new Bundle();
        args.putString(key, message);
        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(savedInstanceState.getString(key))
                .setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        WordQueryDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}
