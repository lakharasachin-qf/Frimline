package com.app.frimline.fragments;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import com.app.frimline.Common.HELPER;
import com.app.frimline.R;
import com.app.frimline.adapters.MultiViewAdapterForHomeFragment;
import com.app.frimline.adapters.OurProductAdapter;
import com.app.frimline.adapters.TopRattedProductAdapter;
import com.app.frimline.adapters.TrendingProductAdapter;
import com.app.frimline.databinding.FragmentHomeBinding;
import com.app.frimline.models.HomeModel;
import com.app.frimline.models.LAYOUT_TYPE;
import com.app.frimline.models.OutCategoryModel;
import com.app.frimline.models.ProductModel;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment {


    private FragmentHomeBinding binding;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        HELPER.changeThemeHomeFragment(binding, pref.getCategoryColor());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.titleTxt.setText(Html.fromHtml("<b>Due to <font color='#FF0707'>COVID-19,</font></b>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.titleTxt.setText(Html.fromHtml("<b>Due to <font color='#FF0707'>COVID-19,</font></b>"));
        }

        binding.shimmerViewContainer.startShimmer();
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        setAdapterForProduct();
        setAdapterForOurProduct();
        setAdapterForTrendingProduct();
        setAdapterForTopRattedProduct();
        launch();
        return binding.getRoot();
    }

    Handler handler;
    Runnable r;

    public void launch() {
        handler = new Handler();
        if (r != null)
            handler.removeCallbacks(r);

        r = new Runnable() {
            public void run() {

                binding.shimmerViewContainer.stopShimmer();
                binding.shimmerViewContainer.setVisibility(View.GONE);

            }
        };

        handler.postDelayed(r, 3000);
    }

    public void setAdapterForOurProduct() {
        ArrayList<OutCategoryModel> modelArrayList = new ArrayList<>();
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        OurProductAdapter productAdapter = new OurProductAdapter(modelArrayList, getActivity());
        binding.ourProductSection.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.ourProductSection.setAdapter(productAdapter);
    }

    public void setAdapterForTrendingProduct() {
        ArrayList<OutCategoryModel> modelArrayList = new ArrayList<>();
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        TrendingProductAdapter productAdapter = new TrendingProductAdapter(modelArrayList, getActivity());
        binding.trendingSectionRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.trendingSectionRecycler.setAdapter(productAdapter);
    }

    public void setAdapterForTopRattedProduct() {
        ArrayList<OutCategoryModel> modelArrayList = new ArrayList<>();
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        TopRattedProductAdapter productAdapter = new TopRattedProductAdapter(modelArrayList, getActivity());
        binding.topRattingProductRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.topRattingProductRecycler.setAdapter(productAdapter);
    }

    ArrayList<HomeModel> productArray = new ArrayList<>();

    public void setAdapterForProduct() {
        ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
        ProductModel productModel = new ProductModel();
        productModelArrayList.add(productModel);
        productModelArrayList.add(productModel);
        productModelArrayList.add(productModel);

        HomeModel homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_THREE_PRODUCT);
        homeModel.setProductModels(productModelArrayList);

        productArray.add(homeModel);
        productArray.add(homeModel);
        productArray.add(homeModel);


        MultiViewAdapterForHomeFragment productAdapter = new MultiViewAdapterForHomeFragment(productArray, getActivity());
        binding.categoryProductRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.categoryProductRecycler.setHasFixedSize(true);
        SnapHelper startSnapHelper = new PagerSnapHelper();
        binding.categoryProductRecycler.setOnFlingListener(null);
        startSnapHelper.attachToRecyclerView(binding.categoryProductRecycler);
        binding.categoryProductRecycler.setAdapter(productAdapter);
    }
}