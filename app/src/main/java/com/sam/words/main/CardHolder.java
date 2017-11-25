package com.sam.words.main;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sam.words.components.WordsView;

/**
 * Holds the views that make up a story card in a Browse fragment
 */

class CardHolder extends RecyclerView.ViewHolder {

    View mItemView;
    CardView mCardView;
    TextView mHeaderView;
    TextView mNewPostsView;
    TextView mNewChaptersView;
    TextView mNewContributorsView;
    TextView mLikesView;
    TextView mDateView;
    TextView mTitleView;
    TextView mAuthorView;
    WordsView mWordsView;

    CardHolder(View item, CardView cardView, TextView headerView, TextView newPosts, TextView newChapters, TextView newContributors, TextView likes, TextView date, TextView title, TextView author, WordsView words) {
        super(item);
        mItemView = item;
        mCardView = cardView;
        mHeaderView = headerView;
        mLikesView = likes;
        mDateView = date;
        mTitleView = title;
        mAuthorView = author;
        mWordsView = words;
        mNewPostsView = newPosts;
        mNewChaptersView = newChapters;
        mNewContributorsView = newContributors;
    }
}
