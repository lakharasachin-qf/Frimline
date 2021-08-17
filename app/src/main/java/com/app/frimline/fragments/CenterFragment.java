package com.app.frimline.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.app.frimline.Common.FRIMLINE;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.ObserverActionID;
import com.app.frimline.R;
import com.app.frimline.databinding.FragmentCenterBinding;
import com.app.frimline.models.CategoryRootFragments.CategorySingleModel;
import com.google.gson.Gson;

public class CenterFragment extends BaseFragment {

    private FragmentCenterBinding binding;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_center, parent, false);
        binding.goToStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.SLIDE_VIEW_LEFT);
            }
        });
        binding.moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.SLIDE_VIEW_RIGHT);
            }
        });
        if (API_MODE) {
            HELPER.LOAD_HTML(binding.description, new Gson().fromJson(getActivity().getIntent().getStringExtra("model"), CategorySingleModel.class).getDescriptions());

        }
        return binding.getRoot();
    }
}