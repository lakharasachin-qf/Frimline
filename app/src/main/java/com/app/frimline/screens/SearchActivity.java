package com.app.frimline.screens;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.adapters.SearchAdapter;
import com.app.frimline.adapters.SortingAdapter;
import com.app.frimline.databinding.ActivitySearchBinding;
import com.app.frimline.models.HomeModel;
import com.app.frimline.models.LAYOUT_TYPE;
import com.app.frimline.models.ListModel;
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
        if (CONSTANT.API_MODE) {

        } else {
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
            fillSortingData();
            binding.sortAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    slideToLeftFilter(binding.filterFrameLayout);
                }
            });
        }
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

    public void slideTorightFilter(View view) {
        Animation RightSwipe = AnimationUtils.loadAnimation(act, R.anim.slide_right_out);
        view.startAnimation(RightSwipe);
        binding.transparentOverlay.setVisibility(View.GONE);
        binding.filterFrameLayout.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        binding.container.setEnabled(true);
    }

    public void slideToLeftFilter(View view) {
        Animation RightSwipe = AnimationUtils.loadAnimation(act, R.anim.slide_from_right);
        view.startAnimation(RightSwipe);
        view.setVisibility(View.VISIBLE);
        binding.transparentOverlay.setVisibility(View.VISIBLE);
        binding.filterFrameLayout.setVisibility(View.VISIBLE);
        binding.container.setEnabled(false);
    }

    @Override
    public void onBackPressed() {
        if (binding.transparentOverlay.getVisibility() == View.VISIBLE) {
            if (binding.filterFrameLayout.getVisibility() == View.VISIBLE) {
                slideTorightFilter(binding.filterFrameLayout);
            }
            return;
        }
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
    }

    ArrayList<ListModel> listModels;
    private SortingAdapter adpt;

    public void fillSortingData() {

        listModels = new ArrayList<>();

        ListModel countryModel = new ListModel();
        countryModel.setName("Sort by popularity");
        listModels.add(countryModel);
        countryModel = new ListModel();
        countryModel.setName("Sort by average rating");
        listModels.add(countryModel);
        countryModel = new ListModel();
        countryModel.setName("Sort by latest");
        listModels.add(countryModel);
        countryModel = new ListModel();
        countryModel.setName("Sort by price : Low to High");
        listModels.add(countryModel);
        countryModel = new ListModel();
        countryModel.setName("Sort by price : High to Low");
        listModels.add(countryModel);

        binding.titleText.setText("Sort By");

        if (listModels != null) {
            adpt = new SortingAdapter(listModels, act, 1);
            SortingAdapter.setOnCheckedRadioListener radioListener = new SortingAdapter.setOnCheckedRadioListener() {
                @Override
                public void onOptionSelect(ListModel listModel, int position) {

                }
            };
            adpt.setRadioListener(radioListener);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act);
            binding.recyclerList.setLayoutManager(mLayoutManager);
            binding.recyclerList.setItemAnimator(new DefaultItemAnimator());
            binding.recyclerList.setAdapter(adpt);


        }

        binding.closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideTorightFilter(binding.filterFrameLayout);
            }
        });


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int fragmentWidth = (width / 2);


        binding.filterFrameLayout.getLayoutParams().width = fragmentWidth;

        binding.filterFrameLayout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }

}