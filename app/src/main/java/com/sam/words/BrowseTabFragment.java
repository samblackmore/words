package com.sam.words;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A fragment representing one tab in the Browse Activity
 */

public class BrowseTabFragment extends Fragment {
    // Fragment arguments
    private static final String ARG_TAB_TITLE = "TITLE";

    public BrowseTabFragment() {
    }

    public static BrowseTabFragment newInstance(int sectionNumber) {
        BrowseTabFragment fragment = new BrowseTabFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TAB_TITLE, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_browse_section, container, false);
        //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_TAB_TITLE)));

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.stories_list);

        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        String[] myDataset = {
                "hi",
                "ho",
                "silver"
        };

        RecyclerView.Adapter mAdapter = new StoryListAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
