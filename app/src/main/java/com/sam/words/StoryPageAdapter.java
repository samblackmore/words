package com.sam.words;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to a page in a story
 */
class StoryPageAdapter extends FragmentPagerAdapter {

    String mStory;

    StoryPageAdapter(FragmentManager fm, String story) {
        super(fm);
        mStory = story;
    }

    @Override
    public Fragment getItem(int position) {
        // Instantiate the fragment for the given page
        return StoryPageFragment.newInstance(position + 1, mStory);
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
