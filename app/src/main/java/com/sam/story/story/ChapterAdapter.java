package com.sam.story.story;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sam.story.R;
import com.sam.story.models.Chapter;
import com.sam.story.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

class ChapterAdapter extends RecyclerView.Adapter<ChapterHolder> {

    private StoryActivity mStoryActivity;
    private List<Chapter> mDataset = new ArrayList<>();
    private List<Integer> mPageNumbers = new ArrayList<>();
    private Typeface typeface;
    private Typeface typefaceItalic;

    ChapterAdapter(StoryActivity storyActivity, List<Chapter> myDataset) {
        mDataset = myDataset;
        mStoryActivity = storyActivity;
    }

    @Override
    public ChapterHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        typeface = Typeface.createFromAsset(parent.getContext().getAssets(),
                "fonts/CrimsonText/CrimsonText-Regular.ttf");

        typefaceItalic = Typeface.createFromAsset(parent.getContext().getAssets(),
                "fonts/CrimsonText/CrimsonText-Italic.ttf");

        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chapter_view, parent, false);

        TextView chapterTitleView = (TextView) v.findViewById(R.id.chapter_title);
        TextView pageNumView = (TextView) v.findViewById(R.id.page_number);
        chapterTitleView.setTypeface(typeface);

        return new ChapterHolder(v, chapterTitleView, pageNumView);
    }

    @Override
    public void onBindViewHolder(final ChapterHolder holder, final int position) {
        String title = mDataset.get(position).getTitle();

        if (title == null) {
            title = "Untitled (in progress)";
            holder.chapterTitleView.setTypeface(typefaceItalic);
        } else {
            title = TextUtil.capitalize(title);
        }

        holder.chapterTitleView.setText(String.valueOf(position) + ".    " + title);

        if (mPageNumbers.size() > position) {
            final int pageNum = mPageNumbers.get(position) + 1;
            holder.pageNumView.setText(String.valueOf(pageNum));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mStoryActivity.goToPage(pageNum);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    void setPageNumbers(List<Integer> pageNumbers) {
        mPageNumbers = pageNumbers;
    }
}
