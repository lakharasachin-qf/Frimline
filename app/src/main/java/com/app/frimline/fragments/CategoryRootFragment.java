package com.app.frimline.fragments;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.frimline.Common.APIs;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.MySingleton;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.adapters.HomeBannerAdapter;
import com.app.frimline.databinding.FragmentCategoryRootBinding;
import com.app.frimline.models.OutCategoryModel;
import com.app.frimline.screens.CategoryLandingActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CategoryRootFragment extends BaseFragment {
    private FragmentCategoryRootBinding binding;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_root, parent, false);
        ((ViewGroup) binding.getRoot().findViewById(R.id.containerLinear)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        HELPER.changeThemeCategoryRootFragment(binding, pref.getCategoryColor());
        //getTodayTomorrow();
        binding.cat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.setConfiguration("#EF7F1A", "#12C0DD");
                Intent intent = new Intent(getActivity(), CategoryLandingActivity.class);
                startActivity(intent);
            }
        });
        binding.cat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.setConfiguration("#EF7F1A", "#12C0DD");
                //pref.setConfiguration("#EF7F1A","#81B533");
                Intent intent = new Intent(getActivity(), CategoryLandingActivity.class);
                startActivity(intent);
            }
        });
        binding.cat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.setConfiguration("#EF7F1A", "#12C0DD");
                //pref.setConfiguration("#EF7F1A","#E8AE21");
                Intent intent = new Intent(getActivity(), CategoryLandingActivity.class);
                startActivity(intent);
            }
        });
        List<OutCategoryModel> outCategoryModels = new ArrayList<>();
        outCategoryModels.add(new OutCategoryModel());
        outCategoryModels.add(new OutCategoryModel());
        outCategoryModels.add(new OutCategoryModel());
        outCategoryModels.add(new OutCategoryModel());
        HomeBannerAdapter sliderAdapter = new HomeBannerAdapter(outCategoryModels, getActivity());
        binding.viewPager.setAdapter(sliderAdapter);
        binding.dot.setViewPager(binding.viewPager);
        binding.dot.setSelectedDotColor(Color.parseColor(new PREF(getActivity()).getThemeColor()));
        return binding.getRoot();
    }


    private void getTodayTomorrow() {
        HELPER.showLoadingTran(act);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.TODAY_TOMORROW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("Accept", "application/x-www-form-urlencoded");//application/json
//                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }


}