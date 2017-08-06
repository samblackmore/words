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
 * Created by samhb on 2017-07-18.
 */

class StoryListAdapter extends RecyclerView.Adapter<StoryListAdapter.ViewHolder> {
    private static List<Story> mDataset = new ArrayList<>();

    public static final String EXTRA_STORY = "STORY";

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView mCardView;
        TextView mTextView;
        WordsView mWordsView;

        ViewHolder(CardView cardView, TextView textView, WordsView wordsView) {
            super(cardView);
            mCardView = cardView;
            mTextView = textView;
            mWordsView = wordsView;
        }
    }

    StoryListAdapter(List<Story> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public StoryListAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);

        TextView textView = (TextView) v.findViewById(R.id.info_text);
        final WordsView wordsView = (WordsView) v.findViewById(R.id.words_view);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), StoryActivity.class);
                intent.putExtra(EXTRA_STORY, wordsView.getText());
                parent.getContext().startActivity(intent);
            }
        });

        return new ViewHolder(v, textView, wordsView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).getAuthor());
        holder.mWordsView.setText(mDataset.get(position).getTitle());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
