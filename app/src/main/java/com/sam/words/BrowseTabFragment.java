package com.sam.words;

import android.app.DialogFragment;
import android.content.Intent;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Collections;
import java.util.List;

/**
 * A fragment representing one tab in the Browse Activity
 */

public class BrowseTabFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_TAB_SECTION = "SECTION";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("stories");

    private RecyclerView mRecyclerView;
    private Button addStoryButton;
    private ProgressBar progressBar;

    public BrowseTabFragment() {
    }

    public static BrowseTabFragment newInstance(int sectionNumber) {
        BrowseTabFragment fragment = new BrowseTabFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TAB_SECTION, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_browse_section, container, false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());

        addStoryButton = (Button) rootView.findViewById(R.id.add_story);
        progressBar = (ProgressBar) rootView.findViewById(R.id.sign_in_progress);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.stories_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Query query = null;
        FirebaseUser currentUser = mAuth.getCurrentUser();

        BrowseTab section = BrowseTab.getSection(getArguments().getInt(ARG_TAB_SECTION));

        switch (section) {
            case TOP:
                query = ref.orderByChild("likes").limitToLast(10);
                break;
            case NEW:
                query = ref.orderByChild("dateCreated").limitToLast(10);
                break;
            case ME:
                showNewStoryButton();
                query = (currentUser == null ? null : ref.orderByChild("userId").equalTo(currentUser.getUid()));
                break;
        }

        if (query != null)
            query.addValueEventListener(new BrowseDataListener(this));

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_story)
            addNewStory();
    }

    private void showNewStoryButton() {

        // Show container
        FrameLayout addStoryContainer = (FrameLayout) addStoryButton.getParent().getParent();
        addStoryContainer.setVisibility(View.VISIBLE);

        // Set up button
        int color = getResources().getColor(R.color.colorAccent);
        int textColor = getResources().getColor(R.color.white);
        addStoryButton.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        addStoryButton.setTextColor(textColor);
        addStoryButton.setOnClickListener(this);

        updateUI(mAuth.getCurrentUser());
    }

    public void updateUI(FirebaseUser user) {

        hideLoading();

        // Refresh the options menu
        BrowseActivity activity = (BrowseActivity) getActivity();
        activity.invalidateOptionsMenu();

        if (user == null) {
            addStoryButton.setText(getResources().getString(R.string.sign_in));
        } else {
            addStoryButton.setText(getResources().getString(R.string.new_story));
        }
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        addStoryButton.setText("");
        addStoryButton.setEnabled(false);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
        addStoryButton.setEnabled(true);
    }

    public void toast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    private void addNewStory() {

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            DialogFragment fragment = new NewStoryFragment();
            fragment.show(getActivity().getFragmentManager(), "newstory");
        } else {
            showLoading();
            Intent intent = new Intent(getActivity(), SignInActivity.class);
            getActivity().startActivity(intent);
            //mAuth.signInAnonymously().addOnCompleteListener(getActivity(), new SignInListener(this));
        }
    }

    public void setStories(List<Story> stories) {
        Collections.reverse(stories);
        RecyclerView.Adapter mAdapter = new BrowseListAdapter(stories);
        mRecyclerView.setAdapter(mAdapter);
    }
}
