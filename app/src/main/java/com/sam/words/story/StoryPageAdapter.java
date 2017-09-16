package com.sam.words.story;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to a page in a story
 */
class StoryPageAdapter extends FragmentStatePagerAdapter {

    private int pages = 1;

    StoryPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // Instantiate the fragment for the given page
        return StoryPageFragment.newInstance(position + 1, pages);
    }

    @Override
    public int getItemPosition(Object object) {
        // Causes adapter to reload all Fragments when notifyDataSetChanged is called
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
        notifyDataSetChanged();
    }
}
