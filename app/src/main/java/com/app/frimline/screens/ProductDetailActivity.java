package com.app.frimline.screens;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.app.frimline.BaseActivity;
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

        setStatusBarTransparent();

        ViewTreeObserver viewTreeObserver = binding.scrollView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                //
            }
        });
        setupTabIcons();
    }


    private DescriptionFragment descriptionFragment=new DescriptionFragment();
    private HowToUseFragment howToUseFragment=new HowToUseFragment();
    private IngredientsFragment ingredientsFragment=new IngredientsFragment();
    private AdditionalInfoFragment additionalInfoFragment=new AdditionalInfoFragment();
    private ReviewsFragment reviewsFragment=new ReviewsFragment();
    private QnAFragment qnAFragment=new QnAFragment();

    private int indicatorWidth;
    private void setupTabIcons() {
        ProductDetailsTabAdapter adapter = new ProductDetailsTabAdapter(getSupportFragmentManager());
        adapter.addFragment(descriptionFragment, "Description");
        adapter.addFragment(howToUseFragment, "How To Use");
        adapter.addFragment(ingredientsFragment, "Ingredients");
        adapter.addFragment(additionalInfoFragment, "Additional Information");
        adapter.addFragment(reviewsFragment, "Reviews");
        adapter.addFragment(qnAFragment, "Q&A");










        binding.viewPager.setAdapter(adapter);
        binding.tab.setupWithViewPager(binding.viewPager);
        binding.viewPager.setOffscreenPageLimit(3);

        //Determine indicator width at runtime
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
//                // binding.helper.getLayoutParams().height=binding.indicator.getLayoutParams().height;
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