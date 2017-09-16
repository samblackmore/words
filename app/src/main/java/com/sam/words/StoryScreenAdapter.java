package com.sam.words;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class StoryScreenAdapter extends RecyclerView.Adapter<StoryScreenHolder> {

    private Story story;
    private int pageNumber;
    private int pageCount;

    StoryScreenAdapter(Story story, int pageNumber, int pageCount) {
        this.story = story;
        this.pageNumber = pageNumber;
        this.pageCount = pageCount;
    }

    @Override
    public StoryScreenHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        Typeface typeface = Typeface.createFromAsset(
                parent.getContext().getAssets(),
                "fonts/CrimsonText/CrimsonText-Regular.ttf"
        );

        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.story_page, parent, false);

        WordsView wordsView = (WordsView) v.findViewById(R.id.words_view);
        TextView pageNumberView = (TextView) v.findViewById(R.id.page_number);
        pageNumberView.setTypeface(typeface);

        return new StoryScreenHolder(v, wordsView, pageNumberView);
    }

    @Override
    public void onBindViewHolder(final StoryScreenHolder holder, int position) {
        if (position == 0) {
            if (story != null)
            holder.wordsView.setChapters(story.getChapters());
            holder.wordsView.setPageNumber(pageNumber);
            holder.pageNumberView.setText("page " + pageNumber + " of " + pageCount);
        } else {
            holder.itemView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (pageNumber == pageCount)
            return 2;
        else
            return 1;
    }
}
