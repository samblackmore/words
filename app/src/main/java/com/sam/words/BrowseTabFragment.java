package com.sam.words;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing one tab in the Browse Activity
 */

public class BrowseTabFragment extends Fragment {
    // Fragment arguments
    private static final String ARG_TAB_TITLE = "TITLE";

    List<Story> mStories = new ArrayList<>();

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

        final RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.stories_list);

        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("stories");

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mStories = new ArrayList<>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Story story = child.getValue(Story.class);
                    mStories.add(story);
                }

                RecyclerView.Adapter mAdapter = new BrowseListAdapter(mStories);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        RecyclerView.Adapter mAdapter = new BrowseListAdapter(mStories);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
