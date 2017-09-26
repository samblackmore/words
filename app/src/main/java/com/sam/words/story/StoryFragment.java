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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sam.words.R;
import com.sam.words.components.WordsView;
import com.sam.words.models.Post;
import com.sam.words.models.Story;
import com.sam.words.models.Poll;
import com.sam.words.utils.SharedPreferencesHelper;

import java.util.List;
import java.util.Locale;

/**
 * A page in a story. Contains a single WordsView representing that page.
 */

public class StoryFragment extends Fragment implements GoogleSignInFragment, View.OnClickListener{

    // FIXME - get real chapter id
    private int chapterId = 0;

    //private final int COUNTDOWN_LENGTH = 5 * 60 * 1000;
    //private final int COUNTDOWN_LENGTH = 24 * 60 * 60 * 1000;
    private final int COUNTDOWN_LENGTH = 30 * 1000;

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private StoryActivity activity;
    private RecyclerView mRecyclerView;
    private SignInButton signInButton;
    private ProgressBar signInProgress;
    private LinearLayout submitContainer;
    private TextView pollTitle;
    private TextView pollRound;
    private EditText pollInput;
    private TextView timerText;
    private Button pollSubmit;
    private CountDownTimer timer;
    private Story story;
    private Poll currentPoll;

    // Fragment arguments
    public static final String ARG_PAGE_NUMBER = "page_number";
    public static final String ARG_PAGE_COUNT = "page_count";
    private PostValidation formValidation;

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

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/CrimsonText/CrimsonText-Regular.ttf");

        Typeface typefaceItalic = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/CrimsonText/CrimsonText-Italic.ttf");

        View rootView;

        if (pageNum == 0) {

            // Title page
            rootView = inflater.inflate(R.layout.story_title, container, false);
            TextView titleView = (TextView) rootView.findViewById(R.id.story_title);
            TextView authorView = (TextView) rootView.findViewById(R.id.story_author);
            TextView byView = (TextView) rootView.findViewById(R.id.story_by);
            if (story != null) {
                titleView.setText(story.getTitle().toUpperCase());
                authorView.setText(story.getAuthorAlias());
                titleView.setTypeface(typeface);
                authorView.setTypeface(typefaceItalic);
                byView.setTypeface(typefaceItalic);
                titleView.setTextColor(getResources().getColor(R.color.grayDk));
                authorView.setTextColor(getResources().getColor(R.color.gray));
                byView.setTextColor(getResources().getColor(R.color.grayDk));
                titleView.setTextSize((float) SharedPreferencesHelper.getTextSize(getContext()) / 2);
                authorView.setTextSize((float) SharedPreferencesHelper.getTextSize(getContext()) / 2);
                byView.setTextSize((float) SharedPreferencesHelper.getTextSize(getContext()) / 4);

            }
        } else if (pageNum == pageCnt + 1) {

            // Poll page
            rootView = inflater.inflate(R.layout.story_poll, container, false);
            rootView.setVisibility(View.INVISIBLE);

            submitContainer = (LinearLayout) rootView.findViewById(R.id.submit_container);
            signInButton = (SignInButton) rootView.findViewById(R.id.sign_in_button);
            signInProgress = (ProgressBar) rootView.findViewById(R.id.sign_in_progress);
            pollTitle = (TextView) rootView.findViewById(R.id.poll_title);
            pollRound = (TextView) rootView.findViewById(R.id.poll_round);
            pollInput = (EditText) rootView.findViewById(R.id.poll_input);
            pollSubmit = (Button) rootView.findViewById(R.id.poll_submit);
            timerText = (TextView) rootView.findViewById(R.id.poll_timer);

            formValidation = new PostValidation(pollSubmit);

            signInButton.setOnClickListener(activity);
            pollSubmit.setOnClickListener(this);
            pollInput.addTextChangedListener(formValidation);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.votes_list);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(mLayoutManager);

            if (story != null) {
                rootView.setVisibility(View.VISIBLE);
                database.getReference("poll").child(story.getId()).child(String.valueOf(chapterId)).limitToLast(1)
                        .addValueEventListener(new PollListener(this));
            }

            FirebaseUser user = auth.getCurrentUser();

