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

    static final String EXTRA_STORY = "STORY";

    BrowseListAdapter(List<Story> myDataset) {
        mDataset = myDataset;
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

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), StoryActivity.class);
                intent.putExtra(EXTRA_STORY, "Yep, long story! Back in August we had this surprise office meeting where we all had to go into the canteen and listen to a call from one of the McAfee VPs. He said he was flying out to Montreal from California to be with us the next day to answer any questions we had and the reason is because McAfee decided they're shutting down the whole Montreal office so everyone would be out of a job. Was definitely a shock because we'd just been doing the \"Q3 kick-off\" where we hear the roadmap for the rest of the year. Apparently no-one in Montreal knew about it because we'd even been doing interviews that week and were looking to hire 6 new people. Someone had even turned down a job offer from somewhere else the day before because McAfee offered to pay more money. So we've all been looking for new jobs ever since and luckily we had until October 2nd to figure it out. So a couple of weeks ago I had a few interviews and last week I accepted an offer from my number 1 choice which was Unity, have you heard of it?");
                parent.getContext().startActivity(intent);
            }
        });

        return new BrowseStoryHolder(v, likesView, dateView, titleView, authorView, wordsView);
    }

    @Override
    public void onBindViewHolder(BrowseStoryHolder holder, int position) {
        Story story = mDataset.get(position);
        holder.mLikesView.setText(String.valueOf(story.getLikes()));
        holder.mDateView.setText(TimeAgo.timeAgo(story.getDateUpdated()));

        Chapter chapter = new Chapter("Chapter", story.getContent());
        List<Chapter> chapters = new ArrayList<>();
        chapters.add(chapter);

        holder.mTitleView.setText(story.getTitle());
        holder.mAuthorView.setText(story.getAuthor());
        holder.mWordsView.setText(chapters);
        holder.mWordsView.setPageNumber(0);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
