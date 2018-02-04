package com.sam.story.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.sam.story.R;

/**
 * Simplify tasks dealing with shared prefs
 */

public class SharedPreferencesHelper {

    public static int getTextSize(Context context) {

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        int defaultTextSize = context.getResources().getInteger(R.integer.default_text_size);

        return sharedPref.getInt(context.getString(R.string.saved_text_size), defaultTextSize);
    }
}