            if (user != null) {
                submitContainer.setVisibility(View.VISIBLE);
                signInButton.setVisibility(View.GONE);
            }

        } else {

            // Story page
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

    public void gotPoll(Poll poll) {
        if (story != null && poll != null) {

            int maxRounds = story.getChapterSize();
            int chapter = story.getChapters().size();

            if (currentPoll != null && currentPoll.getRound() != poll.getRound())
                Toast.makeText(activity, "New voting round: " + poll.getRound(), Toast.LENGTH_SHORT).show();

            if (poll.getRound() > maxRounds) {
                pollRound.setText("Chapter " + chapter + " title");
                pollInput.setHint(R.string.your_chapter_title);
                formValidation.isTitle(true);
            }
            else {
                pollRound.setText("Round " + poll.getRound() + "/" + maxRounds);
                pollInput.setHint(R.string.your_3_words);
                formValidation.isTitle(false);
            }

            currentPoll = poll;
            gotPosts(poll.getPosts());
            gotTimer(poll.getTimeEnding());

            updateUI(auth.getCurrentUser());
        }
    }

    public void gotPosts(List<Post> posts) {
        Toast.makeText(activity, "Refreshed posts", Toast.LENGTH_SHORT).show();

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
                long duration = millisUntilFinished / 1000;
                long hours = duration / 3600;
                long minutes = (duration % 3600) / 60;
                long seconds = (duration % 60);

                StringBuilder builder = new StringBuilder();

                if (hours > 0) {
                    builder.append(String.format(Locale.US, "%02dh", hours));
                    builder.append(" ");
                }

                if (minutes > 0) {
                    builder.append(String.format(Locale.US, "%02dm", minutes));
                    builder.append(" ");
                }

                if (seconds > 0) {
                    builder.append(String.format(Locale.US, "%02ds", seconds));
                    builder.append(" ");
                }

                timerText.setText("Voting ends in " + builder.toString());
            }
            public void onFinish() {
                timerFinished();
            }
        };
    }

    private void timerFinished() {
        timerText.setText("Voting finished!");

        // To know timer finished, we first need to have retrieved end time from poll
        // We can only get poll after getting story therefore story cannot be null

        database.getReference("poll")
                .child(story.getId())
                .child(String.valueOf(chapterId))
                .child(String.valueOf(currentPoll.getRound()))
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

                if (story == null) {
                    Toast.makeText(getActivity(), "Story not loaded!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (currentPoll != null) {
                    String message = pollInput.getText().toString();
                    Post newPost = new Post(story.getId(), user.getUid(), user.getDisplayName(), message);

                    DatabaseReference pollRef = database.getReference("poll")
                            .child(story.getId())
                            .child(String.valueOf(chapterId))
                            .child(String.valueOf(currentPoll.getRound()));

                    DatabaseReference newPostRef = pollRef.child("posts").push();
                    newPost.setPath(newPostRef.toString());
                    newPostRef.setValue(newPost);

                    pollRef.child("timeEnding")
                            .setValue(System.currentTimeMillis() + COUNTDOWN_LENGTH);

                    submitContainer.setVisibility(View.GONE);
                    pollTitle.setVisibility(View.VISIBLE);
                    pollTitle.setText(R.string.submitted);
                    pollInput.setText("");
                }

                break;
        }
    }

    @Override
    public void updateUI(FirebaseUser user) {
        showLoading(false);
        submitContainer.setVisibility(user == null ? View.GONE : View.VISIBLE);
        signInButton.setVisibility(user == null ? View.VISIBLE : View.GONE);

        if (user != null && currentPoll != null) {
            for (Post post : currentPoll.getPosts()) {
                if (post.getUserId().equals(user.getUid())) {
                    submitContainer.setVisibility(View.GONE);
                    pollTitle.setVisibility(View.VISIBLE);
                    pollTitle.setText(R.string.submitted);
                }
            }
        }
    }

    @Override
    public void showLoading(boolean show) {
        if (show) {
            signInProgress.setVisibility(View.VISIBLE);
            signInButton.setVisibility(View.GONE);
            submitContainer.setVisibility(View.GONE);
        } else {
            signInProgress.setVisibility(View.GONE);
            if (auth.getCurrentUser() == null) {
                signInButton.setVisibility(View.VISIBLE);
                submitContainer.setVisibility(View.GONE);
            } else {
                signInButton.setVisibility(View.GONE);
                submitContainer.setVisibility(View.VISIBLE);
            }
        }
    }
}
