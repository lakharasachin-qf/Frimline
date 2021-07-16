package com.app.frimline.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.frimline.R;
import com.app.frimline.databinding.FragmentCategoryProfileLayoutBinding;
import com.google.android.material.appbar.AppBarLayout;


public class CategoryProfileFragment extends Fragment {
    private FragmentCategoryProfileLayoutBinding binding;

    public interface OnNavClick {
        void onNavigationDrawerClick();

        void GoToStore();
    }

    private OnNavClick onNavClick;

    public void setOnNavClick(OnNavClick onNavClick) {
        this.onNavClick = onNavClick;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_profile_layout, container, false);
        hideShowView();
        return binding.getRoot();
    }


    public void hideShowView() {
        binding.icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavClick.onNavigationDrawerClick();
            }
        });
        binding.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavClick.onNavigationDrawerClick();
            }
        });
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavClick.GoToStore();
            }
        });

        binding.mainAppbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    // If collapsed, then do this

                    binding.titleTxt.setVisibility(View.VISIBLE);
                    binding.icon2.setVisibility(View.VISIBLE);
                    binding.icon.setVisibility(View.GONE);

                } else if (verticalOffset == 0) {
                    // If expanded, then do this
                    binding.icon2.setVisibility(View.GONE);
                    binding.titleTxt.setVisibility(View.GONE);

                    binding.icon.setVisibility(View.VISIBLE);

                } else {
                    binding.icon2.setVisibility(View.GONE);
                    binding.titleTxt.setVisibility(View.GONE);
                    binding.icon.setVisibility(View.VISIBLE);

                }
            }
        });
    }


}