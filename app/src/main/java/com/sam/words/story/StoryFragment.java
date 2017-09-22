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
import com.sam.words.R;
import com.sam.words.components.WordsView;
import com.sam.words.models.Post;
import com.sam.words.models.Story;
import com.sam.words.models.Poll;
import com.sam.words.utils.TimeAgo;

import java.util.List;

/**
 * A page in a story. Contains a single WordsView representing that page.
 */

public class StoryFragment extends Fragment implements View.OnClickListener{

    //private final int COUNTDOWN_LENGTH = 5 * 60 * 1000;
    private final int COUNTDOWN_LENGTH = 30 * 1000;

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private StoryActivity activity;
    private RecyclerView mRecyclerView;
    private TextView pollTitle;
    private EditText pollInput;
    private TextView timerText;
    private CountDownTimer timer;
    private Story story;
    private Poll currentPoll;

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

        activity = ((StoryActivity) getActivity());
        story = activity.getStory();

        int pageNum = getArguments().getInt(ARG_PAGE_NUMBER);
        int pageCnt = getArguments().getInt(ARG_PAGE_COUNT);

        View rootView;

        if (pageNum == pageCnt + 1) {

            // Poll page
            rootView = inflater.inflate(R.layout.story_poll, container, false);
            rootView.setVisibility(View.INVISIBLE);

            TextView pollDescription = (TextView) rootView.findViewById(R.id.poll_description);
            Button pollSubmit = (Button) rootView.findViewById(R.id.poll_submit);
            pollTitle = (TextView) rootView.findViewById(R.id.poll_title);
            pollInput = (EditText) rootView.findViewById(R.id.poll_input);
            timerText = (TextView) rootView.findViewById(R.id.timer);
            
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.votes_list);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(mLayoutManager);

            if (story != null) {
                rootView.setVisibility(View.VISIBLE);
                DatabaseReference pollRef = database.getReference("poll").child(story.getId());
                pollRef.child("pollCount").addValueEventListener(new PollCountListener(this));
                pollRef.child("polls").limitToLast(1).addValueEventListener(new PollListener(this));
            }

            FirebaseUser user = auth.getCurrentUser();

            if (user != null && story != null) {
                pollDescription.setText("Contribute to " + story.getTitle() + " by " + story.getAuthorAlias());

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

            if (activity.getPages() != null && activity.getPages().size() > 0)
                wordsView.setPage(activity.getPages().get(pageNum - 1));
            wordsView.setPageNumber(pageNum);

            pageNumberView.setTypeface(typeface);
            pageNumberView.setText("page " + pageNum + " of " + pageCnt);
        }

        return rootView;
    }

    public void gotPollCount(Long count) {
        pollTitle.setText("Round " + count);
        Toast.makeText(activity, "New voting round: " + count, Toast.LENGTH_SHORT).show();
    }

    public void gotPoll(Poll poll) {
        if (poll != null) {
            currentPoll = poll;
            gotPosts(poll.getPosts());
            gotTimer(poll.getTimeEnding());
        }
    }

    public void gotPosts(List<Post> posts) {
        Toast.makeText(activity, "Got posts!", Toast.LENGTH_SHORT).show();

        RecyclerView.Adapter mAdapter = new VoteAdapter(posts);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void gotTimer(Long timeout) {
        long time = System.currentTimeMillis();

        if (timeout == null) {
            timerText.setText("Timer not set");
            return;
        }

        if (time < timeout) {
            if (timer != null) timer.cancel();
            timer = makeTimer(timeout - time).start();
        } else {
            timerFinished();
        }
    }

    private CountDownTimer makeTimer(long millisInFuture) {
        return new CountDownTimer(millisInFuture, 1000) {
            public void onTick(long millisUntilFinished) {
                timerText.setText("Voting ends in " + millisUntilFinished / 1000 + "s");
            }
            public void onFinish() {
                timerFinished();
            }
        };
    }

    private void timerFinished() {
        timerText.setText("Voting finished!");
        database.getReference("poll")
                .child(currentPoll.getId())
                .child("finished")
                .setValue(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.poll_submit:

                FirebaseUser user = auth.getCurrentUser();

                if (user == null) {
                    Toast.makeText(getActivity(), "Not signed in!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (currentPoll != null) {
                    String message = pollInput.getText().toString();
                    Post newPost = new Post(story.getId(), user.getUid(), user.getDisplayName(), message);

                    DatabaseReference ref = database.getReference("poll")
                            .child(story.getId())
                            .child("polls")
                            .child(currentPoll.getId());

                    ref.child("posts").push().setValue(newPost);

                    ref.child("timeEnding")
                            .setValue(System.currentTimeMillis() + COUNTDOWN_LENGTH);
                }

                break;
        }
    }
}
