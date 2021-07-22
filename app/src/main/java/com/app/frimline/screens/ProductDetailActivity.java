package com.app.frimline.screens;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.adapters.LoginTabAdapter;
import com.app.frimline.adapters.ProductDetailsTabAdapter;
import com.app.frimline.databinding.ActivityProductDetailBinding;
import com.app.frimline.fragments.LoginVEmailFragment;
import com.app.frimline.fragments.LoginVMobileFragment;
import com.app.frimline.fragments.aboutProducts.AdditionalInfoFragment;
import com.app.frimline.fragments.aboutProducts.DescriptionFragment;
import com.app.frimline.fragments.aboutProducts.HowToUseFragment;
import com.app.frimline.fragments.aboutProducts.IngredientsFragment;
import com.app.frimline.fragments.aboutProducts.QnAFragment;
import com.app.frimline.fragments.aboutProducts.ReviewsFragment;
import com.google.android.material.tabs.TabLayout;

public class ProductDetailActivity extends BaseActivity {


    private boolean redShoeVisible = true;
    private ActivityProductDetailBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_product_detail);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorScreenBackground));
        }
        //setStatusBarTransparent();

        ViewTreeObserver viewTreeObserver = binding.scrollView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                //
            }
        });
        binding.toolbarNavigation.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.ON_BACK_PRESS_ANIM(act);
            }
        });
        binding.toolbarNavigation.title.setText("Mouthwash");

        //setupTabIcons();
        changeTheme();
    }

    public void changeTheme() {
        binding.detailContainer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
        binding.price.setTextColor(Color.parseColor(new PREF(act).getCategoryColor()));
        binding.cartIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
        GradientDrawable drawable = (GradientDrawable) binding.addCartContainer.getBackground();
        drawable.setStroke(2, Color.parseColor(new PREF(act).getCategoryColor()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HELPER.ON_BACK_PRESS_ANIM(act);
    }

    private DescriptionFragment descriptionFragment = new DescriptionFragment();
    private HowToUseFragment howToUseFragment = new HowToUseFragment();
    private IngredientsFragment ingredientsFragment = new IngredientsFragment();
    private AdditionalInfoFragment additionalInfoFragment = new AdditionalInfoFragment();
    private ReviewsFragment reviewsFragment = new ReviewsFragment();
    private QnAFragment qnAFragment = new QnAFragment();

    private int indicatorWidth;

    private void setupTabIcons() {
        ProductDetailsTabAdapter adapter = new ProductDetailsTabAdapter(getSupportFragmentManager());
        adapter.addFragment(descriptionFragment, "Description");
        adapter.addFragment(howToUseFragment, "How To Use");
        adapter.addFragment(ingredientsFragment, "Ingredients");
        adapter.addFragment(additionalInfoFragment, "Additional Information");
        adapter.addFragment(reviewsFragment, "Reviews");
        adapter.addFragment(qnAFragment, "Q&A");
//
//        binding.viewPager.setAdapter(adapter);
//        binding.viewPager.setOffscreenPageLimit(3);


//        binding.viewPager.setAdapter(adapter);
//    //    binding.tab.setupWithViewPager(binding.viewPager);
//        binding.viewPager.setOffscreenPageLimit(3);

//        binding.tab.post(new Runnable() {
//            @Override
//            public void run() {
//
//
//                indicatorWidth = binding.tab.getWidth() / binding.tab.getTabCount();
//
//                //Assign new width
//                FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) binding.indicator.getLayoutParams();
//                indicatorParams.width = indicatorWidth;
//                binding.indicator.setLayoutParams(indicatorParams);
//                //binding.helper.getLayoutParams().height=binding.indicator.getLayoutParams().height;
//            }
//        });
//
//        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//
//            @Override
//            public void onPageScrolled(int i, float positionOffset, int positionOffsetPx) {
//                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) binding.indicator.getLayoutParams();
//                float translationOffset = (positionOffset + i) * indicatorWidth;
//                params.leftMargin = (int) translationOffset;
//                binding.indicator.setLayoutParams(params);
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//                // Log.e("PAge", i + "d");
//            }
//        });


    }

}