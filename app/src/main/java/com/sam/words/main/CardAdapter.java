package com.sam.words.main;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sam.words.R;
import com.sam.words.utils.SharedPreferencesHelper;
import com.sam.words.story.StoryActivity;
import com.sam.words.components.WordsView;
import com.sam.words.models.Story;
import com.sam.words.utils.TimeAgo;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapts a list of stories into a list of card views in a Browse fragment
 */

public class CardAdapter extends RecyclerView.Adapter<CardHolder> {

    public static final String EXTRA_STORY = "STORY";
    private List<Story> mDataset = new ArrayList<>();

    CardAdapter(List<Story> myDataset) {
        mDataset = myDataset;
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

        TextView likesView = (TextView) v.findViewById(R.id.story_likes);
        TextView dateView = (TextView) v.findViewById(R.id.story_date);
        TextView titleView = (TextView) v.findViewById(R.id.story_title);
        TextView authorView = (TextView) v.findViewById(R.id.story_author);
        final WordsView wordsView = (WordsView) v.findViewById(R.id.words_view);

        titleView.setTypeface(typefaceBold);
        authorView.setTypeface(typefaceItalic);
        titleView.setTextColor(parent.getResources().getColor(R.color.black));
        authorView.setTextColor(parent.getResources().getColor(R.color.black));
        titleView.setTextSize((float) SharedPreferencesHelper.getTextSize(parent.getContext()) / 2);

        return new CardHolder(v, likesView, dateView, titleView, authorView, wordsView);
    }

    @Override
    public void onBindViewHolder(final CardHolder holder, int position) {
        final Story story = mDataset.get(position);
        holder.mLikesView.setText(String.valueOf(story.getLikeCount()));
        holder.mDateView.setText(TimeAgo.timeAgo(story.getDateUpdated()));

        holder.mTitleView.setText(story.getTitle());
        holder.mAuthorView.setText(story.getAuthorAlias());
        //holder.mWordsView.setChapters(story.getChapters());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.mCardView.getContext(), StoryActivity.class);
                intent.putExtra(EXTRA_STORY, story.getTitle());
                holder.mCardView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
