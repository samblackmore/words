package com.sam.words.story;

import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sam.words.components.WordsView;

/**
 * Holds the views that make up a story card in a Browse fragment
 */

class StoryScreenHolder extends RecyclerView.ViewHolder {

    RelativeLayout itemView;
    WordsView wordsView;
    TextView pageNumberView;

    public StoryScreenHolder(RelativeLayout itemView, WordsView wordsView, TextView pageNumberView) {
        super(itemView);
        this.itemView = itemView;
        this.wordsView = wordsView;
        this.pageNumberView = pageNumberView;
    }
}
