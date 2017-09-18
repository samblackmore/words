package com.sam.words.main;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sam.words.R;
import com.sam.words.models.Story;

import java.util.Collections;
import java.util.List;

/**
 * A fragment representing one tab in the Browse Activity
 */

public class TabFragment extends Fragment {

    private static final String ARG_TAB_SECTION = "SECTION";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("stories");

    private RecyclerView mRecyclerView;
    private SignInButton signInButton;
    private Button addStoryButton;
    private ProgressBar progressBar;

    public TabFragment() {
    }

    public static TabFragment newInstance(int sectionNumber) {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TAB_SECTION, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_browse_section, container, false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());

        signInButton = (SignInButton) rootView.findViewById(R.id.sign_in_button);
        addStoryButton = (Button) rootView.findViewById(R.id.add_story);
        progressBar = (ProgressBar) rootView.findViewById(R.id.sign_in_progress);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.stories_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Query query = null;
        FirebaseUser currentUser = mAuth.getCurrentUser();

        TabEnum section = TabEnum.getSection(getArguments().getInt(ARG_TAB_SECTION));

        switch (section) {
            case TOP:
                query = ref.orderByChild("likes").limitToLast(10);
                break;
            case NEW:
                query = ref.orderByChild("dateCreated").limitToLast(10);
                break;
            case ME:
                initMyStories();
                query = (currentUser == null ? null : ref.orderByChild("userId").equalTo(currentUser.getUid()));
                break;
        }

        if (query != null)
            query.addListenerForSingleValueEvent(new CardListener(this));

        return rootView;
    }

    private void initMyStories() {

        // Show container
        FrameLayout addStoryContainer = (FrameLayout) addStoryButton.getParent().getParent();
        addStoryContainer.setVisibility(View.VISIBLE);

        // Set up button
        int color = getResources().getColor(R.color.colorAccent);
        int textColor = getResources().getColor(R.color.white);
        addStoryButton.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        addStoryButton.setTextColor(textColor);
        addStoryButton.setOnClickListener((MainActivity) getActivity());
        signInButton.setOnClickListener((MainActivity) getActivity());

        updateUI(mAuth.getCurrentUser());
    }

    public void updateUI(FirebaseUser user) {

        showLoading(false);

        // Refresh the options menu
        MainActivity activity = (MainActivity) getActivity();
        activity.invalidateOptionsMenu();

        if (user == null) {
            signInButton.setVisibility(View.VISIBLE);
            addStoryButton.setVisibility(View.INVISIBLE);
        } else {
            signInButton.setVisibility(View.INVISIBLE);
            addStoryButton.setVisibility(View.VISIBLE);
        }
    }

    public void showLoading(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            signInButton.setVisibility(View.INVISIBLE);
            addStoryButton.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            signInButton.setVisibility(View.INVISIBLE);
            addStoryButton.setVisibility(View.VISIBLE);
        }
    }

    public void setStories(List<Story> stories) {
        Collections.reverse(stories);
        RecyclerView.Adapter mAdapter = new CardAdapter(stories);
        mRecyclerView.setAdapter(mAdapter);
    }
}
