package com.app.frimline.screens;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.app.cartcounter.CharOrder;
import com.app.cartcounter.strategy.Strategy;
import com.app.frimline.BaseActivity;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.adapters.ProductDetailsTabAdapter;
import com.app.frimline.adapters.ProductImageSliderAdpater;
import com.app.frimline.databinding.ActivityProductDetailBinding;
import com.app.frimline.fragments.aboutProducts.AdditionalInfoFragment;
import com.app.frimline.fragments.aboutProducts.DescriptionFragment;
import com.app.frimline.fragments.aboutProducts.HowToUseFragment;
import com.app.frimline.fragments.aboutProducts.IngredientsFragment;
import com.app.frimline.fragments.aboutProducts.QnAFragment;
import com.app.frimline.fragments.aboutProducts.ReviewsFragment;
import com.app.frimline.models.OutCategoryModel;
import com.app.frimline.views.WrapContentHeightViewPager;
import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductDetailActivity extends BaseActivity {


    private boolean redShoeVisible = true;
    private ActivityProductDetailBinding binding;
    private boolean isAddedToCart = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_product_detail);
        makeStatusBarSemiTranspenret(binding.toolbarNavigation.toolbar);
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
        binding.counter.setText("1");
        binding.incrementAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentCounter = Integer.parseInt(binding.counter.getText().toString());
                currentCounter++;
                binding.counter.setText(String.valueOf(currentCounter));

            }
        });

        binding.decrementAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentCounter = Integer.parseInt(binding.counter.getText().toString());
                if (currentCounter > 1) {
                    currentCounter--;
                    binding.counter.setText(String.valueOf(currentCounter));

                } else {
                    Toast.makeText(act, "At least one quantity required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.addCartContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isAddedToCart) {
                    binding.addTextTxt.setText("Add to cart");
                    isAddedToCart = false;
                    binding.addCartContainer.setBackgroundTintList(null);
                    binding.cartIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(prefManager.getCategoryColor())));
                    binding.addTextTxt.setTextColor(Color.parseColor(prefManager.getCategoryColor()));
                } else {
                    binding.addTextTxt.setText("Added to cart");
                    isAddedToCart = true;
                    binding.addCartContainer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(prefManager.getCategoryColor())));
                    binding.cartIcon.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                    binding.addTextTxt.setTextColor(Color.WHITE);
                }

            }
        });

        binding.counter.setAnimationDuration(150L);
        binding.counter.setTextFontFamily(Objects.requireNonNull(ResourcesCompat.getFont(act, R.font.proxinova_semi_bold)));
        binding.counter.setCharStrategy(Strategy.NormalAnimation());
        binding.counter.addCharOrder(CharOrder.Number);
        binding.counter.setAnimationInterpolator(new AccelerateDecelerateInterpolator());
        binding.counter.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

            }
        });

        setupTabIcons();
        changeTheme();
        setImageSlide();
    }

    public void setImageSlide() {
        List<OutCategoryModel> outCategoryModels = new ArrayList<>();
        outCategoryModels.add(new OutCategoryModel());
        outCategoryModels.add(new OutCategoryModel());
        outCategoryModels.add(new OutCategoryModel());
        outCategoryModels.add(new OutCategoryModel());
        ProductImageSliderAdpater sliderAdapter = new ProductImageSliderAdpater(outCategoryModels, act);
        binding.productImagesSlider.setAdapter(sliderAdapter);
        binding.dotsIndicator.setViewPager(binding.productImagesSlider);
        binding.dotsIndicator.setSelectedDotColor(Color.parseColor(prefManager.getCategoryColor()));
    }

    public void changeTheme() {
        binding.detailContainer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
        binding.price.setTextColor(Color.parseColor(new PREF(act).getCategoryColor()));
        binding.cartIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
        GradientDrawable drawable = (GradientDrawable) binding.addCartContainer.getBackground();
        drawable.setStroke(2, Color.parseColor(new PREF(act).getCategoryColor()));


        VectorChildFinder vector = new VectorChildFinder(act, R.drawable.ic_not_returnable, binding.nonReturnIcon);
        VectorDrawableCompat.VFullPath path1 = vector.findPathByName("colorGreen");
        path1.setFillColor(Color.parseColor(prefManager.getCategoryColor()));
        binding.nonReturnIcon.invalidate();


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
        WrapContentHeightViewPager wrapContentHeightViewPager = findViewById(R.id.viewPager);
        ProductDetailsTabAdapter adapter = new ProductDetailsTabAdapter(getSupportFragmentManager());
        adapter.addFragment(descriptionFragment, "Description");
        adapter.addFragment(howToUseFragment, "How To Use");
        adapter.addFragment(ingredientsFragment, "Ingredients");
        adapter.addFragment(additionalInfoFragment, "Additional Information");
        adapter.addFragment(reviewsFragment, "Reviews");
        adapter.addFragment(qnAFragment, "Q&A");
        wrapContentHeightViewPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        wrapContentHeightViewPager.setAdapter(adapter);
        wrapContentHeightViewPager.setOffscreenPageLimit(10);
        binding.tabLayout.setupWithViewPager(wrapContentHeightViewPager);
        binding.tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        binding.tabLayout.setTabTextColors((Color.WHITE), (Color.parseColor(prefManager.getCategoryColor())));
        wrapContentHeightViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                binding.scrollView.smoothScrollTo(binding.viewPager.getLeft(), binding.viewPager.getTop());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}