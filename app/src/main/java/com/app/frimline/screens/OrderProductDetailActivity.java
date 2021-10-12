package com.app.frimline.screens;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.graphics.ColorUtils;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.app.frimline.BaseActivity;
import com.app.frimline.Common.APIs;
import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.MySingleton;
import com.app.frimline.Common.ResponseHandler;
import com.app.frimline.R;
import com.app.frimline.adapters.ProductDetailsTabAdapter;
import com.app.frimline.adapters.ProductImageSliderAdpater;
import com.app.frimline.databaseHelper.CartRoomDatabase;
import com.app.frimline.databinding.ActivityOrderProductDetailBinding;
import com.app.frimline.fragments.aboutProducts.AdditionalInfoFragment;
import com.app.frimline.fragments.aboutProducts.DescriptionFragment;
import com.app.frimline.fragments.aboutProducts.HowToUseFragment;
import com.app.frimline.fragments.aboutProducts.IngredientsFragment;
import com.app.frimline.fragments.aboutProducts.QnAFragment;
import com.app.frimline.fragments.aboutProducts.ReviewsFragment;
import com.app.frimline.models.HomeFragements.ProductModel;
import com.app.frimline.models.OrderedProductModel;
import com.app.frimline.views.WrapContentHeightViewPager;
import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderProductDetailActivity extends BaseActivity {


    private final boolean redShoeVisible = true;
    private ActivityOrderProductDetailBinding binding;
    private final boolean isAddedToCart = false;
    private int observableId;
    private boolean applyThemeColor = false;
    private String defaultColor = "#EF7F1A";
    private final boolean isSameProductEdit = false;
    private String productId;
    private final DescriptionFragment descriptionFragment = new DescriptionFragment();
    private final HowToUseFragment howToUseFragment = new HowToUseFragment();
    private final IngredientsFragment ingredientsFragment = new IngredientsFragment();
    private final AdditionalInfoFragment additionalInfoFragment = new AdditionalInfoFragment();
    private final ReviewsFragment reviewsFragment = new ReviewsFragment();
    private final QnAFragment qnAFragment = new QnAFragment();
    private int indicatorWidth;
    private ProductModel productModel;
    private boolean isLoading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_order_product_detail);
        defaultColor = "#EF7F1A";

        CartRoomDatabase cartRoomDatabase = CartRoomDatabase.getAppDatabase(act);

        if (getIntent().hasExtra("themeColor"))
            applyThemeColor = true;

        if (applyThemeColor) {
            defaultColor = prefManager.getThemeColor();
        } else {
            defaultColor = prefManager.getCategoryColor();
        }

        binding.counterLayout.setVisibility(View.INVISIBLE);

        binding.cartActionLayout.setVisibility(View.GONE);

        makeStatusBarSemiTranspenret(binding.toolbar);
        ViewTreeObserver viewTreeObserver = binding.scrollView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(() -> {

        });
        binding.backPress.setOnClickListener(v -> HELPER.ON_BACK_PRESS_ANIM(act));


        binding.titleToolbar.setText("");


        if (CONSTANT.API_MODE) {
            productId = getIntent().getStringExtra("productId");
            loadProductDetails();
        }

        binding.screenLoader.setProgressTintList(ColorStateList.valueOf(Color.parseColor(prefManager.getThemeColor())));
        binding.screenLoader.getIndeterminateDrawable().setColorFilter(Color.parseColor(prefManager.getThemeColor()), android.graphics.PorterDuff.Mode.MULTIPLY);


    }

    @Override
    public void onResume() {
        super.onResume();
        //loadData();
        //HELPER.changeCartCounterToolbar(act);
    }

    public void setImageSlide() {
        ArrayList<String> outCategoryModels = new ArrayList<>();
        outCategoryModels.add("");
        outCategoryModels.add("");
        outCategoryModels.add("");
        outCategoryModels.add("");
        ProductImageSliderAdpater sliderAdapter = new ProductImageSliderAdpater(outCategoryModels, act);
        binding.productImagesSlider.setAdapter(sliderAdapter);
        binding.dotsIndicator.setViewPager(binding.productImagesSlider);
    }

    public void changeTheme() {


        int code = ColorUtils.setAlphaComponent(Color.parseColor(defaultColor), 100);
        binding.dotsIndicator.setDotsColor(code);
        binding.dotsIndicator.setSelectedDotColor(Color.parseColor(defaultColor));
        binding.tabLayout.setTabTextColors((Color.WHITE), (Color.parseColor(defaultColor)));
        binding.cartBackgroundLayar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(defaultColor)));
        binding.detailContainer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(defaultColor)));
        binding.price.setTextColor(Color.parseColor(defaultColor));
        binding.cartIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(defaultColor)));
        GradientDrawable drawable = (GradientDrawable) binding.addCartContainer.getBackground();
        drawable.setStroke(2, Color.parseColor(defaultColor));

        VectorChildFinder vector = new VectorChildFinder(act, R.drawable.ic_not_returnable, binding.nonReturnIcon);
        VectorDrawableCompat.VFullPath path1 = vector.findPathByName("colorGreen");
        path1.setFillColor(Color.parseColor(defaultColor));
        binding.nonReturnIcon.invalidate();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HELPER.ON_BACK_PRESS_ANIM(act);
    }

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
        wrapContentHeightViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //  binding.scrollView.smoothScrollTo(binding.viewPager.getLeft(), binding.viewPager.getTop());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void loadData() {
        ArrayList<String> productImages = productModel.getProductImagesList();
        binding.titleToolbar.setText(productModel.getCategoryName());
        ProductImageSliderAdpater sliderAdapter = new ProductImageSliderAdpater(productImages, act);
        binding.productImagesSlider.setAdapter(sliderAdapter);
        binding.dotsIndicator.setViewPager(binding.productImagesSlider);
        int rate = Integer.parseInt(productModel.getRating());
        binding.ratting.setRating((float) rate);

        HELPER.LOAD_HTML(binding.nameTxt, productModel.getName());
        HELPER.LOAD_HTML(binding.shortDescription, productModel.getShortDescription());
        HELPER.LOAD_HTML(binding.categoryLabel, "<b>Category : </b>" + productModel.getCategoryName());
        HELPER.LOAD_HTML(binding.price, act.getString(R.string.Rs) + productModel.getPrice());

        if (productModel.isReturnAble()) {
            binding.returnAbleLAbel.setText("Returnable");
        } else {
            binding.returnAbleLAbel.setText("Non-Returnable");
        }
        StringBuilder tagsStr = new StringBuilder();
        if (productModel.getTagsModel() != null && productModel.getTagsModel().size() != 0) {
            for (int i = 0; i < productModel.getTagsModel().size(); i++) {
                tagsStr.append(productModel.getTagsModel().get(i).getTag()).append(", ");
            }
            HELPER.LOAD_HTML(binding.tagsLabel, "<b>Tags : <b>" + tagsStr);
        }
        binding.boottomFooter.setVisibility(View.GONE);
        //  binding.addTextTxt.setText("Qty : "+adpaterData.getQty());

    }

    private void loadProductDetails() {

        if (!isLoading)
            isLoading = true;

        binding.screenLoader.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.PRODUCT_DETAILS + productId, response -> {
            isLoading = false;

            JSONObject object = ResponseHandler.createJsonObject(response);
            productModel = ResponseHandler.getProductDetails(object);

            if (productModel != null) {
                binding.screenLoader.setVisibility(View.GONE);
                binding.NoDataFound.setVisibility(View.GONE);
                binding.scrollView.setVisibility(View.VISIBLE);
                binding.boottomFooter.setVisibility(View.VISIBLE);
                getIntent().putExtra("model", gson.toJson(productModel));
                setupTabIcons();
                changeTheme();
                setImageSlide();
                loadData();
            } else {
                binding.screenLoader.setVisibility(View.GONE);
                binding.NoDataFound.setVisibility(View.VISIBLE);
                binding.scrollView.setVisibility(View.GONE);
                binding.boottomFooter.setVisibility(View.GONE);
                Toast.makeText(act, "No Product Found", Toast.LENGTH_SHORT).show();
            }

        },
                error -> {
                    error.printStackTrace();
                    binding.screenLoader.setVisibility(View.GONE);
                    binding.NoDataFound.setVisibility(View.VISIBLE);
                    binding.scrollView.setVisibility(View.GONE);
                    binding.boottomFooter.setVisibility(View.GONE);
                    isLoading = false;
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {

                return getHeader();
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        MySingleton.getInstance(act).addToRequestQueue(stringRequest);
    }
}