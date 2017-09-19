package com.sam.words.story;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Holds the views that make up a story card in a Browse fragment
 */

class VoteHolder extends RecyclerView.ViewHolder {

    TextView scoreView;
    ImageView upArrow;
    TextView postView;
    TextView submittedByView;
    TextView timeAgoView;

    public VoteHolder(View itemView, TextView scoreView, ImageView upArrow, TextView postView, TextView submittedByView, TextView timeAgoView) {
        super(itemView);
        this.scoreView = scoreView;
        this.upArrow = upArrow;
        this.postView = postView;
        this.submittedByView = submittedByView;
        this.timeAgoView = timeAgoView;
    }
}
