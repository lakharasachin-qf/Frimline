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
import com.app.frimline.databinding.FragmentLoginVEmailBinding;
import com.app.frimline.screens.CategoryRootActivity;
import com.app.frimline.screens.ForgotPasswordActivity;

import org.jetbrains.annotations.NotNull;

public class LoginVEmailFragment extends BaseFragment {

    private FragmentLoginVEmailBinding binding;


    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_v_email, parent, false);
        ((ViewGroup) binding.getRoot().findViewById(R.id.containerLinear)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changeTheme();
    }

    private void changeTheme() {
        // binding.scrollView.setNestedScrollingEnabled(false);
        binding.includeBtn.button.setText("Sign In");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.hint.setText(Html.fromHtml("Please Sign in with <b>Email</b> to continue using<br>our app", Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.hint.setText(Html.fromHtml("Please Sign in with <b>Email</b> to continue using<br>our app"));
        }
        binding.includeBtn.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(getActivity()).getThemeColor())));
        binding.shipToDiffCheck.setButtonTintList(ColorStateList.valueOf(Color.parseColor(new PREF(getActivity()).getThemeColor())));
        HELPER.ERROR_HELPER(binding.userNameEdt, binding.userNameEdtLayout);
        HELPER.ERROR_HELPER(binding.confirmPassword, binding.confirmPasswordLayout);

        HELPER.ERROR_HELPER(binding.userNameEdt, binding.userNameEdtLayout);
        HELPER.ERROR_HELPER(binding.confirmPassword, binding.confirmPasswordLayout);


        binding.forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(getActivity(), ForgotPasswordActivity.class);
            }
        });
        binding.includeBtn.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PROTOTYPE_MODE) {
                    HELPER.SIMPLE_ROUTE(getActivity(), CategoryRootActivity.class);
                    getActivity().finish();
                } else {
                    if (!validations()) {
                        HELPER.SIMPLE_ROUTE(getActivity(), CategoryRootActivity.class);
                        getActivity().finish();
                    }
                }
            }
        });
    }

    public boolean validations() {

        boolean isError = false;
        boolean isFocus = false;
        if (binding.userNameEdt.getText().toString().trim().length() == 0) {
            isError = true;
            isFocus = true;
            binding.userNameEdtLayout.setErrorEnabled(true);
            binding.userNameEdtLayout.setError("Enter username or email id");
            binding.userNameEdt.requestFocus();
        }

        if (binding.confirmPassword.getText().toString().length() == 0) {
            isError = true;
            if (!isFocus) {
                isFocus = true;
                binding.confirmPassword.requestFocus();
            }
            binding.confirmPasswordLayout.setErrorEnabled(true);
            binding.confirmPasswordLayout.setError("Enter password");
        }

        return isError;
    }
}