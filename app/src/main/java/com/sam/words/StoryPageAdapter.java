package com.sam.words;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to a page in a story
 */
class StoryPageAdapter extends FragmentStatePagerAdapter {

    private String mStory;

    StoryPageAdapter(FragmentManager fm, String story) {
        super(fm);
        mStory = story;
    }

    void setStory(String story) {
        mStory = story;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        // Instantiate the fragment for the given page
        return StoryPageFragment.newInstance(position + 1, mStory);
    }

    @Override
    public int getItemPosition(Object object) {
        // Causes adapter to reload all Fragments when notifyDataSetChanged is called
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "SECTION 1";
            case 1:
                return "SECTION 2";
            case 2:
                return "SECTION 3";
        }
        return null;
    }
}
