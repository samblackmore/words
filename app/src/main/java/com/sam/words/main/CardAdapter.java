package com.sam.words.main;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.sam.words.R;
import com.sam.words.models.Notifications;
import com.sam.words.models.Post;
import com.sam.words.utils.SharedPreferencesHelper;
import com.sam.words.story.StoryActivity;
import com.sam.words.components.WordsView;
import com.sam.words.models.Story;
import com.sam.words.utils.TextUtil;
import com.sam.words.utils.TimeAgo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Adapts a list of stories into a list of card views in a Browse fragment
 */

public class CardAdapter extends RecyclerView.Adapter<CardHolder> {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    public static final String EXTRA_STORY = "STORY";
    private List<Story> stories = new ArrayList<>();
    private List<Post> posts = new ArrayList<>();
    private HashMap<String, Notifications> activity = new HashMap<>();
    private int pink;
    private int pinkLt;
    private Typeface typefaceBold;
    private boolean activityList = false;

    void refresh() {
        notifyDataSetChanged();
    }

    private void updateStories(Story newStory) {

        for (int i = 0; i < stories.size(); i++) {
            Story story = stories.get(i);
            if (story.getId().equals(newStory.getId())) {
                stories.set(i, newStory);
                return;
            }
        }
        stories.add(newStory);
    }

    void gotStory(Story story) {
        updateStories(story);
        notifyDataSetChanged();
    }

    void gotStories(List<Story> stories) {
        this.stories = stories;
        notifyDataSetChanged();
    }

    void gotPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    void gotActivity(HashMap<String, Notifications> activity) {
        this.activity = activity;
        notifyDataSetChanged();
    }

    void setActivityList(boolean activityList) {
        this.activityList = activityList;
    }

    @Override
    public CardHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        typefaceBold = Typeface.createFromAsset(
                parent.getContext().getAssets(),
                "fonts/CrimsonText/CrimsonText-Bold.ttf"
        );

        Typeface typefaceItalic = Typeface.createFromAsset(
                parent.getContext().getAssets(),
                "fonts/CrimsonText/CrimsonText-Italic.ttf"
        );

        View v = LayoutInflater.from(parent.getContext())
                .inflate(activityList ? R.layout.activity_item : R.layout.card_view, parent, false);

        v.setAlpha(0);
        v.animate().alpha(1.0f);

        TextView headerView = (TextView) v.findViewById(R.id.header);
        TextView newPostsView = (TextView) v.findViewById(R.id.new_posts_count);
        TextView newChaptersView = (TextView) v.findViewById(R.id.new_chapters_count);
        TextView newContributorsView = (TextView) v.findViewById(R.id.new_contributors_count);
        TextView likesView = (TextView) v.findViewById(R.id.story_likes);
        TextView dateView = (TextView) v.findViewById(R.id.story_date);
        TextView titleView = (TextView) v.findViewById(R.id.story_title);
        TextView authorView = (TextView) v.findViewById(R.id.story_author);

        pink = parent.getResources().getColor(R.color.pink);
        pinkLt = parent.getResources().getColor(R.color.pinkLt);

