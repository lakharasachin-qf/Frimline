package com.app.frimline.fragments;

import android.animation.LayoutTransition;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.databinding.FragmentLoginVMobileBinding;
import com.app.frimline.screens.ForgotPasswordActivity;
import com.app.frimline.screens.OtpVerificationActivity;

import org.jetbrains.annotations.NotNull;

public class LoginVMobileFragment extends BaseFragment {

    private FragmentLoginVMobileBinding binding;


    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_v_mobile, parent, false);
        ((ViewGroup) binding.getRoot().findViewById(R.id.containerLinear)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changeTheme();

    }

    public void changeTheme() {
        //binding.scrollView.setNestedScrollingEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.hint.setText(Html.fromHtml("Please Sign in with <b>Mobile</b> to continue using<br>our app", Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.hint.setText(Html.fromHtml("Please Sign in with <b>Mobile</b> to continue using<br>our app"));
        }
        binding.includeBtn.button.setText("Get OTP");
        binding.includeBtn.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(getActivity()).getThemeColor())));
        binding.shipToDiffCheck.setButtonTintList(ColorStateList.valueOf(Color.parseColor(new PREF(getActivity()).getThemeColor())));
        HELPER.ERROR_HELPER_For_MOBILE_VALIDATION(binding.nameEdt, binding.nameEdtLayout);
        binding.includeBtn.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PROTOTYPE_MODE) {
                    HELPER.SIMPLE_ROUTE(getActivity(), OtpVerificationActivity.class);
                } else {
                    if (binding.nameEdt.getText().toString().length() == 10) {
                        HELPER.SIMPLE_ROUTE(getActivity(), OtpVerificationActivity.class);
                    } else if (binding.nameEdt.getText().toString().trim().length() == 0) {
                        binding.nameEdt.requestFocus();
                        binding.nameEdtLayout.setError("Enter Mobile No.");
                    } else {
                        binding.nameEdt.requestFocus();
                        binding.nameEdtLayout.setError("Enter Valid Mobile No.");
                    }
                }

            }
        });

        binding.forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(getActivity(), ForgotPasswordActivity.class);
            }
        });

    }
}