package com.sam.words;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapts a list of stories into a list of card views in a Browse fragment
 */

class BrowseListAdapter extends RecyclerView.Adapter<BrowseStoryHolder> {
    private static List<Story> mDataset = new ArrayList<>();
    BrowseTab section;

    static final String EXTRA_STORY = "STORY";

    BrowseListAdapter(BrowseTab section, List<Story> myDataset) {
        mDataset = myDataset;
        this.section = section;
    }

    @Override
    public BrowseStoryHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

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
        titleView.setTextSize((float) SharedPreferencesHelper.getTextSize(parent.getContext()) / 2);
        authorView.setTypeface(typefaceItalic);

        return new BrowseStoryHolder(v, likesView, dateView, titleView, authorView, wordsView);
    }

    @Override
    public void onBindViewHolder(final BrowseStoryHolder holder, int position) {
        final Story story = mDataset.get(position);
        holder.mLikesView.setText(String.valueOf(story.getLikes()));
        holder.mDateView.setText(TimeAgo.timeAgo(story.getDateUpdated()));

        holder.mTitleView.setText(story.getTitle());
        holder.mAuthorView.setText(story.getAuthorName());
        holder.mWordsView.setText(story.getChapters());
        holder.mWordsView.setPageNumber(0);

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.mCardView.getContext(), StoryActivity.class);
                intent.putExtra(EXTRA_STORY, story.getStoryId());
                holder.mCardView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
