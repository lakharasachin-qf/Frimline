package com.app.frimline.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.frimline.fragments.CenterFragment;
import com.app.frimline.fragments.LeftFragment;
import com.app.frimline.fragments.RightFragment;


public class CategoryNavViewPager extends FragmentPagerAdapter {
    Context context;
    int totalTabs;

    public CategoryNavViewPager(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new LeftFragment();
            case 1:
                return new CenterFragment();
            case 2:
                return new RightFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }


}