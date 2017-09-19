package com.sam.words.story;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sam.words.R;
import com.sam.words.components.WordsView;
import com.sam.words.models.Post;
import com.sam.words.models.Story;

import java.util.List;

/**
 * A page in a story. Contains a single WordsView representing that page.
 */

public class StoryFragment extends Fragment {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private RecyclerView mRecyclerView;

    // Fragment arguments
    public static final String ARG_PAGE_NUMBER = "page_number";
    public static final String ARG_PAGE_COUNT = "page_count";

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
        final Story story = activity.getStory();

        int pageNum = getArguments().getInt(ARG_PAGE_NUMBER);
        int pageCnt = getArguments().getInt(ARG_PAGE_COUNT);

        View rootView;

        if (pageNum == pageCnt + 1) {

            // Poll page
            rootView = inflater.inflate(R.layout.story_poll, container, false);

            TextView pollDescription = (TextView) rootView.findViewById(R.id.poll_description);
            final EditText pollInput = (EditText) rootView.findViewById(R.id.poll_input);
            Button pollSubmit = (Button) rootView.findViewById(R.id.poll_submit);
            
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.votes_list);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(mLayoutManager);

            if (story != null) {
                Query query = database.getReference("stories").child(story.getStoryId()).child("vote");
                query.addValueEventListener(new VoteListener(this));
            }

            final FirebaseUser user = auth.getCurrentUser();

            if (user != null && story != null) {
                pollDescription.setText("Contribute to " + story.getTitle() + " by " + story.getAuthorName());

                pollSubmit.setEnabled(true);
                pollSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String post = pollInput.getText().toString();

                        database.getReference("stories")
                                .child(story.getStoryId())
                                .child("vote")
                                .child(post)
                                .setValue(new Post(story.getStoryId(), user.getUid(), user.getDisplayName()));
                    }
                });
            }

        } else {

            // Story page
            Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/CrimsonText/CrimsonText-Regular.ttf");

            rootView = inflater.inflate(R.layout.fragment_story_page, container, false);

            WordsView wordsView = (WordsView) rootView.findViewById(R.id.words_view);
            TextView pageNumberView = (TextView) rootView.findViewById(R.id.page_number);

            if (activity.getPages() != null)
                wordsView.setPage(activity.getPages().get(pageNum - 1));
            wordsView.setPageNumber(pageNum);

            pageNumberView.setTypeface(typeface);
            pageNumberView.setText("page " + pageNum + " of " + pageCnt);
        }

        return rootView;
    }

    public void gotVotes(List<Post> votes) {
        RecyclerView.Adapter mAdapter = new VoteAdapter(votes);
        mRecyclerView.setAdapter(mAdapter);
    }
}