        if (!activityList) {

            titleView.setTypeface(typefaceBold);
            authorView.setTypeface(typefaceItalic);
            titleView.setTextColor(parent.getResources().getColor(R.color.black));
            authorView.setTextColor(parent.getResources().getColor(R.color.black));
            titleView.setTextSize((float) SharedPreferencesHelper.getTextSize(parent.getContext()) / 2);

            WordsView wordsView = (WordsView) v.findViewById(R.id.words_view);
            wordsView.setVisibility(View.VISIBLE);
            wordsView.setAlpha(0);

            return new CardHolder(v, headerView, newPostsView, newChaptersView, newContributorsView, likesView, dateView, titleView, authorView, wordsView);
        } else {

            titleView.setTypeface(Typeface.DEFAULT_BOLD);
            titleView.setTextSize(16);

            return new CardHolder(v, headerView, newPostsView, newChaptersView, newContributorsView, likesView, dateView, titleView, authorView, null);
        }
    }

    private void showNotification(TextView view, int storyCount, int userCount) {
        int diff = storyCount - userCount;
        view.setText(String.valueOf(diff));
        view.setVisibility(diff == 0 ? View.GONE : View.VISIBLE);
        if (diff != 0)
            ((View) view.getParent()).setVisibility(View.VISIBLE);
    }

    @Override
    public void onBindViewHolder(final CardHolder holder, int position) {

        ArrayList<Story> updatedStories = new ArrayList<>();

        for (Story s : stories) {

            Notifications a = activity.get(s.getId());

            if (a != null) {
                if (a.getPostCount() != s.getPostCount() ||
                        a.getChapterCount() != s.getChapterCount() ||
                        a.getContributorsCount() != s.getContributorsCount()) {
                    updatedStories.add(s);
                }
            }
        }

        ArrayList<Story> oldStories = new ArrayList<>(stories);
        oldStories.removeAll(updatedStories);

        FirebaseUser user = auth.getCurrentUser();
        final Story story = position < updatedStories.size() ? updatedStories.get(position) : oldStories.get(position - updatedStories.size());
        final String storyId = story.getId();

        View.OnClickListener openStory = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.mCardView.getContext(), StoryActivity.class);
                intent.putExtra(EXTRA_STORY, storyId);
                holder.mCardView.getContext().startActivity(intent);
            }
        };

        if (activityList) {
            holder.mHeaderView.setVisibility(View.GONE);

            if (updatedStories.size() > 0 && oldStories.size() > 0) {

                if (position == 0) {
                    holder.mHeaderView.setVisibility(View.VISIBLE);
                    holder.mHeaderView.setText(updatedStories.size() + " updates");
                }
                if (position == updatedStories.size()) {
                    holder.mHeaderView.setVisibility(View.VISIBLE);
                    holder.mHeaderView.setText(oldStories.size() + " active");
                }
            }
        }

        holder.mLikesView.setText(String.valueOf(story.getLikeCount()));
        holder.mDateView.setText(TimeAgo.timeAgo(story.getDateUpdated()));
        holder.mTitleView.setText(TextUtil.capitalize(story.getTitle()));
        holder.mAuthorView.setText(story.getAuthorAlias());
        holder.mTitleView.setOnClickListener(openStory);

        if (user != null) {
            if (story.getUserId().equals(user.getUid())) {
                holder.mAuthorView.setTextColor(pink);
                holder.mAuthorView.setTypeface(activityList ? Typeface.DEFAULT_BOLD : typefaceBold);
                holder.mAuthorView.setBackgroundColor(pinkLt);
            }

            int heart = story.getLikes().containsKey(user.getUid()) ? R.drawable.ic_heart_lit : R.drawable.ic_heart;

            if (activityList) {

                Notifications a = activity.get(story.getId());

                if (a != null) {
                    showNotification(holder.mNewPostsView, story.getPostCount(), a.getPostCount());
                    showNotification(holder.mNewChaptersView, story.getChapterCount(), a.getChapterCount());
                    showNotification(holder.mNewContributorsView, story.getContributorsCount(), a.getContributorsCount());
                }
                holder.mLikesView.setCompoundDrawablesWithIntrinsicBounds(0, heart, 0, 0);
            }
            else
                holder.mLikesView.setCompoundDrawablesWithIntrinsicBounds(heart, 0, 0, 0);
        }

        if (!activityList) {
            holder.mWordsView.setOnClickListener(openStory);
            database.getReference("posts").child(storyId).child("0").addListenerForSingleValueEvent(new CardPostListener(holder));
        }

        holder.mLikesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = database.getReference("stories").child(storyId);
                ref.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Story s = mutableData.getValue(Story.class);
                        FirebaseUser user = auth.getCurrentUser();

                        if (s == null || user == null)
                            return Transaction.success(mutableData);

                        if (s.getLikes() != null && s.getLikes().containsKey(user.getUid()))
                            s.removeLike(user.getUid());
                        else
                            s.addLike(user.getUid());

                        mutableData.setValue(s);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                        if (databaseError != null)
                            Log.d("Debug", databaseError.toString());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }
}
