package com.sam.story.story;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.sam.story.R;
import com.sam.story.models.Post;
import com.sam.story.utils.TimeAgo;

import java.util.ArrayList;
import java.util.List;

class VoteAdapter extends RecyclerView.Adapter<VoteHolder> {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private List<Post> mDataset = new ArrayList<>();
    private int pink;
    private int white;

    VoteAdapter(List<Post> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public VoteHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vote_view, parent, false);

        ConstraintLayout clickableView = (ConstraintLayout) v.findViewById(R.id.vote_view);
        TextView scoreView = (TextView) v.findViewById(R.id.score);
        ImageView upArrow = (ImageView) v.findViewById(R.id.up_arrow);
        TextView wordView = (TextView) v.findViewById(R.id.word);
        TextView submittedByView = (TextView) v.findViewById(R.id.word_submitted_by);
        TextView timeAgoView = (TextView) v.findViewById(R.id.time_ago);

        pink = parent.getResources().getColor(R.color.pink);
        white = parent.getResources().getColor(R.color.white);

        return new VoteHolder(v, clickableView, scoreView, upArrow, wordView, submittedByView, timeAgoView);
    }

    @Override
    public void onBindViewHolder(final VoteHolder holder, int position) {
        final Post post = mDataset.get(position);
        if (post != null) {

            FirebaseUser user = auth.getCurrentUser();
            if (user != null && post.getVotes().contains(user.getUid())) {
                holder.scoreView.setBackgroundColor(pink);
                holder.scoreView.setTextColor(white);
            }

            holder.scoreView.setText(String.valueOf(post.getVoteCount()));
            holder.postView.setText(post.getMessage());
            holder.submittedByView.setText(post.getAuthorName());
            holder.timeAgoView.setText(TimeAgo.timeAgo(post.getDateCreated()) + " ago");

            holder.clickableView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference ref = database.getReferenceFromUrl(post.getPath());
                    ref.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            Post p = mutableData.getValue(Post.class);
                            FirebaseUser user = auth.getCurrentUser();

                            if (p == null || user == null)
                                return Transaction.success(mutableData);

                            if (p.getVotes() != null && p.getVotes().contains(user.getUid()))
                                p.removeVote(user.getUid());
                            else
                                p.addVote(user.getUid());

                            mutableData.setValue(p);
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
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
