package com.sam.words.story;

import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sam.words.components.WordsView;

class StoryPollHolder extends RecyclerView.ViewHolder {

    LinearLayout itemView;
    TextView titleView;
    TextView authorView;

    public StoryPollHolder(LinearLayout itemView, TextView titleView, TextView authorView) {
        super(itemView);
        this.itemView = itemView;
        this.titleView = titleView;
        this.authorView = authorView;
    }
}
