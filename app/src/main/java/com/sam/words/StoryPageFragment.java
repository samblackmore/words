package com.sam.words;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A page in a story. Contains a single WordsView representing that page.
 */

public class StoryPageFragment extends Fragment {

    // Fragment arguments
    public static final String ARG_PAGE_NUMBER = "page_number";
    private static final String ARG_PAGE_COUNT = "page_count";

    private RecyclerView mRecyclerView;
    //private static Typeface typeface;
    //private WordsView wordsView;
    //private TextView pollView;

    public StoryPageFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        /*typeface = Typeface.createFromAsset(
                context.getAssets(),
                "fonts/CrimsonText/CrimsonText-Regular.ttf"
        );*/
    }

    public static StoryPageFragment newInstance(int pageNumber, int pageCount) {
        StoryPageFragment fragment = new StoryPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, pageNumber);
        args.putInt(ARG_PAGE_COUNT, pageCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_browse_section, container, false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());

        Story story = ((StoryActivity) getActivity()).getStory();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.stories_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        /*

        wordsView = (WordsView) rootView.findViewById(R.id.words_view);
        wordsView.setPageNumber(getArguments().getInt(ARG_PAGE_NUMBER));
        if (story != null)
            wordsView.setChapters(story.getChapters());

        pollView = (TextView) rootView.findViewById(R.id.poll_container);

        TextView pageNumberView = (TextView) rootView.findViewById(R.id.page_number);
        pageNumberView.setText("page " + getArguments().getInt(ARG_PAGE_NUMBER) + " of " + getArguments().getInt(ARG_PAGE_COUNT));
        pageNumberView.setTypeface(typeface);*/

        return rootView;
    }

    public void updateStory(Story story) {
        RecyclerView.Adapter mAdapter = new StoryScreenAdapter(story);
        mRecyclerView.setAdapter(mAdapter);
        //wordsView.setChapters(story.getChapters());
    }

    public void gotWordsBottom(int y) {
        //pollView.setVisibility(View.VISIBLE);
        //pollView.setY(y);
    }

    //public WordsView getWordsView() {
    //    return wordsView;
    //}
}
