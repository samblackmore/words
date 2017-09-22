package com.sam.words.main;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
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

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    public static final String EXTRA_STORY = "STORY";
    private List<Story> stories = new ArrayList<>();
    private List<Post> posts = new ArrayList<>();

    public void gotStories(List<Story> stories) {
        this.stories = stories;
        notifyDataSetChanged();
    }

    public void gotPosts(List<Post> posts) {
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
        final Story story = stories.get(position);
        holder.mLikesView.setText(String.valueOf(story.getLikeCount()));
        holder.mDateView.setText(TimeAgo.timeAgo(story.getDateUpdated()));

        holder.mTitleView.setText(TextUtil.capitalize(story.getTitle()));
        holder.mAuthorView.setText(story.getAuthorAlias());

        String storyId = stories.get(position).getId();
        database.getReference("posts").child(storyId).child("0").child("posts").addListenerForSingleValueEvent(new CardPostListener(holder));

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.mCardView.getContext(), StoryActivity.class);
                intent.putExtra(EXTRA_STORY, story.getId());
                holder.mCardView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }
}
