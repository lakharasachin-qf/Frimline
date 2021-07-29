package com.app.frimline.screens;

import android.animation.Animator;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.adapters.SearchAdapter;
import com.app.frimline.databinding.ActivitySearchBinding;
import com.app.frimline.models.HomeModel;
import com.app.frimline.models.LAYOUT_TYPE;
import com.app.frimline.models.OutCategoryModel;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity {
    private ActivitySearchBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_search);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (savedInstanceState == null) {
            binding.rootBackground.setVisibility(View.INVISIBLE);

            final ViewTreeObserver viewTreeObserver = binding.rootBackground.getViewTreeObserver();

            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        circularRevealActivity();
                        binding.rootBackground.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                });
            }

        }

        changeStatusBarColor(ContextCompat.getColor(act, R.color.colorScreenBackground));
        binding.nameEdtLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.nameEdt.requestFocus();

            }
        });
        binding.clearAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ArrayList<OutCategoryModel> innerDataList = new ArrayList<>();
        OutCategoryModel outCategoryModel = new OutCategoryModel();
        outCategoryModel.setName("");
        innerDataList.add(outCategoryModel);
        outCategoryModel = new OutCategoryModel();
        outCategoryModel.setName("");
        innerDataList.add(outCategoryModel);
        outCategoryModel = new OutCategoryModel();
        outCategoryModel.setName("");
        innerDataList.add(outCategoryModel);


        ArrayList<HomeModel> arrayList = new ArrayList<>();
        HomeModel homeModel = new HomeModel();

        homeModel.setProductList(innerDataList);
        homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_TOP_PRODUCT);
        arrayList.add(homeModel);
        homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_FILTER_CHIP);
        homeModel.setProductList(innerDataList);
        arrayList.add(homeModel);
        homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_HOT_PRODUCT);
        homeModel.setProductList(innerDataList);
        arrayList.add(homeModel);


        SearchAdapter shopFilterAdapter = new SearchAdapter(arrayList, act);
        binding.containerRecycler.setLayoutManager(new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false));
        binding.containerRecycler.setNestedScrollingEnabled(false);

        binding.containerRecycler.setAdapter(shopFilterAdapter);
        changeTheme();
    }

    public void changeTheme() {

        binding.clearAction.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.searchIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.sortAction.setChipIconTint(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.sortAction.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
    }

    private void circularRevealActivity() {
        int cx = binding.rootBackground.getRight() - getDips(44);
        int cy = binding.rootBackground.getTop() - getDips(44);

        float finalRadius = Math.max(binding.rootBackground.getWidth(), binding.rootBackground.getHeight());

        Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                binding.rootBackground,
                cx,
                cy,
                0,
                finalRadius);

        circularReveal.setDuration(500);
        binding.rootBackground.setVisibility(View.VISIBLE);
        circularReveal.start();

    }

    private int getDips(int dps) {
        Resources resources = getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dps,
                resources.getDisplayMetrics());
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int cx = binding.rootBackground.getWidth() - getDips(44);
            int cy = binding.rootBackground.getTop() - getDips(44);

            float finalRadius = Math.max(binding.rootBackground.getWidth(), binding.rootBackground.getHeight());
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(binding.rootBackground, cx, cy, finalRadius, 0);

            circularReveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    binding.rootBackground.setVisibility(View.INVISIBLE);
                    finish();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            circularReveal.setDuration(300);
            circularReveal.start();
        } else {
            super.onBackPressed();
        }
    }
}