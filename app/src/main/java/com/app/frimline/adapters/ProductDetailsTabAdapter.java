package com.app.frimline.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsTabAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();
    private final int currentItemFragment = -1;
    public ProductDetailsTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public void setPrimaryItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        super.setPrimaryItem(container, position, object);
        if (position == currentItemFragment) {
//            EnhancedDynamicHeightViewPager viewPager  = (EnhancedDynamicHeightViewPager) container;
//            currentItemFragment = position;
//            viewPager.measureCurrentView(fragmentList.get(position).requireView());
        }
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }


}