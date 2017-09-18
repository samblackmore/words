package com.sam.words.story;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sam.words.R;
import com.sam.words.components.Page;
import com.sam.words.models.Story;

import org.w3c.dom.Text;

import java.util.List;

/**
 * A page in a story. Contains a single WordsView representing that page.
 */

public class StoryFragment extends Fragment {

    // Fragment arguments
    public static final String ARG_PAGE_NUMBER = "page_number";
    private static final String ARG_PAGE_COUNT = "page_count";

    private RecyclerView mRecyclerView;
    //private TextView pollView;

    public StoryFragment() {
    }

    public static StoryFragment newInstance(int pageNumber, int pageCount) {
        StoryFragment fragment = new StoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, pageNumber);
        args.putInt(ARG_PAGE_COUNT, pageCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StoryActivity activity = ((StoryActivity) getActivity());
        int pageNum = getArguments().getInt(ARG_PAGE_NUMBER);
        int pageCnt = getArguments().getInt(ARG_PAGE_COUNT);

        View rootView = inflater.inflate(R.layout.fragment_story_page, container, false);
        TextView pollContainer = (TextView) rootView.findViewById(R.id.poll_container);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        RecyclerView.Adapter mAdapter = new StoryPageAdapter(activity.getStory(), pageNum, pageCnt);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.screen_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        if (pageNum == pageCnt) {
            List<Page> pages = activity.getPages();
            pollContainer.setVisibility(View.VISIBLE);
            pollContainer.setY(pages.get(pages.size() - 1).getWordsBottom());
        }

        return rootView;
    }

    public void gotWordsBottom(int y) {
        //pollView.setVisibility(View.VISIBLE);
        //pollView.setY(y);
    }
}
