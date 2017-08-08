package com.sam.words;

import android.content.Intent;
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

        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);

        TextView likesView = (TextView) v.findViewById(R.id.story_likes);
        TextView dateView = (TextView) v.findViewById(R.id.story_date);
        final WordsView wordsView = (WordsView) v.findViewById(R.id.words_view);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), StoryActivity.class);
                intent.putExtra(EXTRA_STORY, wordsView.getText());
                parent.getContext().startActivity(intent);
            }
        });

        return new BrowseStoryHolder(v, likesView, dateView, wordsView);
    }

    @Override
    public void onBindViewHolder(BrowseStoryHolder holder, int position) {
        Story story = mDataset.get(position);
        holder.mLikesView.setText(String.valueOf(story.getLikes()));
        holder.mDateView.setText(TimeAgo.timeAgo(story.getDateUpdated()));

        holder.mWordsView.setTitle(story.getTitle());
        holder.mWordsView.setAuthor(story.getAuthor());
        holder.mWordsView.setText(story.getContent());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
