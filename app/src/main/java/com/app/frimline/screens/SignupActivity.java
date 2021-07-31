package com.app.frimline.screens;

import android.animation.LayoutTransition;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.databinding.DataBindingUtil;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.Common.Validators;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivitySignupBinding;
import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;

public class SignupActivity extends BaseActivity {
    private ActivitySignupBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_signup);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ((ViewGroup) findViewById(R.id.containerLinear)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        changeTheme();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }

    public void changeTheme() {
        binding.includeBtn.button.setText("Sign Up");
        binding.includeBtn.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.tabContainer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.backgroundLayar2.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.signupTxt1.setTextColor((Color.parseColor(new PREF(act).getThemeColor())));
        ImageView logo = act.findViewById(R.id.logo);
        VectorChildFinder vector = new VectorChildFinder(act, R.drawable.ic_logo_green, logo);
        VectorDrawableCompat.VFullPath path1 = vector.findPathByName("background");
        path1.setFillColor(Color.parseColor(new PREF(act).getThemeColor()));
        logo.invalidate();
        binding.signupTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.backPress.getLayoutParams();
        layoutParams.topMargin = HELPER.getStatusBarHeight(act);
        binding.backPress.setLayoutParams(layoutParams);

        binding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.includeBtn.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PROTOTYPE_MODE) {
                    onBackPressed();
                } else {
                    if (!validations())
                        onBackPressed();
                }
            }
        });
        HELPER.ERROR_HELPER(binding.nameTxt, binding.nameTxtLayout);
        HELPER.ERROR_HELPER(binding.emailEdt, binding.emailEdtLayout);
        HELPER.ERROR_HELPER(binding.phoneNoEdt, binding.phoneNoEdtLayout);
        HELPER.ERROR_HELPER(binding.newPasswordEdt, binding.newPasswordLayout);


        HELPER.FOCUS_HELPER(binding.scrollView,binding.nameTxt, binding.nameTxtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView,binding.emailEdt, binding.emailEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView,binding.phoneNoEdt, binding.phoneNoEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView,binding.newPasswordEdt, binding.newPasswordLayout);


        binding.nameTxtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.emailEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.phoneNoEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.newPasswordLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));


        binding.phoneNoEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.phoneNoEdtLayout.setError("");
                binding.phoneNoEdtLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith("+91 ")) {
                    binding.phoneNoEdt.setText("+91 ");
                    Selection.setSelection(binding.phoneNoEdt.getText(), binding.phoneNoEdt.getText().length());

                }
            }
        });
    }

    public boolean validations() {
        boolean isError = false;
        boolean isFocus = false;

        if (binding.nameTxt.getText().toString().trim().length() == 0) {
            isError = true;
            isFocus = true;
            binding.nameTxtLayout.setError("Enter username");
            binding.nameTxt.requestFocus();
            binding.nameTxtLayout.setErrorEnabled(true);

        }
        if (binding.emailEdt.getText().toString().trim().length() == 0) {
            isError = true;
            if (!isFocus) {
                isFocus = true;
                binding.emailEdt.requestFocus();
            }
            binding.emailEdtLayout.setError("Enter email id");
            binding.emailEdtLayout.setErrorEnabled(true);

        }
        if (!Validators.Companion.isEmailValid(binding.emailEdt.getText().toString())) {
            isError = true;
            if (!isFocus) {
                isFocus = true;
                binding.emailEdt.requestFocus();
            }
            binding.emailEdtLayout.setError("Enter valid email id");
            binding.emailEdtLayout.setErrorEnabled(true);

        }
        if (binding.phoneNoEdt.getText().toString().trim().length() == 0) {
            isError = true;
            if (!isFocus) {
                isFocus = true;
                binding.phoneNoEdt.requestFocus();
            }
            binding.phoneNoEdtLayout.setError("Enter phone no.");
            binding.phoneNoEdtLayout.setErrorEnabled(true);
        }
        if (binding.phoneNoEdt.getText().toString().length() < 14) {
            isError = true;
            if (!isFocus) {
                isFocus = true;
                binding.phoneNoEdt.requestFocus();
            }
            binding.phoneNoEdtLayout.setError("Enter valid phone no.");
            binding.phoneNoEdtLayout.setErrorEnabled(true);
        }
        if (binding.newPasswordEdt.getText().toString().trim().length() == 0) {
            isError = true;
            if (!isFocus) {
                isFocus = true;
                binding.newPasswordEdt.requestFocus();
            }
            binding.newPasswordLayout.setError("Enter password");
            binding.newPasswordLayout.setErrorEnabled(true);
        }
        if (!Validators.Companion.isValidPassword(binding.newPasswordEdt.getText().toString())) {
            isError = true;
            if (!isFocus) {
                isFocus = true;
                binding.newPasswordEdt.requestFocus();
            }
            binding.newPasswordLayout.setError("Password should be at least 12 characters, at least one uppercase and one lowercase letter, one number and one special character.");

            //  binding.newPasswordLayout.setError("Enter valid password");
            binding.newPasswordLayout.setErrorEnabled(true);
        }



        return isError;

    }

}