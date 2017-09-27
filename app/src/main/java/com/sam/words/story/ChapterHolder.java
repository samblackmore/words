package com.sam.words.story;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Holds the views that make up a story card in a Browse fragment
 */

class ChapterHolder extends RecyclerView.ViewHolder {

    TextView wordsView;

    public ChapterHolder(View itemView, TextView wordsView) {
        super(itemView);
        this.wordsView = wordsView;
    }
}
