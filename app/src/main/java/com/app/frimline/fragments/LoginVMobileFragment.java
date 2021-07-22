package com.app.frimline.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.databinding.FragmentLoginVMobileBinding;
import com.app.frimline.screens.OtpVerificationActivity;

import org.jetbrains.annotations.NotNull;

public class LoginVMobileFragment extends Fragment {

    private FragmentLoginVMobileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_v_mobile, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changeTheme();

    }

    public void changeTheme() {
        binding.includeBtn.button.setText("Get OTP");
        binding.includeBtn.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(getActivity()).getCategoryColor())));
        binding.shipToDiffCheck.setButtonTintList(ColorStateList.valueOf(Color.parseColor(new PREF(getActivity()).getCategoryColor())));
        HELPER.SET_STYLE(getActivity(), binding.mobileNoEdt, binding.mobileNoEdtLayout);
        binding.includeBtn.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(getActivity(), OtpVerificationActivity.class);
            }
        });


    }
}