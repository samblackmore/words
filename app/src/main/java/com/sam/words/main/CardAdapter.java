package com.sam.words.main;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.sam.words.models.Post;
import com.sam.words.utils.SharedPreferencesHelper;
import com.sam.words.story.StoryActivity;
import com.sam.words.components.WordsView;
import com.sam.words.models.Story;
import com.sam.words.utils.TextUtil;
import com.sam.words.utils.TimeAgo;

import java.util.ArrayList;
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

    void refresh() {
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

    @Override
    public CardHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        Typeface typefaceBold = Typeface.createFromAsset(
                parent.getContext().getAssets(),
                "fonts/CrimsonText/CrimsonText-Bold.ttf"
        );

        Typeface typefaceItalic = Typeface.createFromAsset(
                parent.getContext().getAssets(),
                "fonts/CrimsonText/CrimsonText-Italic.ttf"
        );

        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);

        v.setAlpha(0);
        v.animate().alpha(1.0f);

        TextView likesView = (TextView) v.findViewById(R.id.story_likes);
        TextView dateView = (TextView) v.findViewById(R.id.story_date);
        TextView titleView = (TextView) v.findViewById(R.id.story_title);
        TextView authorView = (TextView) v.findViewById(R.id.story_author);
        final WordsView wordsView = (WordsView) v.findViewById(R.id.words_view);
        wordsView.setAlpha(0);

        titleView.setTypeface(typefaceBold);
        authorView.setTypeface(typefaceItalic);
        titleView.setTextColor(parent.getResources().getColor(R.color.black));
        authorView.setTextColor(parent.getResources().getColor(R.color.black));
        titleView.setTextSize((float) SharedPreferencesHelper.getTextSize(parent.getContext()) / 2);

        return new CardHolder(v, likesView, dateView, titleView, authorView, wordsView);
    }

    @Override
    public void onBindViewHolder(final CardHolder holder, int position) {
        FirebaseUser user = auth.getCurrentUser();
        final Story story = stories.get(position);
        holder.mLikesView.setText(String.valueOf(story.getLikeCount()));
        holder.mDateView.setText(TimeAgo.timeAgo(story.getDateUpdated()));

        holder.mTitleView.setText(TextUtil.capitalize(story.getTitle()));
        holder.mAuthorView.setText(story.getAuthorAlias());

        final String storyId = story.getId();
        database.getReference("posts").child(storyId).child("0").addListenerForSingleValueEvent(new CardPostListener(holder));

        View.OnClickListener openStory = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.mCardView.getContext(), StoryActivity.class);
                intent.putExtra(EXTRA_STORY, storyId);
                holder.mCardView.getContext().startActivity(intent);
            }
        };

        holder.mTitleView.setOnClickListener(openStory);
        holder.mWordsView.setOnClickListener(openStory);

        if (user != null && story.getLikes().containsKey(user.getUid()))
            holder.mLikesView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_lit, 0, 0, 0);
        else
            holder.mLikesView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart, 0, 0, 0);

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
