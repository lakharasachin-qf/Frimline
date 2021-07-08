package com.app.frimline.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.frimline.R;
import com.app.frimline.adapters.OurProductAdapter;
import com.app.frimline.adapters.TopRattedProductAdapter;
import com.app.frimline.adapters.TrendingProductAdapter;
import com.app.frimline.databinding.FragmentHomeBinding;
import com.app.frimline.models.OutCategoryModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {


    private FragmentHomeBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.titleTxt.setText(Html.fromHtml("<b>Due to <font color='#FF0707'>COVID-19,</font></b>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.titleTxt.setText(Html.fromHtml("<b>Due to <font color='#FF0707'>COVID-19,</font></b>"));
        }

        binding.shimmerViewContainer.startShimmer();
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
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

    public void setAdapterForOurProduct(){
        ArrayList<OutCategoryModel> modelArrayList=new ArrayList<>();
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        OurProductAdapter productAdapter =new OurProductAdapter(modelArrayList,getActivity());
        binding.ourProductSection.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.ourProductSection.setAdapter(productAdapter);
    }

    public void setAdapterForTrendingProduct(){
        ArrayList<OutCategoryModel> modelArrayList=new ArrayList<>();
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        TrendingProductAdapter productAdapter =new TrendingProductAdapter(modelArrayList,getActivity());
        binding.trendingSectionRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.trendingSectionRecycler.setAdapter(productAdapter);
    }

    public void setAdapterForTopRattedProduct(){
        ArrayList<OutCategoryModel> modelArrayList=new ArrayList<>();
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        TopRattedProductAdapter productAdapter =new TopRattedProductAdapter(modelArrayList,getActivity());
        binding.topRattingProductRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.topRattingProductRecycler.setAdapter(productAdapter);
    }
}