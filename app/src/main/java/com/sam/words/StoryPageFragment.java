package com.sam.words;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A page in a story. Contains a single WordsView representing that page.
 */

public class StoryPageFragment extends Fragment {

    // Fragment arguments
    private static final String ARG_PAGE_NUMBER = "page_number";
    private static final String ARG_PAGE_CONTENT = "page_content";

    public StoryPageFragment() {
    }

    public static StoryPageFragment newInstance(int pageNumber, String content) {
        StoryPageFragment fragment = new StoryPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, pageNumber);
        args.putString(ARG_PAGE_CONTENT, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_story_page, container, false);
        WordsView wordsView = (WordsView) rootView.findViewById(R.id.words_view);
        wordsView.setText(getArguments().getString(ARG_PAGE_CONTENT)
                        + " page "
                        + getArguments().getInt(ARG_PAGE_NUMBER));
        return rootView;
    }
}
