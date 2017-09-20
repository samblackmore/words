package com.sam.words.story;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sam.words.R;
import com.sam.words.components.WordsView;
import com.sam.words.models.Post;
import com.sam.words.models.Story;
import com.sam.words.models.Vote;

import java.util.List;

/**
 * A page in a story. Contains a single WordsView representing that page.
 */

public class StoryFragment extends Fragment implements View.OnClickListener{

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private RecyclerView mRecyclerView;
    private EditText pollInput;
    private TextView timer;
    private Story story;
    private Vote currentVote;

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
        story = activity.getStory();

        int pageNum = getArguments().getInt(ARG_PAGE_NUMBER);
        int pageCnt = getArguments().getInt(ARG_PAGE_COUNT);

        View rootView;

        if (pageNum == pageCnt + 1) {

            // Poll page
            rootView = inflater.inflate(R.layout.story_poll, container, false);

            TextView pollDescription = (TextView) rootView.findViewById(R.id.poll_description);
            Button pollSubmit = (Button) rootView.findViewById(R.id.poll_submit);
            pollInput = (EditText) rootView.findViewById(R.id.poll_input);
            timer = (TextView) rootView.findViewById(R.id.timer);
            
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.votes_list);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(mLayoutManager);

            if (story != null) {
                DatabaseReference ref = database.getReference("stories").child(story.getStoryId());

                ref.child("votes").limitToLast(1).addValueEventListener(new VoteListener(this));
                ref.child("voteEnds").addValueEventListener(new VoteEndListener(this));
            }

            FirebaseUser user = auth.getCurrentUser();

            if (user != null && story != null) {
                pollDescription.setText("Contribute to " + story.getTitle() + " by " + story.getAuthorName());

                pollSubmit.setEnabled(true);
                pollSubmit.setOnClickListener(this);
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

    public void gotVote(Vote vote) {
        if (vote != null && currentVote != null && !vote.getId().equals(currentVote.getId()))
            Toast.makeText(getContext(), "New voting round!", Toast.LENGTH_SHORT).show();
        currentVote = vote;
        if (vote != null)
            gotPosts(vote.getPosts());
    }

    public void gotPosts(List<Post> posts) {
        Toast.makeText(getContext(), "Got posts!", Toast.LENGTH_SHORT).show();
        RecyclerView.Adapter mAdapter = new VoteAdapter(posts);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void gotTimer(Integer timeout) {

        if (timeout == null) {
            timer.setText("Timer not set");
            return;
        }

        long time = System.currentTimeMillis();

        if (time < timeout) {
            new CountDownTimer(timeout - time, 1000) {

                public void onTick(long millisUntilFinished) {
                    timer.setText("seconds remaining: " + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    timer.setText("done!");
                }
            }.start();
        } else {

        }
    }

    @Override
    public void onClick(View v) {

        FirebaseUser user = auth.getCurrentUser();

        switch (v.getId()) {
            case R.id.poll_submit:

                if (currentVote != null) {
                    String post = pollInput.getText().toString();

                    database.getReference("stories")
                            .child(story.getStoryId())
                            .child("votes")
                            .child(currentVote.getId())
                            .child("posts")
                            .child(String.valueOf(currentVote.getPosts().size()))
                            .setValue(new Post(story.getStoryId(), user.getUid(), user.getDisplayName(), post));
                }

                break;
        }
    }
}
