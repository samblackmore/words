package com.sam.words;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A page in a story. Contains a single WordsView representing that page.
 */

public class StoryPageFragment extends Fragment {

    // Fragment arguments
    private static final String ARG_PAGE_NUMBER = "page_number";
    private static final String ARG_PAGE_CONTENT = "page_content";

    private static Typeface typeface;
    private WordsView wordsView;

    public StoryPageFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        typeface = Typeface.createFromAsset(
                context.getAssets(),
                "fonts/CrimsonText/CrimsonText-Regular.ttf"
        );
    }

    public static StoryPageFragment newInstance(int pageNumber) {
        StoryPageFragment fragment = new StoryPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_story_page, container, false);
        TextView pageNumberView = (TextView) rootView.findViewById(R.id.page_number);
        wordsView = (WordsView) rootView.findViewById(R.id.words_view);

        wordsView.setChapters(((StoryActivity) getActivity()).getStory().getChapters());
        wordsView.setPageNumber(getArguments().getInt(ARG_PAGE_NUMBER));
        pageNumberView.setText("page " + getArguments().getInt(ARG_PAGE_NUMBER));
        pageNumberView.setTypeface(typeface);
        return rootView;
    }

    public void updateStory(Story story) {
        wordsView.setChapters(story.getChapters());
    }
}
