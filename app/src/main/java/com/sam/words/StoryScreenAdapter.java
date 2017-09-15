package com.sam.words;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class StoryScreenAdapter extends RecyclerView.Adapter<StoryScreenHolder> {

    private List<Word> mDataset = new ArrayList<>();

    /*StoryScreenAdapter(List<Word> myDataset) {
        mDataset = myDataset;
    }*/

    @Override
    public StoryScreenHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.story_page, parent, false);

        WordsView wordsView = (WordsView) v.findViewById(R.id.words_view);
        TextView pageNumberView = (TextView) v.findViewById(R.id.page_number);

        return new StoryScreenHolder(v, wordsView, pageNumberView);
    }

    @Override
    public void onBindViewHolder(final StoryScreenHolder holder, int position) {
        //final Word word = mDataset.get(position);

        /*

        wordsView = (WordsView) rootView.findViewById(R.id.words_view);
        wordsView.setPageNumber(getArguments().getInt(ARG_PAGE_NUMBER));
        if (story != null)
            wordsView.setChapters(story.getChapters());

        pollView = (TextView) rootView.findViewById(R.id.poll_container);

        TextView pageNumberView = (TextView) rootView.findViewById(R.id.page_number);
        pageNumberView.setText("page " + getArguments().getInt(ARG_PAGE_NUMBER) + " of " + getArguments().getInt(ARG_PAGE_COUNT));
        pageNumberView.setTypeface(typeface);*/

        if (position == 0)
            holder.itemView.setVisibility(View.INVISIBLE);

        holder.pageNumberView.setText(String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
