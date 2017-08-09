package com.sam.words;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Simplify tasks dealing with shared prefs
 */

class SharedPreferencesHelper {

    static int getTextSize(Context context) {

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        int defaultTextSize = context.getResources().getInteger(R.integer.default_text_size);

        return sharedPref.getInt(context.getString(R.string.saved_text_size), defaultTextSize);
    }
}
