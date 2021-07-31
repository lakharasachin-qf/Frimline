package com.app.frimline.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.databinding.FragmentCategoryProfileLayoutBinding;
import com.app.frimline.models.CategoryRootFragments.CategorySingleModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;


public class CategoryProfileFragment extends BaseFragment {
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
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_profile_layout, parent, false);
        hideShowView();
        showColor();
        if (API_MODE) {
            setProfile();
        }
        return binding.getRoot();
    }

    public void showColor() {
        PREF pref = new PREF(getActivity());
        binding.underLineRight.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        binding.underLineRightDesc.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        binding.text1.setTextColor((Color.parseColor(pref.getCategoryColor())));
        binding.text2.setTextColor((Color.parseColor(pref.getCategoryColor())));
        binding.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        binding.titleTxt.setTextColor((Color.parseColor(pref.getCategoryColor())));
        binding.viewBottom.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        GradientDrawable drawable = (GradientDrawable) binding.sectuib.getBackground();
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

    public void setProfile() {

        CategorySingleModel model = new Gson().fromJson(getActivity().getIntent().getStringExtra("model"), CategorySingleModel.class);
        HELPER.LOAD_HTML(binding.shortDescription, model.getDescriptions());
        HELPER.LOAD_HTML(binding.descTXt, model.getLongDescription());
        Log.e("FRAGMENT",new Gson().toJson(model));

    }
}