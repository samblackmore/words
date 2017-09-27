package com.sam.words.story;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sam.words.R;
import com.sam.words.models.Chapter;
import com.sam.words.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

class ChapterAdapter extends RecyclerView.Adapter<ChapterHolder> {

    private List<Chapter> mDataset = new ArrayList<>();

    ChapterAdapter(List<Chapter> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public ChapterHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        Typeface typeface = Typeface.createFromAsset(parent.getContext().getAssets(),
                "fonts/CrimsonText/CrimsonText-Regular.ttf");

        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chapter_view, parent, false);

        TextView wordsView = (TextView) v.findViewById(R.id.words_view);
        wordsView.setTypeface(typeface);

        return new ChapterHolder(v, wordsView);
    }

    @Override
    public void onBindViewHolder(final ChapterHolder holder, int position) {
        final Chapter chapter = mDataset.get(position);
        holder.wordsView.setText(String.valueOf(position) + ".    " + TextUtil.capitalize(chapter.getTitle()));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
