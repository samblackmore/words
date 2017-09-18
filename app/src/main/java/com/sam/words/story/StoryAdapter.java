package com.sam.words.story;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to a page in a story
 */
class StoryAdapter extends FragmentStatePagerAdapter {

    private int pageCount = 1;

    StoryAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return StoryFragment.newInstance(position + 1, pageCount);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        try{
            super.finishUpdate(container);
        } catch (NullPointerException nullPointerException){
            //
        }
    }

    void setPageCount(int pageCount) {
        this.pageCount = pageCount;
        notifyDataSetChanged();
    }
}
