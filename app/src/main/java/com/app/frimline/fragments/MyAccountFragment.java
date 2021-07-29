package com.app.frimline.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.databinding.FragmentMyAccountBinding;
import com.app.frimline.screens.AddressesActivity;
import com.app.frimline.screens.EditProfileActivity;
import com.app.frimline.screens.LoginActivity;
import com.app.frimline.screens.SignupActivity;

import org.jetbrains.annotations.NotNull;

public class MyAccountFragment extends Fragment {
    public interface OnDrawerAction {
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_account, container, false);
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


        binding.includeLoginBtn.button.setText("Sign In");
        binding.includeSignupBtn.button.setText("Sign Up");
        binding.includeLoginBtn.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(getActivity(), LoginActivity.class);
            }
        });
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(getActivity(), LoginActivity.class);
            }
        });

        binding.includeSignupBtn.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(getActivity(), SignupActivity.class);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewStateRestored(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        changeColor();
    }

    public void changeColor() {
        PREF pref = new PREF(getActivity());
        binding.orderIcon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        binding.accountIcon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        binding.addressIcon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        binding.editIcon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));


        binding.logoutBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        binding.includeSignupBtn.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        binding.includeLoginBtn.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));

        binding.addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradientDrawable drawable = (GradientDrawable) binding.container.getBackground();
                drawable.setStroke(2, Color.parseColor(pref.getThemeColor()));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        HELPER.SIMPLE_ROUTE(getActivity(), AddressesActivity.class);
                        drawable.setStroke(2, ContextCompat.getColor(getActivity(), R.color.cardViewBorder));
                    }
                }, 40);
            }
        });
        binding.orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradientDrawable drawable = (GradientDrawable) binding.orderContainer.getBackground();
                drawable.setStroke(2, Color.parseColor(pref.getThemeColor()));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawerAction.changeFragment();
                        drawable.setStroke(2, ContextCompat.getColor(getActivity(), R.color.cardViewBorder));
                    }
                }, 40);
            }
        });
        binding.accountDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradientDrawable drawable = (GradientDrawable) binding.accountDetailsContainer.getBackground();
                drawable.setStroke(2, Color.parseColor(pref.getThemeColor()));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //drawerAction.changeFragment();
                        HELPER.SIMPLE_ROUTE(getActivity(), EditProfileActivity.class);
                        drawable.setStroke(2, ContextCompat.getColor(getActivity(), R.color.cardViewBorder));
                    }
                }, 40);
            }
        });
    }


}