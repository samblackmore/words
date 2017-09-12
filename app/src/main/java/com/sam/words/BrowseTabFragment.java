package com.sam.words;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing one tab in the Browse Activity
 */

public class BrowseTabFragment extends Fragment {
    // Fragment arguments
    private static final String ARG_TAB_TITLE = "TITLE";

    private FirebaseAuth mAuth;
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

        if (getArguments().getInt(ARG_TAB_TITLE) == 3) {

            // Show the new story button
            RelativeLayout addStoryContainer = (RelativeLayout) rootView.findViewById(R.id.add_story_container);
            addStoryContainer.setVisibility(View.VISIBLE);

            // Set up button
            Button addStoryButton = (Button) rootView.findViewById(R.id.add_story);
            int color = getResources().getColor(R.color.colorAccent);
            int textColor = getResources().getColor(R.color.white);
            addStoryButton.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            addStoryButton.setTextColor(textColor);

            // Check if signed in
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser == null) {
                addStoryButton.setText(getResources().getString(R.string.sign_in));
            } else {
                addStoryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFragment fragment = new NewStoryFragment();
                        fragment.show(getActivity().getFragmentManager(), "newstory");
                    }
                });
            }
        }


        //TextView textView = (TextView) rootView.findViewById(R.id.section_label);;

        final RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.stories_list);

        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("stories");

        Query query = null;

        switch (getArguments().getInt(ARG_TAB_TITLE)) {
            case 1: query = ref.orderByChild("likes"); break;
            case 2: query = ref.orderByChild("dateCreated"); break;
            case 3: query = ref.orderByChild("author").equalTo("Sam"); break;
        }

        assert query != null;

        query.addValueEventListener(new ValueEventListener() {
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
                Toast.makeText(getContext(), "Failed to get stories! " + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
