package com.app.frimline.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.frimline.Common.PREF;
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
        showColor();
        return binding.getRoot();
    }

    public void showColor(){
        PREF pref=new PREF(getActivity());
        binding.underLineRight.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        binding.underLineRightDesc.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        binding.text1.setTextColor((Color.parseColor(pref.getCategoryColor())));
        binding.text2.setTextColor((Color.parseColor(pref.getCategoryColor())));
        binding.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        binding.titleTxt.setTextColor((Color.parseColor(pref.getCategoryColor())));
        binding.viewBottom.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        GradientDrawable drawable = (GradientDrawable)binding.sectuib.getBackground();
        drawable.setStroke(2, Color.parseColor(pref.getCategoryColor()));

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
                //changeStatusBarColor(ContextCompat.getColor(getActivity(),R.color.colorScreenBackground));
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

    public void changeStatusBarColor(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }
    }
}