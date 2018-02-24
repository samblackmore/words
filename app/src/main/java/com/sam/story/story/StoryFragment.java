package com.sam.story.story;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sam.story.R;
import com.sam.story.components.Page;
import com.sam.story.components.WordsView;
import com.sam.story.main.newstory.BadWordsCheck;
import com.sam.story.models.Post;
import com.sam.story.models.Story;
import com.sam.story.models.Poll;
import com.sam.story.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A page in a story. Contains a single WordsView representing that page.
 */

public class StoryFragment extends Fragment implements GoogleSignInFragment, View.OnClickListener{

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private StoryActivity activity;
    private RecyclerView mRecyclerView;
    private RecyclerView chapterList;
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
    private int chapterId;
    private boolean alreadyPosted = false;

    // Fragment arguments
    public static final String ARG_PAGE_NUMBER = "page_number";
    public static final String ARG_PAGE_COUNT = "page_count";
    private PostValidation formValidation;
    private ValueEventListener listenerPoll;
    private ValueEventListener listenerPosts;
    private DatabaseReference listenRefPoll;
    private DatabaseReference listenRefPosts;
    private RelativeLayout pollRoot;
    private LinearLayout theEndContainer;
    private TextView theEnd;
    private LinearLayout pollBanner;
    private Integer pageNum;
    private Integer pageCnt;
    private int secretOptionsCountdown = 1;

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
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible && activity != null && pageNum != null && pageCnt != null) {
            activity.showFab(pageNum != pageCnt + 1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        activity = ((StoryActivity) getActivity());
        story = activity.getStory();

        if (story != null)
            chapterId = story.getChapters().size() - 1;

        pageNum = getArguments().getInt(ARG_PAGE_NUMBER);
        pageCnt = getArguments().getInt(ARG_PAGE_COUNT);

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/CrimsonText/CrimsonText-Regular.ttf");

        Typeface typefaceItalic = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/CrimsonText/CrimsonText-Italic.ttf");

        Typeface typefaceBold= Typeface.createFromAsset(getContext().getAssets(),
                "fonts/CrimsonText/CrimsonText-Bold.ttf");

        View rootView;

        if (pageNum == 0) {

            // Title page
            rootView = inflater.inflate(R.layout.story_title, container, false);
            TextView titleView = (TextView) rootView.findViewById(R.id.story_title);
            TextView authorView = (TextView) rootView.findViewById(R.id.story_author);
            TextView byView = (TextView) rootView.findViewById(R.id.story_by);
            titleView.setOnClickListener(this);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            chapterList = (RecyclerView) rootView.findViewById(R.id.chapter_list);
            chapterList.setHasFixedSize(true);
            chapterList.setLayoutManager(mLayoutManager);

            if (story != null) {

                ChapterAdapter chapterAdapter = new ChapterAdapter(activity, story.getChapters());

                List<Page> pages = activity.getPages();
                List<Integer> pageNumbers = new ArrayList<>();

                if (pages != null && pages.size() > 0) {
                    for (int num = 0; num < pages.size(); num++) {
                        Page page = pages.get(num);
                        if (page.getChapterSubtitle() != null)
                            pageNumbers.add(num);
                    }
                }

                if (pageNumbers.size() > 0)
                    chapterAdapter.setPageNumbers(pageNumbers);

                chapterList.setAdapter(chapterAdapter);


                titleView.setText(story.getTitle().toUpperCase());
                authorView.setText(story.getAuthorAlias());
                titleView.setTypeface(typefaceBold);
                authorView.setTypeface(typefaceItalic);
                byView.setTypeface(typefaceItalic);
                authorView.setTextColor(getResources().getColor(R.color.gray));
                byView.setTextColor(getResources().getColor(R.color.grayDk));
                titleView.setTextSize((float) SharedPreferencesHelper.getTextSize(getContext()) / 2);
                authorView.setTextSize((float) SharedPreferencesHelper.getTextSize(getContext()) / 3);
                byView.setTextSize((float) SharedPreferencesHelper.getTextSize(getContext()) / 4);
            }

        } else if (pageNum == pageCnt + 1) {

            // Poll page
            rootView = inflater.inflate(R.layout.story_poll, container, false);
            rootView.setVisibility(View.INVISIBLE);

            submitContainer = (LinearLayout) rootView.findViewById(R.id.submit_container);
            signInButton = (SignInButton) rootView.findViewById(R.id.sign_in_button);
            signInProgress = (ProgressBar) rootView.findViewById(R.id.sign_in_progress);
            pollRoot = (RelativeLayout) rootView.findViewById(R.id.poll_root);
            pollBanner = (LinearLayout) rootView.findViewById(R.id.poll_banner);
            pollTitle = (TextView) rootView.findViewById(R.id.poll_title);
            pollRound = (TextView) rootView.findViewById(R.id.poll_round);
            pollInput = (EditText) rootView.findViewById(R.id.poll_input);
            pollSubmit = (Button) rootView.findViewById(R.id.poll_submit);
            timerText = (TextView) rootView.findViewById(R.id.poll_timer);
            theEndContainer = (LinearLayout) rootView.findViewById(R.id.the_end_container);
            theEnd = (TextView) rootView.findViewById(R.id.the_end);

            pollInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        pollInput.setInputType(pollInput.getInputType() | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                        pollInput.append("\n");
                        return true;
                    }
                    return false;
                }
            });

            theEnd.setTypeface(typefaceItalic);
            theEnd.setTextColor(getResources().getColor(R.color.black));
            theEnd.setTextSize((float) SharedPreferencesHelper.getTextSize(getContext()) / 2);

            formValidation = new PostValidation(pollInput, pollSubmit);

            signInButton.setOnClickListener(activity);
            pollSubmit.setOnClickListener(this);
            pollInput.addTextChangedListener(formValidation);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.votes_list);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(mLayoutManager);

            if (story != null) {
                rootView.setVisibility(View.VISIBLE);
                listenRefPoll = database.getReference("poll").child(story.getId());
                listenerPoll = listenRefPoll.addValueEventListener(new PollListener(this));
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

            pageNumberView.setText("page " + pageNum + " of " + pageCnt);
        }

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (listenRefPoll != null)
            listenRefPoll.removeEventListener(listenerPoll);
        if (listenRefPosts != null)
            listenRefPosts.removeEventListener(listenerPosts);
    }

    public void gotPoll(Poll poll) {
        if (story != null && poll != null) {

            int maxRounds = story.getChapterSize();
            int chapter = story.getChapters().size();

            if (poll.getRound() == maxRounds) {
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
            gotTimer(poll.getEndTime());

            listenRefPosts = database.getReference("posts").child(story.getId()).child(String.valueOf(poll.getRound()));
            listenerPosts = listenRefPosts.addValueEventListener(new PostsListener(this));

            onSignInSignOut(auth.getCurrentUser());
        }
    }

    public void gotPosts(List<Post> posts) {
        alreadyPosted = false;
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            for (Post post : posts) {
                if (post.getUserId().equals(user.getUid())) {
                    alreadyPosted = true;
                    onSignInSignOut(user);
                }
            }
        }

        if (story != null && currentPoll != null) {
            RecyclerView.Adapter mAdapter = new VoteAdapter(posts);
            mRecyclerView.setAdapter(mAdapter);
        }
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
    }

    @Override
    public void onClick(View v) {

        FirebaseUser user = auth.getCurrentUser();

        switch (v.getId()) {

            case R.id.story_title:

                if (story != null && user != null && user.getUid().equals(story.getUserId())) {
                    if (secretOptionsCountdown == 5) {
                        secretOptionsCountdown = 1;
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        database.getReference("stories")
                                                .child(story.getId())
                                                .child("finished")
                                                .setValue(true);
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        dialog.cancel();
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Are you sure you you want to end this story now? This cannot be undone!")
                                .setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener)
                                .show();
                    } else {
                        secretOptionsCountdown += 1;
                    }

                }

                break;

            case R.id.poll_submit:

                // Hide keyboard
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                if (user == null) {
                    Toast.makeText(getActivity(), "Not signed in!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (story == null) {
                    Toast.makeText(getActivity(), "Story not loaded!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (currentPoll != null) {
                    String input = pollInput.getText().toString();
                    String message = input.replaceAll("↩\n", "\n\t");
                    Post newPost = new Post(story.getId(), user.getUid(), user.getDisplayName(), message);

                    NewPostCallback newPostCallback = new NewPostCallback(activity, newPost, story.getId(), String.valueOf(currentPoll.getRound()));
                    BadWordsCheck badWordsCheck = new BadWordsCheck(input, newPostCallback);
                    badWordsCheck.execute();

                    onSignInSignOut(user);
                }

                pollInput.setText("");

                break;
        }
    }

    @Override
    public void onSignInSignOut(FirebaseUser user) {
        showSignInLoading(false);
        submitContainer.setVisibility(user == null ? View.GONE : View.VISIBLE);
        signInButton.setVisibility(user == null ? View.VISIBLE : View.GONE);

        if (storyFinished())
            showStoryEnd();

        if (user != null) {
            if (wonLastRound(user))
                showBanner(R.string.you_won_last_round, R.drawable.ic_cake);
            else if (alreadyPosted)
                showBanner(R.string.submitted, R.drawable.ic_check_box);
        }
    }

    private boolean storyFinished() {
        return story != null && story.isFinished();
    }

    private boolean wonLastRound(FirebaseUser user) {
        Post latestPost = activity.getLatestPost();
        return latestPost != null && latestPost.getUserId().equals(user.getUid());
    }

    private void showStoryEnd() {
        pollRoot.setVisibility(View.GONE);
        theEndContainer.setVisibility(View.VISIBLE);
        activity.showFab(false);
    }

    private void showBanner(int stringId, int drawableId) {
        submitContainer.setVisibility(View.GONE);
        pollTitle.setVisibility(View.VISIBLE);
        pollTitle.setText(stringId);
        pollTitle.setCompoundDrawablesWithIntrinsicBounds(0, drawableId, 0, 0);
        pollBanner.setBackgroundColor(getResources().getColor(R.color.gray));
    }

    @Override
    public void showSignInLoading(boolean show) {
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
