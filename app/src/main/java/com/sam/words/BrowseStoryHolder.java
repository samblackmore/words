package com.sam.words;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

/**
 * Holds the views that make up a story card in a Browse fragment
 */

class BrowseStoryHolder extends RecyclerView.ViewHolder {

    CardView mCardView;
    TextView mLikesView;
    TextView mDateView;
    TextView mTitleView;
    TextView mAuthorView;
    WordsView mWordsView;

    BrowseStoryHolder(CardView card, TextView likes, TextView date, TextView title, TextView author, WordsView words) {
        super(card);
        mCardView = card;
        mLikesView = likes;
        mDateView = date;
        mTitleView = title;
        mAuthorView = author;
        mWordsView = words;
    }
}