package com.sam.words.story;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Form validation for the
 */

class PostValidation implements TextWatcher {

    private boolean isTitle;
    private EditText editText;
    private Button submitButton;

    PostValidation(EditText editText, Button submitButton) {
        this.submitButton = submitButton;
        this.editText = editText;
        isTitle = false;
    }

    PostValidation(Button submitButton, boolean isTitle) {
        this.submitButton = submitButton;
        this.isTitle = isTitle;
    }

    public void isTitle(boolean isTitle) {
        this.isTitle = isTitle;
    }

    private boolean isValid(String s) {

        if (s.length() == 0)
            return false;

        String cleaned = s
                .replaceAll("[\n↩]", " ")
                .replaceAll("[^a-zA-Z0-9 ]", "")
                .replaceAll(" +", " ");

        if (cleaned.length() > 1 && cleaned.charAt(0) == ' ')
            cleaned = cleaned.substring(1, cleaned.length());

        int wordCount = cleaned.split(" ").length;

        return isTitle ? wordCount > 0 && wordCount < 4 : wordCount == 3;
    }

    @Override
    public void afterTextChanged(Editable s) {

        String newText = s.toString();

        String re = "↩(?!\n)";

        Pattern p = Pattern.compile(re);
        Matcher m = p.matcher(newText);

        if (m.find()) {
            newText = newText.replaceAll(re, " ");
            editText.setText(newText);
            editText.setSelection(newText.length());
        }

        re = "(?<!↩)\n";

        p = Pattern.compile(re);
        m = p.matcher(newText);

        if (m.find()) {
            newText = newText.replaceAll(re, "↩\n");
            editText.setText(newText);
            editText.setSelection(newText.length());
        }
        
        submitButton.setEnabled(isValid(s.toString()));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
