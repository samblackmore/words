package com.sam.words.story;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

/**
 * Form validation for the
 */

class PostValidation implements TextWatcher {

    private Button submitButton;

    PostValidation(Button submitButton) {
        this.submitButton = submitButton;
    }

    private boolean isValid(String s) {

        if (s.length() == 0)
            return false;

        String cleaned = s
                .replaceAll("\\.", "")
                .replaceAll(",", "")
                .replaceAll(" +", " ");

        if (cleaned.length() > 1 && cleaned.charAt(0) == ' ')
            cleaned = cleaned.substring(1, cleaned.length());

        return cleaned.split(" ").length == 3;
    }

    @Override
    public void afterTextChanged(Editable s) {
        submitButton.setEnabled(isValid(s.toString()));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
