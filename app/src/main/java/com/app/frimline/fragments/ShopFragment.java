package com.app.frimline.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.adapters.ShopAdapter;
import com.app.frimline.adapters.ShopFilterAdapter;
import com.app.frimline.databinding.FragmentShopBinding;
import com.app.frimline.models.HomeModel;
import com.app.frimline.models.LAYOUT_TYPE;
import com.app.frimline.models.OutCategoryModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class ShopFragment extends Fragment {
    private FragmentShopBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop, container, false);

        ArrayList<HomeModel> arrayList = new ArrayList<>();
        HomeModel homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_TOP_PRODUCT);
        arrayList.add(homeModel);
        homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_FILTER_CHIP);
        arrayList.add(homeModel);
        homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_HOT_PRODUCT);
        arrayList.add(homeModel);


        ShopAdapter shopFilterAdapter = new ShopAdapter(arrayList, getActivity());
        binding.containerRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.containerRecycler.setNestedScrollingEnabled(false);

        binding.containerRecycler.setAdapter(shopFilterAdapter);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changeTheme();
    }

    public void changeTheme() {

        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(getActivity()).getCategoryColor())));
        binding.filterChip.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor(new PREF(getActivity()).getCategoryColor())));
        binding.sortFilterAction.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor(new PREF(getActivity()).getCategoryColor())));
        binding.filterChip.setChipIconTint(ColorStateList.valueOf(Color.parseColor(new PREF(getActivity()).getCategoryColor())));
        binding.sortFilterAction.setChipIconTint(ColorStateList.valueOf(Color.parseColor(new PREF(getActivity()).getCategoryColor())));
    }
}