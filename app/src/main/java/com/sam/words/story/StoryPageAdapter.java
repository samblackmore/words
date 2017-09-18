package com.sam.words.story;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sam.words.R;
import com.sam.words.components.Page;
import com.sam.words.components.WordsView;

import java.util.List;

/**
 * The adapter on each story page which makes the page scrollable or not.
 * getItemCount() returns 2 for the last page in the story and 1 otherwise.
 */

class StoryPageAdapter extends RecyclerView.Adapter<StoryPageHolder> {

    private List<Page> pages;
    private int pageNumber;
    private int pageCount;

    StoryPageAdapter(List<Page> pages, int pageNumber, int pageCount) {
        this.pages = pages;
        this.pageNumber = pageNumber;
        this.pageCount = pageCount;
    }

    @Override
    public StoryPageHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        Typeface typeface = Typeface.createFromAsset(
                parent.getContext().getAssets(),
                "fonts/CrimsonText/CrimsonText-Regular.ttf"
        );

        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.story_page, parent, false);

        WordsView wordsView = (WordsView) v.findViewById(R.id.words_view);
        TextView pageNumberView = (TextView) v.findViewById(R.id.page_number);
        pageNumberView.setTypeface(typeface);

        return new StoryPageHolder(v, wordsView, pageNumberView);
    }

    @Override
    public void onBindViewHolder(final StoryPageHolder holder, int position) {
        if (position == 0) {
            if (pages != null)
            holder.wordsView.setPage(pages.get(pageNumber - 1));
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
