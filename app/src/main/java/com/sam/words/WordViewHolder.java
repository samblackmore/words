package com.sam.words;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Holds the views that make up a story card in a Browse fragment
 */

class WordViewHolder extends RecyclerView.ViewHolder {

    TextView scoreView;
    ImageView upArrow;
    ImageView downArrow;
    TextView wordView;
    TextView submittedByView;
    TextView timeAgoView;

    public WordViewHolder(View itemView, TextView scoreView, ImageView upArrow, ImageView downArrow, TextView wordView, TextView submittedByView, TextView timeAgoView) {
        super(itemView);
        this.scoreView = scoreView;
        this.upArrow = upArrow;
        this.downArrow = downArrow;
        this.wordView = wordView;
        this.submittedByView = submittedByView;
        this.timeAgoView = timeAgoView;
    }
}
