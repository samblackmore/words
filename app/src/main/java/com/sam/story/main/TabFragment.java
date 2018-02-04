package com.sam.story.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sam.story.R;
import com.sam.story.models.Notifications;
import com.sam.story.models.Post;
import com.sam.story.models.Story;
import com.sam.story.story.GoogleSignInFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
    private ProgressBar signInProgressBar;
    private ProgressBar progressBar;
    private TextView tabMessage;
    private Long userStoryCount;

    public TabFragment() {
    }

    public static TabFragment newInstance(int sectionNumber) {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TAB_SECTION, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    /*@Override
    public void onResume(Bundle bundle) {

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_browse_section, container, false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());

        signInButton = (SignInButton) rootView.findViewById(R.id.sign_in_button);
        signInProgressBar = (ProgressBar) rootView.findViewById(R.id.sign_in_progress);
        progressBar = (ProgressBar) rootView.findViewById(R.id.loading);
        tabMessage = (TextView) rootView.findViewById(R.id.tab_message);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.stories_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mCardAdapter = new CardAdapter();
        mRecyclerView.setAdapter(mCardAdapter);

        TabEnum section = TabEnum.getSection(getArguments().getInt(ARG_TAB_SECTION));
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference storyRef = database.getReference("stories");

        switch (section) {
            case ACTIVITY:
                mCardAdapter.setActivityList(true);
                if (user != null) {
                    DatabaseReference userRef = database.getReference("users").child(user.getUid());
                    userRef.child("activity").addValueEventListener(new UserActivityListener(this));
                    userRef.child("stories").addValueEventListener(new UserStoryListener(this));
                }
                signInButton.setOnClickListener((MainActivity) getActivity());
                mRecyclerView.setVisibility(View.GONE);
                signInButton.setVisibility(user != null ? View.GONE : View.VISIBLE);
                progressBar.setVisibility(user != null ? View.VISIBLE : View.GONE);
                if (user == null) {
                    tabMessage.setVisibility(View.VISIBLE);
                    //tabMessage.setText("Welcome to 3 Words!\n\nSign in to start your own story\nor swipe to browse other people's");
                    tabMessage.setText("Welcome to 3 Words!\n\nSwipe to browse ongoing stories or\nsign in to start your own");
                }
                break;
            case NEW:
                storyRef.orderByChild("dateCreated").limitToLast(10).addValueEventListener(new CardStoryListener(this));
                break;
            case TOP:
                storyRef.orderByChild("likeCount").limitToLast(10).addValueEventListener(new CardStoryListener(this));
                break;
        }

        MainActivity activity = (MainActivity) getActivity();
        activity.showAddStoryButton(user != null);

        return rootView;
    }

    @Override
    public void onSignInSignOut(FirebaseUser firebaseUser) {
        // All tab fragments need to reset so sign out the whole activity
        MainActivity activity = (MainActivity) getActivity();
        activity.onSignInSignOut();
    }

    public void showSignInLoading(boolean show) {
        signInProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show)
            signInButton.setVisibility(View.INVISIBLE);
    }

    public void gotUserStoryCount(long count) {
        userStoryCount = count;
    }

    public void gotStory(Story story) {
        progressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mCardAdapter.gotStory(story);

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            int myActiveCount = 0;
            ArrayList<Story> myStories = new ArrayList<>();
            MainActivity activity = (MainActivity) getActivity();

            for (Story s : mCardAdapter.getStories()) {
                if (s.getUserId().equals(user.getUid())) {
                    myStories.add(s);
                    myActiveCount += s.isFinished() ? 0 : 1;
                }
            }
            if (userStoryCount != null && myStories.size() == userStoryCount)
                activity.gotActiveStoryCount(myActiveCount);
        }
    }

    public void gotActivity(HashMap<String, Notifications> activity) {
        if (activity.size() == 0) {
            progressBar.setVisibility(View.GONE);
            tabMessage.setVisibility(View.VISIBLE);
            tabMessage.setText("No activity to show.\nTry contributing to a story!");
            mRecyclerView.setVisibility(View.GONE);
        } else {
            tabMessage.setVisibility(View.GONE);
            mCardAdapter.gotActivity(activity);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
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
