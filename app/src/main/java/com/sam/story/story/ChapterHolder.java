package com.sam.story.story;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Holds the views that make up a story card in a Browse fragment
 */

class ChapterHolder extends RecyclerView.ViewHolder {

    View itemView;
    TextView chapterTitleView;
    TextView pageNumView;

    ChapterHolder(View itemView, TextView chapterTitleView, TextView pageNumView) {
        super(itemView);
        this.itemView = itemView;
        this.chapterTitleView = chapterTitleView;
        this.pageNumView = pageNumView;
    }
}
