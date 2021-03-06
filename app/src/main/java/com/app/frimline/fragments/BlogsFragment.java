package com.app.frimline.fragments;

import android.os.Bundle;
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
import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.MySingleton;
import com.app.frimline.Common.ResponseHandler;
import com.app.frimline.R;
import com.app.frimline.adapters.BlogsAdapter;
import com.app.frimline.databinding.FragmentBlogsBinding;
import com.app.frimline.models.BlogModel;
import com.app.frimline.models.CategoryRootFragments.CategorySingleModel;
import com.app.frimline.models.LAYOUT_TYPE;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BlogsFragment extends BaseFragment {


    private FragmentBlogsBinding binding;
    private BlogsAdapter blogsAdapter;


    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_blogs, container, false);


        if (CONSTANT.API_MODE) {
            startShimmer();
            loadBlog();

        } else {
            ArrayList<BlogModel> arrayList = new ArrayList<>();
            BlogModel homeModel = new BlogModel();
            homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_LEFT_BLOG);
            arrayList.add(homeModel);
            homeModel = new BlogModel();
            homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_RIGHT_BLOG);
            arrayList.add(homeModel);
            homeModel = new BlogModel();
            homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_LEFT_BLOG);
            arrayList.add(homeModel);


            BlogsAdapter adaptertop = new BlogsAdapter(arrayList, getActivity());
            binding.blogsRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            binding.blogsRecycler.setAdapter(adaptertop);
            binding.blogsRecycler.setVisibility(View.VISIBLE);
            binding.shimmerViewContainer.setVisibility(View.GONE);
            binding.shimmerViewContainer.stopShimmer();
            binding.blogsRecycler.setVisibility(View.VISIBLE);
        }
        return binding.getRoot();
    }


    public void startShimmer() {
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();
        binding.blogsRecycler.setVisibility(View.GONE);
        binding.emptyData.setVisibility(View.GONE);
    }

    public void stopShimmer() {
        binding.shimmerViewContainer.setVisibility(View.GONE);
        binding.shimmerViewContainer.stopShimmer();
        binding.blogsRecycler.setVisibility(View.VISIBLE);
    }


    private ArrayList<BlogModel> rootModel;
    private boolean isLoading = false;

    private void loadBlog() {

        if (!isLoading)
            isLoading = true;

        CategorySingleModel model = new Gson().fromJson(getActivity().getIntent().getStringExtra("model"), CategorySingleModel.class);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.BLOGS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("Response", response);
                stopShimmer();
                isLoading = false;
                rootModel = ResponseHandler.handleResponseBlogFragment(response);
                blogsAdapter = new BlogsAdapter(rootModel, getActivity());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
                binding.blogsRecycler.setHasFixedSize(true);
                binding.blogsRecycler.setNestedScrollingEnabled(false);
                binding.blogsRecycler.setLayoutManager(mLayoutManager);
                binding.blogsRecycler.setAdapter(blogsAdapter);
                //loadData(rootModel);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        isLoading = false;
                        stopShimmer();
                        binding.emptyData.setVisibility(View.VISIBLE);
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
}