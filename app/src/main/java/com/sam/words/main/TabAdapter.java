package com.sam.words.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class TabAdapter extends FragmentPagerAdapter {

    TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return TabFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return TabEnum.TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        TabEnum section = TabEnum.getSection(position + 1);
        return section == null ? null : section.toString();
    }
}
