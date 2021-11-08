package com.app.frimline.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.app.frimline.common.APIs;
import com.app.frimline.common.CONSTANT;
import com.app.frimline.common.HELPER;
import com.app.frimline.common.MySingleton;
import com.app.frimline.common.ResponseHandler;
import com.app.frimline.R;
import com.app.frimline.adapters.BlogsAdapter;
import com.app.frimline.databinding.FragmentBlogsBinding;
import com.app.frimline.models.BlogModel;
import com.app.frimline.models.LAYOUT_TYPE;

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

            BlogsAdapter blogsAdapter = new BlogsAdapter(arrayList, getActivity());
            binding.blogsRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            binding.blogsRecycler.setAdapter(blogsAdapter);
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

        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.BLOGS, response -> {
            HELPER.print("reponse",response);
            stopShimmer();
            isLoading = false;
            rootModel = ResponseHandler.handleResponseBlogFragment(response);
            blogsAdapter = new BlogsAdapter(rootModel, getActivity());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
            binding.blogsRecycler.setHasFixedSize(true);
            binding.blogsRecycler.setNestedScrollingEnabled(false);
            binding.blogsRecycler.setLayoutManager(mLayoutManager);
            binding.blogsRecycler.setAdapter(blogsAdapter);
            binding.blogsRecycler.setVisibility(View.VISIBLE);
            binding.emptyData.setVisibility(View.GONE);
            if (rootModel==null || rootModel.size()==0){
                binding.blogsRecycler.setVisibility(View.GONE);
                binding.emptyData.setVisibility(View.VISIBLE);
                binding.emptyData.setText("No blogs found");
            }
        },
                error -> {
                    error.printStackTrace();
                    isLoading = false;
                    stopShimmer();
                    binding.blogsRecycler.setVisibility(View.GONE);
                    binding.emptyData.setText("No blogs found");
                    binding.emptyData.setVisibility(View.VISIBLE);
                }
        ) {
            /**
             * Passing some request headers*
             */

            @Override
            protected Map<String, String> getParams() {
                return new HashMap<>();
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}