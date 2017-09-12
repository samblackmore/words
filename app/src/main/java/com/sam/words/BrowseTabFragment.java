package com.sam.words;

import android.app.DialogFragment;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

public class BrowseTabFragment extends Fragment implements View.OnClickListener {
    // Fragment arguments
    private static final String ARG_TAB_TITLE = "TITLE";

    private FirebaseAuth mAuth;
    private Button addStoryButton;
    private ProgressBar progressBar;
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
            progressBar = (ProgressBar) rootView.findViewById(R.id.sign_in_progress);
            FrameLayout addStoryContainer = (FrameLayout) rootView.findViewById(R.id.add_story_container);
            addStoryContainer.setVisibility(View.VISIBLE);

            // Set up button
            addStoryButton = (Button) rootView.findViewById(R.id.add_story);
            int color = getResources().getColor(R.color.colorAccent);
            int textColor = getResources().getColor(R.color.white);
            addStoryButton.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            addStoryButton.setTextColor(textColor);

            // Check if signed in
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser == null)
                signOut();

            addStoryButton.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_story)
            addNewStory();
    }

    public void signIn() {
        addStoryButton.setText(getResources().getString(R.string.new_story));
    }

    public void signOut() {
        addStoryButton.setText(getResources().getString(R.string.sign_in));
    }

    private void addNewStory() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {

            // Already signed in
            DialogFragment fragment = new NewStoryFragment();
            fragment.show(getActivity().getFragmentManager(), "newstory");

        } else {

            // Change to sign in button
            signOut();
            progressBar.setVisibility(View.VISIBLE);
            addStoryButton.setText("");
            addStoryButton.setEnabled(false);

            mAuth.signInAnonymously().addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    // Stop loading
                    progressBar.setVisibility(View.GONE);
                    addStoryButton.setEnabled(true);

                    if (task.isSuccessful()) {
                        signIn();

                        // Add the sign out button to options menu
                        BrowseActivity activity = (BrowseActivity) getActivity();
                        activity.invalidateOptionsMenu();

                        Toast.makeText(getActivity(), "Authentication success!", Toast.LENGTH_SHORT).show();
                    } else {
                        signOut();
                        Toast.makeText(getActivity(), "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
