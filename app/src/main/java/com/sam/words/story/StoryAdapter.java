package com.sam.words.story;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.sam.words.components.Page;

import java.util.List;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to a page in a story
 */
class StoryAdapter extends FragmentStatePagerAdapter {

    private List<Page> pages;

    StoryAdapter(FragmentManager fm, List<Page> pages) {
        super(fm);
        this.pages = pages;
    }

    @Override
    public Fragment getItem(int position) {
        return StoryFragment.newInstance(position, pages.size());
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return pages.size() + 2;
    }

    public void update(List<Page> pages) {
        this.pages = pages;
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        try{
            super.finishUpdate(container);
        } catch (NullPointerException nullPointerException){
            System.out.println("Catch the NullPointerException");
        }
    }
}
