package com.app.frimline.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.app.frimline.Common.HELPER;
import com.app.frimline.R;
import com.app.frimline.databinding.FragmentCategoryRootBinding;
import com.app.frimline.screens.CategoryLandingActivity;


public class CategoryRootFragment extends BaseFragment {
    private FragmentCategoryRootBinding binding;
    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_category_root,parent,false);

        HELPER.changeThemeCategoryRootFragment(binding, pref.getCategoryColor());
        binding.cat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.setConfiguration("#EF7F1A","#12C0DD");
                Intent intent = new Intent(getActivity(), CategoryLandingActivity.class);
                startActivity(intent);
            }
        });
        binding.cat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.setConfiguration("#EF7F1A","#81B533");
                Intent intent = new Intent(getActivity(), CategoryLandingActivity.class);
                startActivity(intent);
            }
        });
        binding.cat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.setConfiguration("#EF7F1A","#E8AE21");
                Intent intent = new Intent(getActivity(), CategoryLandingActivity.class);
                startActivity(intent);
            }
        });
        return  binding.getRoot();
    }





}