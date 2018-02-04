package com.sam.story.story;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Holds the views that make up a story card in a Browse fragment
 */

class VoteHolder extends RecyclerView.ViewHolder {

    ConstraintLayout clickableView;
    TextView scoreView;
    ImageView upArrow;
    TextView postView;
    TextView submittedByView;
    TextView timeAgoView;

    public VoteHolder(View itemView, ConstraintLayout clickableView, TextView scoreView, ImageView upArrow, TextView postView, TextView submittedByView, TextView timeAgoView) {
        super(itemView);
        this.clickableView = clickableView;
        this.scoreView = scoreView;
        this.upArrow = upArrow;
        this.postView = postView;
        this.submittedByView = submittedByView;
        this.timeAgoView = timeAgoView;
    }
}
