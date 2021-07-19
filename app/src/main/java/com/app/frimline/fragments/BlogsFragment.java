package com.app.frimline.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.frimline.Common.HELPER;
import com.app.frimline.R;
import com.app.frimline.adapters.BlogsAdapter;
import com.app.frimline.adapters.ParentHomeAdapter;
import com.app.frimline.adapters.TopRattedProductAdapter;
import com.app.frimline.databinding.FragmentBlogsBinding;
import com.app.frimline.models.HomeModel;
import com.app.frimline.models.LAYOUT_TYPE;
import com.app.frimline.screens.BlogDetailsActivity;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class BlogsFragment extends Fragment {

    private FragmentBlogsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_blogs, container, false);
        ArrayList<HomeModel> arrayList = new ArrayList<>();
        HomeModel homeModel =new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_LEFT_BLOG);
        arrayList.add(homeModel);
        homeModel =new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_RIGHT_BLOG);
        arrayList.add(homeModel);
        homeModel =new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_LEFT_BLOG);
        arrayList.add(homeModel);
        homeModel =new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_RIGHT_BLOG);
        arrayList.add(homeModel);
        homeModel =new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_LEFT_BLOG);
        arrayList.add(homeModel);
        homeModel =new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_RIGHT_BLOG);
        arrayList.add(homeModel);
        homeModel =new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_LEFT_BLOG);
        arrayList.add(homeModel);

        BlogsAdapter adaptertop = new BlogsAdapter(arrayList, getActivity());
        binding.blogsRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.blogsRecycler.setAdapter(adaptertop);
        return binding.getRoot();
    }
}