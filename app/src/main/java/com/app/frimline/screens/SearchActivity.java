package com.app.frimline.screens;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.adapters.SearchAdapter;
import com.app.frimline.adapters.ShopAdapter;
import com.app.frimline.databinding.ActivitySearchBinding;
import com.app.frimline.models.HomeModel;
import com.app.frimline.models.LAYOUT_TYPE;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity {
    private ActivitySearchBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_search);
        changeStatusBarColor(ContextCompat.getColor(act, R.color.colorScreenBackground));
        binding.nameEdtLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.nameEdt.requestFocus();

            }
        });


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


        SearchAdapter shopFilterAdapter = new SearchAdapter(arrayList, act);
        binding.containerRecycler.setLayoutManager(new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false));
        binding.containerRecycler.setNestedScrollingEnabled(false);

        binding.containerRecycler.setAdapter(shopFilterAdapter);
        changeTheme();
    }

    public void changeTheme() {

        binding.clearAction.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
        binding.searchIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
        binding.sortAction.setChipIconTint(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
        binding.sortAction.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
    }
}