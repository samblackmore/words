package com.sam.words.story;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sam.words.R;
import com.sam.words.models.Post;
import com.sam.words.utils.TimeAgo;

import java.util.ArrayList;
import java.util.List;

class VoteAdapter extends RecyclerView.Adapter<VoteHolder> {

    private List<Post> mDataset = new ArrayList<>();

    VoteAdapter(List<Post> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public VoteHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vote_view, parent, false);

        TextView scoreView = (TextView) v.findViewById(R.id.score);
        ImageView upArrow = (ImageView) v.findViewById(R.id.up_arrow);
        TextView wordView = (TextView) v.findViewById(R.id.word);
        TextView submittedByView = (TextView) v.findViewById(R.id.word_submitted_by);
        TextView timeAgoView = (TextView) v.findViewById(R.id.time_ago);

        return new VoteHolder(v, scoreView, upArrow, wordView, submittedByView, timeAgoView);
    }

    @Override
    public void onBindViewHolder(final VoteHolder holder, int position) {
        final Post post = mDataset.get(position);
        if (post != null) {
            holder.scoreView.setText(String.valueOf(post.getVotes()));
            holder.postView.setText(post.getMessage());
            holder.submittedByView.setText(post.getAuthorName());
            holder.timeAgoView.setText(TimeAgo.timeAgo(post.getDateCreated()));
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
