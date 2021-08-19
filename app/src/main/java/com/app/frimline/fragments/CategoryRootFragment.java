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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.frimline.Common.APIs;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.MySingleton;
import com.app.frimline.Common.PREF;
import com.app.frimline.Common.ResponseHandler;
import com.app.frimline.R;
import com.app.frimline.adapters.CatBannerAdapter;
import com.app.frimline.adapters.CategoryAdapter;
import com.app.frimline.adapters.TodaysTomorrowAdapter;
import com.app.frimline.databinding.FragmentCategoryRootBinding;
import com.app.frimline.models.CategoryRootFragments.CategoryRootModel;
import com.app.frimline.models.CategoryRootFragments.TodaysModel;
import com.app.frimline.models.LAYOUT_TYPE;
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
        HELPER.changeThemeCategoryRootFragment(binding, pref.getThemeColor());

        if (API_MODE) {
            startShimmer();
            getTodayTomorrow();
        } else {
            binding.shimmerViewContainer.setVisibility(View.GONE);
            binding.categoryProductRecycler.setVisibility(View.VISIBLE);
            binding.scrollView.setVisibility(View.VISIBLE);

            List<String> outCategoryModels = new ArrayList<>();
            outCategoryModels.add("");
            outCategoryModels.add("");
            outCategoryModels.add("");
            outCategoryModels.add("");
            CatBannerAdapter sliderAdapter = new CatBannerAdapter(outCategoryModels, getActivity());
            binding.viewPager.setAdapter(sliderAdapter);
            binding.dot.setViewPager(binding.viewPager);
            binding.dot.setSelectedDotColor(Color.parseColor(new PREF(getActivity()).getThemeColor()));

        }

        binding.cat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (API_MODE) {
                    pref.setConfiguration(pref.getThemeColor(), "#12C0DD");

                } else {
                    pref.setConfiguration("#EF7F1A", "#12C0DD");

                }
                Intent intent = new Intent(getActivity(), CategoryLandingActivity.class);
                startActivity(intent);
            }
        });
        binding.cat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (API_MODE) {
                    pref.setConfiguration(pref.getThemeColor(), "#12C0DD");
                } else {
                    pref.setConfiguration("#EF7F1A", "#12C0DD");
                    //pref.setConfiguration("#EF7F1A","#81B533");

                }
                Intent intent = new Intent(getActivity(), CategoryLandingActivity.class);
                startActivity(intent);
            }
        });
        binding.cat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (API_MODE) {
                    pref.setConfiguration(pref.getThemeColor(), "#12C0DD");
                } else {
                    pref.setConfiguration("#EF7F1A", "#12C0DD");
                    //pref.setConfiguration("#EF7F1A","#E8AE21");
                }
                Intent intent = new Intent(getActivity(), CategoryLandingActivity.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    public void startShimmer() {
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();
        binding.categoryProductRecycler.setVisibility(View.GONE);
        binding.scrollView.setVisibility(View.GONE);
    }

    public void stopShimmer() {
        binding.shimmerViewContainer.setVisibility(View.GONE);
        binding.shimmerViewContainer.stopShimmer();
        binding.scrollView.setVisibility(View.VISIBLE);
        binding.categoryProductRecycler.setVisibility(View.VISIBLE);
    }


    private CategoryRootModel rootModel;
    private boolean isLoading = false;

    private void getTodayTomorrow() {

        if (!isLoading)
            isLoading = true;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.TODAY_TOMORROW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("Response", response);
                stopShimmer();
                isLoading = false;
                rootModel = ResponseHandler.handleResponseCategoryRootFragment(response);
                loadData(rootModel);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        isLoading = false;
                        stopShimmer();
                    }
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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


    public void loadData(CategoryRootModel rootModel) {
        if (API_MODE) {
            binding.layout1.setVisibility(View.GONE);
            binding.layout2.setVisibility(View.GONE);
            binding.layout3.setVisibility(View.GONE);
            binding.layout4.setVisibility(View.GONE);

            binding.todaysRecycler.setVisibility(View.VISIBLE);
            if (!rootModel.getMessages().isEmpty()) {
                TodaysTomorrowAdapter adapter = new TodaysTomorrowAdapter(prepareData(), act);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
                binding.todaysRecycler.setNestedScrollingEnabled(false);
                binding.todaysRecycler.setLayoutManager(mLayoutManager);
                binding.todaysRecycler.setAdapter(adapter);

                CategoryAdapter categoryAdapter = new CategoryAdapter(rootModel.getCategoryList(), act);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(act, RecyclerView.HORIZONTAL, false);
                binding.categoryRecycler.setNestedScrollingEnabled(false);
                binding.categoryRecycler.setLayoutManager(layoutManager);
                binding.categoryRecycler.setAdapter(categoryAdapter);
                binding.categoryRecycler.setVisibility(View.VISIBLE);
                binding.dummyContainer.setVisibility(View.GONE);
            } else {
                //show error message
            }
            loadBanner();
        }
    }

    public void loadBanner() {
            CatBannerAdapter sliderAdapter = new CatBannerAdapter(rootModel.getBannerList(), getActivity());
            binding.viewPager.setAdapter(sliderAdapter);
            binding.dot.setViewPager(binding.viewPager);
            binding.dot.setSelectedDotColor(Color.parseColor(new PREF(act).getThemeColor()));
    }

    public ArrayList<TodaysModel> prepareData() {
        String message = rootModel.getMessages();

        String[] msgList = message.split("\\.");
        ArrayList<TodaysModel> list = new ArrayList<>();
        int i = 0;
        for (String str : msgList) {
            TodaysModel model = new TodaysModel();
            if (i == 0) {
                model.setLayoutType(LAYOUT_TYPE.LAYOUT_LEFT_BLOG);
            } else if (i == 1) {
                model.setLayoutType(LAYOUT_TYPE.LAYOUT_RIGHT_BLOG);
            } else {
                if ((i % 2) == 0) {
                    model.setLayoutType(LAYOUT_TYPE.LAYOUT_LEFT_BLOG);
                } else {
                    model.setLayoutType(LAYOUT_TYPE.LAYOUT_RIGHT_BLOG);
                }
            }
            Log.e("" + i, String.valueOf(model.getLayoutType()));
            model.setMessage(str.trim() + ".");
            list.add(model);
            i++;
        }

        return list;
    }

}