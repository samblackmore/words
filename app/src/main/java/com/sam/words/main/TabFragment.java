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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sam.words.R;
import com.sam.words.models.Post;
import com.sam.words.models.Story;
import com.sam.words.story.GoogleSignInFragment;

import java.util.Collections;
import java.util.List;

/**
 * A fragment representing one tab in the Browse Activity
 */

public class TabFragment extends Fragment implements GoogleSignInFragment{

    private static final String ARG_TAB_SECTION = "SECTION";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    private RecyclerView mRecyclerView;
    private CardAdapter mCardAdapter;
    private SignInButton signInButton;
    private Button addStoryButton;
    private ProgressBar signInProgressBar;
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
        signInProgressBar = (ProgressBar) rootView.findViewById(R.id.sign_in_progress);
        progressBar = (ProgressBar) rootView.findViewById(R.id.loading);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.stories_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mCardAdapter = new CardAdapter();
        mRecyclerView.setAdapter(mCardAdapter);

        TabEnum section = TabEnum.getSection(getArguments().getInt(ARG_TAB_SECTION));
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference storyRef = database.getReference("stories");
        DatabaseReference activityRef = user == null ? null : database.getReference("users").child(user.getUid()).child("activity");

        switch (section) {
            case ACTIVITY:
                mCardAdapter.setActivityList(true);
                if (activityRef != null)
                    activityRef.addListenerForSingleValueEvent(new UserActivityListener(this));
                break;
            case NEW:
                storyRef.orderByChild("dateCreated").limitToLast(10).addValueEventListener(new CardStoryListener(this));
                break;
            case TOP:
                initMyStories();
                storyRef.orderByChild("likes").limitToLast(10).addValueEventListener(new CardStoryListener(this));
                break;
        }

        if (activityRef != null)
            activityRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mCardAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

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

        mCardAdapter.refresh();
    }

    public void showLoading(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            signInProgressBar.setVisibility(View.VISIBLE);
            signInButton.setVisibility(View.INVISIBLE);
            addStoryButton.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            signInProgressBar.setVisibility(View.GONE);
            if (mAuth.getCurrentUser() == null) {
                signInButton.setVisibility(View.VISIBLE);
                addStoryButton.setVisibility(View.INVISIBLE);
            } else {
                signInButton.setVisibility(View.INVISIBLE);
                addStoryButton.setVisibility(View.VISIBLE);
            }
        }
    }

    public void gotStory(Story story) {
        progressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mCardAdapter.gotStory(story);
    }

    public void gotStories(List<Story> stories) {
        progressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        Collections.reverse(stories);
        mCardAdapter.gotStories(stories);
    }

    public void gotPosts(List<Post> posts) {
        Collections.reverse(posts);
        mCardAdapter.gotPosts(posts);
    }
}
