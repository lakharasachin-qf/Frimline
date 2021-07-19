package com.app.frimline.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.databinding.FragmentMyAccountBinding;
import com.app.frimline.screens.AddressesActivity;
import com.app.frimline.screens.EditProfileActivity;

import org.jetbrains.annotations.NotNull;

public class MyAccountFragment extends Fragment {
    public  interface OnDrawerAction{
        void changeFragment();
    }
    private OnDrawerAction drawerAction;

    public void setDrawerAction(OnDrawerAction drawerAction) {
        this.drawerAction = drawerAction;
    }

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
        binding.orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerAction.changeFragment();
            }
        });


        return binding.getRoot();
    }

    public void changeColor(){
        PREF pref=new PREF(getActivity());
        binding.orderIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        binding.accountIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        binding.addressIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        binding.editIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));

        binding.logoutBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        GradientDrawable drawable = (GradientDrawable)binding.container.getBackground();
        drawable.setStroke(2, Color.parseColor(pref.getCategoryColor()));

    }


}