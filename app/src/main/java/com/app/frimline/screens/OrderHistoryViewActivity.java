package com.app.frimline.screens;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.adapters.ProductDetailsTabAdapter;
import com.app.frimline.databinding.ActivityOrderHistoryViewBinding;
import com.app.frimline.databinding.DialogDiscardImageBinding;
import com.app.frimline.fragments.aboutProducts.AdditionalInfoFragment;
import com.app.frimline.fragments.aboutProducts.DescriptionFragment;
import com.app.frimline.fragments.aboutProducts.HowToUseFragment;
import com.app.frimline.fragments.aboutProducts.IngredientsFragment;
import com.app.frimline.fragments.aboutProducts.QnAFragment;
import com.app.frimline.fragments.aboutProducts.ReviewsFragment;
import com.app.frimline.models.OrderModel;
import com.app.frimline.views.WrapContentHeightViewPager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;

public class OrderHistoryViewActivity extends BaseActivity {
    BottomSheetBehavior sheetBehavior;
    LinearLayout layoutBottomSheet;
    private ActivityOrderHistoryViewBinding binding;
    private OrderModel model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_order_history_view);
        makeStatusBarSemiTranspenret(binding.toolbarNavigation.toolbar);
        binding.toolbarNavigation.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.ON_BACK_PRESS(act);
            }
        });
        binding.toolbarNavigation.title.setText("Order Details");
        layoutBottomSheet = findViewById(R.id.bottom_sheet);

        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        TextView headingTitle = findViewById(R.id.headingTitle);
        RelativeLayout bottomMinState = findViewById(R.id.bottomMinState);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {

                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        headingTitle.setVisibility(View.VISIBLE);
                        bottomMinState.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        headingTitle.setVisibility(View.GONE);
                        bottomMinState.setVisibility(View.VISIBLE);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        headingTitle.setVisibility(View.VISIBLE);
                        bottomMinState.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        layoutBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBottomSheet();
            }
        });
        setupTabIcons();
        changeTheme();

        if (CONSTANT.API_MODE) {
            model = gson.fromJson(getIntent().getStringExtra("model"), OrderModel.class);
            loadData();
        }
    }

    public void loadData() {

    }

    public void changeTheme() {
        CardView deliveredCardview = findViewById(R.id.deliveredCardview);
        deliveredCardview.setCardBackgroundColor(Color.parseColor(new PREF(act).getThemeColor()));
        LinearLayout priceSection = findViewById(R.id.priceSection);
        priceSection.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        ImageView deliveryCheckIcon = findViewById(R.id.deliveryCheckIcon);
        deliveryCheckIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(prefManager.getThemeColor())));
        deliveryCheckIcon.setBackgroundTintList(ColorStateList.valueOf((Color.WHITE)));
        binding.toolbarNavigation.downloadPDf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialog();
            }
        });
    }

    DialogDiscardImageBinding discardImageBinding;

    public void confirmationDialog() {
        discardImageBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_discard_image, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(discardImageBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(discardImageBinding.getRoot());

        discardImageBinding.titleTxt.setText("Report Download");
        discardImageBinding.subTitle.setText("Order report downloaded.");

        discardImageBinding.noTxt.setVisibility(View.GONE);
        discardImageBinding.noTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });
        discardImageBinding.yesTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    public void toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }


    private DescriptionFragment descriptionFragment = new DescriptionFragment();
    private HowToUseFragment howToUseFragment = new HowToUseFragment();
    private IngredientsFragment ingredientsFragment = new IngredientsFragment();
    private AdditionalInfoFragment additionalInfoFragment = new AdditionalInfoFragment();
    private ReviewsFragment reviewsFragment = new ReviewsFragment();
    private QnAFragment qnAFragment = new QnAFragment();
    private int indicatorWidth;

    private void setupTabIcons() {
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        WrapContentHeightViewPager wrapContentHeightViewPager = findViewById(R.id.viewPager);
        ProductDetailsTabAdapter adapter = new ProductDetailsTabAdapter(getSupportFragmentManager());
        qnAFragment.setThemColor(true);
        adapter.addFragment(descriptionFragment, "Description");
        adapter.addFragment(howToUseFragment, "How To Use");
        adapter.addFragment(ingredientsFragment, "Ingredients");
        adapter.addFragment(additionalInfoFragment, "Additional Information");
        adapter.addFragment(reviewsFragment, "Reviews");
        adapter.addFragment(qnAFragment, "Q&A");
        wrapContentHeightViewPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        wrapContentHeightViewPager.setAdapter(adapter);
        wrapContentHeightViewPager.setOffscreenPageLimit(10);
        tabLayout.setupWithViewPager(wrapContentHeightViewPager);
        ScrollView scrollView = findViewById(R.id.scrollView);
        wrapContentHeightViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                scrollView.smoothScrollTo(tabLayout.getLeft(), tabLayout.getTop() - 40);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setSelectedTabIndicatorColor(Color.parseColor(prefManager.getThemeColor()));
        tabLayout.setTabTextColors((Color.parseColor(prefManager.getThemeColor())), (Color.WHITE));
    }


}