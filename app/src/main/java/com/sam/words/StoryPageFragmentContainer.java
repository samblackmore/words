package com.sam.words;

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

import java.util.Collections;
import java.util.List;

/**
 * A fragment representing one tab in the Browse Activity
 */

public class StoryPageFragmentContainer extends Fragment {

    private static final String ARG_TAB_SECTION = "SECTION";

    public StoryPageFragmentContainer() {
    }

    public static StoryPageFragmentContainer newInstance(int sectionNumber) {
        StoryPageFragmentContainer fragment = new StoryPageFragmentContainer();
        Bundle args = new Bundle();
        args.putInt(ARG_TAB_SECTION, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_browse_section, container, false);




        return rootView;
    }

    public void setStories(List<Story> stories) {
        Collections.reverse(stories);

    }
}
