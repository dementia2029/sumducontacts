package com.dementia.sumdu.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dementia.sumdu.R;

import java.util.ArrayList;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private static final int COUNT_TABS = 3;

    private Context context;
    ArrayList<PlaceholderFragment> fragments;

    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;

        fragments = new ArrayList<>();
        for(int i = 0; i < COUNT_TABS; i++) {
            PlaceholderFragment fragment = PlaceholderFragment.newInstance(i + 1);
            fragments.add(fragment);
        }
    }

    public void update() {
        fragments.get(0).update();
        fragments.get(1).update();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return COUNT_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.tab_1);
            case 1:
                return context.getString(R.string.tab_2);
            case 2:
                return context.getString(R.string.tab_3);
        }
        return null;
    }
}
