package com.sam.words.story;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

class StoryPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int PAGE = 0;
    private final int POLL = 1;
    private List<Page> pages;
    private int pageNumber;
    private int pageCount;

    StoryPageAdapter(List<Page> pages, int pageNumber, int pageCount) {
        this.pages = pages;
        this.pageNumber = pageNumber;
        this.pageCount = pageCount;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        switch (viewType) {
            case PAGE:
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

            case POLL:
                LinearLayout v2 = (LinearLayout) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.story_poll, parent, false);

                TextView titleView = (TextView) v2.findViewById(R.id.poll_title);
                TextView authorView = (TextView) v2.findViewById(R.id.poll_author);

                return new StoryPollHolder(v2, titleView, authorView);
        }
        throw new IllegalArgumentException("Invalid viewType");
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case PAGE:
                StoryPageHolder storyHolder = (StoryPageHolder) holder;
                if (pages != null)
                    storyHolder.wordsView.setPage(pages.get(pageNumber - 1));
                storyHolder.wordsView.setPageNumber(pageNumber);
                storyHolder.pageNumberView.setText("page " + pageNumber + " of " + pageCount);
                break;

            case POLL:
                StoryPollHolder pollHolder = (StoryPollHolder) holder;
                break;
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
