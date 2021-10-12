package com.app.frimline.screens;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.core.graphics.ColorUtils;
import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.frimline.BaseActivity;
import com.app.frimline.Common.APIs;
import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.MySingleton;
import com.app.frimline.Common.PREF;
import com.app.frimline.Common.ResponseHandler;
import com.app.frimline.R;
import com.app.frimline.adapters.RecentBlogViewAdapter;
import com.app.frimline.databinding.ActivityBlogDetailsBinding;
import com.app.frimline.models.BlogModel;
import com.app.frimline.models.LAYOUT_TYPE;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlogDetailsActivity extends BaseActivity {
    private ActivityBlogDetailsBinding binding;
    private BlogModel model;
    private RecentBlogViewAdapter blogsAdapter;
    private ArrayList<BlogModel> rootModel;
    private boolean isLoading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_blog_details);
        makeStatusBarSemiTranspenret(binding.toolbar.toolbar);
        binding.toolbar.title.setText("Blog");
        binding.toolbar.backPress.setOnClickListener(v -> HELPER.ON_BACK_PRESS_ANIM(act));


        changeTheme();

        if (CONSTANT.API_MODE) {
            model = gson.fromJson(getIntent().getStringExtra("model"), BlogModel.class);
            startShimmer();
            loadData();
            loadBlog();
        } else {
            List<BlogModel> outCategoryModels = new ArrayList<>();
            outCategoryModels.add(new BlogModel());
            BlogModel model =new BlogModel();
            model.setLayoutType(LAYOUT_TYPE.LAYOUT_ONE_BLOG);
            outCategoryModels.add(model);
            binding.shimmerViewContainer.setVisibility(View.GONE);

            RecentBlogViewAdapter sliderAdapter = new RecentBlogViewAdapter(outCategoryModels, act);
            binding.viewPager.setAdapter(sliderAdapter);
            binding.dotsIndicator.setViewPager(binding.viewPager);
        }
    }

    public void loadData() {
        binding.demo.setVisibility(View.GONE);
        binding.longDescriptionTxt.setVisibility(View.VISIBLE);

        HELPER.LOAD_HTML(binding.title, model.getTitle());
        HELPER.LOAD_HTML(binding.longDescriptionTxt, model.getContent());
        Glide.with(act).load(model.getBlogImage())
                .placeholder(R.drawable.ic_banner_place_holder)
                .error(R.drawable.ic_banner_place_holder)
                .into(binding.blogImage);
    }

    public void changeTheme() {
        binding.view1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.view2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.view3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.view4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));

        binding.chip1.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.chip1.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.dotsIndicator.setSelectedDotColor(Color.parseColor(new PREF(act).getThemeColor()));
        int code = ColorUtils.setAlphaComponent(Color.parseColor(new PREF(act).getThemeColor()), 100);
        binding.dotsIndicator.setDotsColor(code);
        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
    }

    public void startShimmer() {
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();
        binding.recentContainer.setVisibility(View.GONE);
    }

    public void stopShimmer() {
        binding.shimmerViewContainer.setVisibility(View.GONE);
        binding.shimmerViewContainer.stopShimmer();
        binding.recentContainer.setVisibility(View.VISIBLE);
    }

    private void loadBlog() {

        if (!isLoading)
            isLoading = true;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.RECENT_BLOG, response -> {
            HELPER.print("Response", response);
            stopShimmer();
            isLoading = false;
            rootModel = ResponseHandler.handleResponseRecentBlog(response);
            blogsAdapter = new RecentBlogViewAdapter(rootModel, act);
            RecentBlogViewAdapter sliderAdapter = new RecentBlogViewAdapter(rootModel, act);
            binding.viewPager.setAdapter(sliderAdapter);
            binding.dotsIndicator.setViewPager(binding.viewPager);
        },
                error -> {
                    error.printStackTrace();
                    isLoading = false;
                    stopShimmer();
                    binding.recentContainer.setVisibility(View.GONE);
                }
        ) {
            /**
             * Passing some request headers*
             */
         /*   @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }*/

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        MySingleton.getInstance(act).addToRequestQueue(stringRequest);
    }
}