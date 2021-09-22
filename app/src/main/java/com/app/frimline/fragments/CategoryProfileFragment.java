package com.app.frimline.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.databinding.FragmentCategoryProfileLayoutBinding;
import com.app.frimline.models.CategoryRootFragments.CategorySingleModel;
import com.bumptech.glide.Glide;
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
        binding.text3.setTextColor((Color.parseColor(pref.getCategoryColor())));
        binding.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        binding.titleTxt.setTextColor((Color.parseColor(pref.getCategoryColor())));
        binding.viewBottom.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        GradientDrawable drawable = (GradientDrawable) binding.sectuib.getBackground();
        drawable.setStroke(2, Color.parseColor(pref.getCategoryColor()));

    }

    public void hideShowView() {
        binding.icon2.setOnClickListener(v -> onNavClick.onNavigationDrawerClick());
        binding.icon.setOnClickListener(v -> onNavClick.onNavigationDrawerClick());
        binding.button.setOnClickListener(v -> {
            //changeStatusBarColor(ContextCompat.getColor(getActivity(),R.color.colorScreenBackground));
            onNavClick.GoToStore();
        });

        binding.mainAppbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
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
        });
    }

    public void setProfile() {

        CategorySingleModel model = new Gson().fromJson(getActivity().getIntent().getStringExtra("model"), CategorySingleModel.class);
        String[] wordList = model.getCategoryName().split(" ");
        if (wordList.length > 1) {
            binding.text1.setText(wordList[0]);
            if (wordList[1].length() > 7) {
                binding.text2.setText(wordList[1]);
                binding.text3.setText(wordList[1]);
                binding.text2.setVisibility(View.GONE);
                binding.text3.setVisibility(View.VISIBLE);
            }
            binding.text2.setText(wordList[1]);

        } else {
            binding.text1.setText(wordList[0]);
        }

        binding.titleTxt.setText(model.getCategoryName());

        binding.shortDescription.setText("");

        binding.descTXt.setText("");
        HELPER.LOAD_HTML(binding.shortDescription, model.getDescriptions());
        HELPER.LOAD_HTML(binding.descTXt, model.getLongDescription());

        Glide.with(act).load(model.getDetailImage()).placeholder(R.drawable.ic_square_place_holder).into(binding.mainBackdrop);
        Glide.with(act).load(model.getImage())
                .circleCrop()
                .into(binding.image2);

    }
}