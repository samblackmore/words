package com.sam.words.dictionary;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sam.words.R;
import com.sam.words.models.Word;
import com.sam.words.utils.TimeAgo;

import java.util.ArrayList;
import java.util.List;

class DictionaryAdapter extends RecyclerView.Adapter<DictionaryHolder> {

    private List<Word> mDataset = new ArrayList<>();

    DictionaryAdapter(List<Word> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DictionaryHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.word_view, parent, false);

        TextView scoreView = (TextView) v.findViewById(R.id.score);
        ImageView upArrow = (ImageView) v.findViewById(R.id.up_arrow);
        ImageView downArrow = (ImageView) v.findViewById(R.id.down_arrow);
        TextView wordView = (TextView) v.findViewById(R.id.word);
        TextView submittedByView = (TextView) v.findViewById(R.id.word_submitted_by);
        TextView timeAgoView = (TextView) v.findViewById(R.id.time_ago);

        return new DictionaryHolder(v, scoreView, upArrow, downArrow, wordView, submittedByView, timeAgoView);
    }

    @Override
    public void onBindViewHolder(final DictionaryHolder holder, int position) {
        final Word word = mDataset.get(position);
        holder.scoreView.setText(String.valueOf(word.getVotes()));
        holder.wordView.setText(word.getWord());
        holder.submittedByView.setText(word.getSubmittedBy());
        holder.timeAgoView.setText(TimeAgo.timeAgo(word.getDateSubmitted()));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
