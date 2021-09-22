package com.app.frimline.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.app.frimline.Common.FRIMLINE;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.ObserverActionID;
import com.app.frimline.R;
import com.app.frimline.databinding.FragmentCenterBinding;
import com.app.frimline.models.CategoryRootFragments.CategorySingleModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.gson.Gson;

public class CenterFragment extends BaseFragment {

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        com.app.frimline.databinding.FragmentCenterBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_center, parent, false);
        binding.goToStore.setOnClickListener(v -> FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.SLIDE_VIEW_LEFT));
        binding.moreInfo.setOnClickListener(v -> FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.SLIDE_VIEW_RIGHT));
        if (API_MODE) {
            HELPER.LOAD_HTML(binding.description, new Gson().fromJson(getActivity().getIntent().getStringExtra("model"), CategorySingleModel.class).getDescriptions());
            String[] wordList = new Gson().fromJson(getActivity().getIntent().getStringExtra("model"), CategorySingleModel.class).getCategoryName().split(" ");
            if (wordList.length > 1) {
                binding.textTop.setText(wordList[0]);
                binding.textBottom.setText(wordList[1]);
            } else {
                binding.textTop.setText(wordList[0]);
            }
            CategorySingleModel model = new Gson().fromJson(getActivity().getIntent().getStringExtra("model"), CategorySingleModel.class);

            if (model != null) {
                binding.layout1.setBackgroundColor(Color.parseColor(pref.getCategoryColor()));
                binding.layout2.setBackgroundColor(Color.parseColor(pref.getThemeColor()));
                binding.commonImage.setImageDrawable(null);
                Glide.with(act).load(model.getCategorySliderImage()).transition(DrawableTransitionOptions.withCrossFade()).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.commonImage);
                binding.text1.setTextColor(Color.parseColor(pref.getThemeColor()));
                binding.icon2.setImageTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));

                binding.text.setTextColor(Color.parseColor(pref.getCategoryColor()));
                binding.icon.setImageTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
            }

        }
        return binding.getRoot();
    }
}