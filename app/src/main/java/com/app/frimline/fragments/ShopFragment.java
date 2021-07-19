package com.app.frimline.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.frimline.R;
import com.app.frimline.databinding.FragmentShopBinding;


public class ShopFragment extends Fragment {
    private FragmentShopBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_shop,container,false);





        return binding.getRoot();
    }
}