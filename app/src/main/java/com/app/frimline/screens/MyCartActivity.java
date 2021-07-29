package com.app.frimline.screens;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.adapters.MyCartAdapter;
import com.app.frimline.databinding.ActivityMyCartBinding;
import com.app.frimline.models.OutCategoryModel;

import java.util.ArrayList;

public class MyCartActivity extends BaseActivity {
    private ActivityMyCartBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_my_cart);
        makeStatusBarSemiTranspenret(binding.toolbarNavigation.toolbar);

        binding.boottomFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(act, BillingAddressActivity.class);
            }
        });
        binding.toolbarNavigation.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.ON_BACK_PRESS_ANIM(act);
            }
        });

        binding.toolbarNavigation.title.setText("Cart");
        changeColor();
        setAdapter();
    }

    public void setAdapter() {
        ArrayList<OutCategoryModel> arrayList = new ArrayList<>();
        OutCategoryModel homeModel = new OutCategoryModel();
        homeModel.setName("0");
        arrayList.add(homeModel);
        homeModel = new OutCategoryModel();
        homeModel.setName("1");
        arrayList.add(homeModel);
        homeModel = new OutCategoryModel();
        homeModel.setName("2");
        arrayList.add(homeModel);

        homeModel = new OutCategoryModel();
        homeModel.setName("3");
        arrayList.add(homeModel);


        MyCartAdapter shopFilterAdapter = new MyCartAdapter(arrayList, act);
        shopFilterAdapter.setActionsListener(new MyCartAdapter.setActionsListener() {
            @Override
            public void onDeleteAction(int position, OutCategoryModel model) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (model.getName().equalsIgnoreCase(arrayList.get(i).getName())) {
                        arrayList.remove(i);
                        shopFilterAdapter.notifyItemRemoved(position);
                        shopFilterAdapter.notifyItemRangeChanged(position, arrayList.size());
                        if (arrayList.size() == 0) {
                            setNoDataFound();
                        }
                        break;
                    }
                }
            }
        });

        binding.cartRecycler.setLayoutManager(new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false));
        binding.cartRecycler.setNestedScrollingEnabled(false);

        binding.cartRecycler.setAdapter(shopFilterAdapter);

        binding.applyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.promoCodeContainer.setVisibility(View.GONE);
                binding.appliedCodeSuccess.setVisibility(View.VISIBLE);
            }
        });
        binding.removePromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.promoCodeContainer.setVisibility(View.VISIBLE);
                binding.appliedCodeSuccess.setVisibility(View.GONE);
            }
        });

    }


    public void setNoDataFound() {
        binding.boottomFooter.setVisibility(View.GONE);
        binding.scrollView.setVisibility(View.GONE);
        binding.NoDataFound.setVisibility(View.VISIBLE);

    }

    public void changeColor() {
        binding.applyCode.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.removePromo.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.bottomSlider.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.boottomText.setTextColor((Color.parseColor(new PREF(act).getThemeColor())));
        binding.scrollView.setBackgroundColor((Color.parseColor(new PREF(act).getThemeColor())));

    }
}