package com.app.frimline.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.frimline.Common.HELPER;
import com.app.frimline.R;
import com.app.frimline.databinding.FragmentMyAccountBinding;
import com.app.frimline.screens.AddressesActivity;
import com.app.frimline.screens.EditProfileActivity;

import org.jetbrains.annotations.NotNull;

public class MyAccountFragment extends Fragment {

    private FragmentMyAccountBinding binding;
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_account,container,false);
        binding.editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(getActivity(), EditProfileActivity.class);
            }
        });
        binding.addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(getActivity(), AddressesActivity.class);
            }
        });


        return binding.getRoot();
    }


}