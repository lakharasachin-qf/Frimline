package com.app.frimline.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.frimline.R;

import com.app.frimline.adapters.OrderHistoryAdapter;
import com.app.frimline.databinding.FragmentOrderHistoryBinding;
import com.app.frimline.models.OutCategoryModel;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OrderHistoryFragment extends Fragment {

    private FragmentOrderHistoryBinding binding;
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_order_history,container,false);
        setAdapterForOurProduct();
        return binding.getRoot();
    }
    public void setAdapterForOurProduct(){
        ArrayList<OutCategoryModel> modelArrayList=new ArrayList<>();
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        OrderHistoryAdapter productAdapter =new OrderHistoryAdapter(modelArrayList,getActivity());
        binding.orderHistoryRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.orderHistoryRecycler.setAdapter(productAdapter);
    }

}