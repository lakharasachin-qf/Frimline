package com.app.frimline.screens;

import android.animation.LayoutTransition;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.Common.Validators;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivityEditProfileBinding;
import com.app.frimline.databinding.DialogDiscardImageBinding;

public class EditProfileActivity extends BaseActivity {
    private ActivityEditProfileBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_edit_profile);
        //setStatusBarTransparent();

        makeStatusBarSemiTranspenret(binding.toolbar.toolbar);
        binding.toolbar.title.setText("Edit Profile");
        binding.toolbar.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.ON_BACK_PRESS_ANIM(act);
            }
        });
        ((ViewGroup) findViewById(R.id.containerLinear)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        changeTheme();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }

    public void changeTheme() {
        binding.includeBtn.button.setText("Save Profile");
        HELPER.ERROR_HELPER(binding.nameEdt, binding.nameEdtLayout);
        HELPER.ERROR_HELPER(binding.lnameEdt, binding.lnameEdtLayout);
        HELPER.ERROR_HELPER(binding.displayNameEdt, binding.displayNameEdtLayout);
        HELPER.ERROR_HELPER(binding.emailEdt, binding.emailEdtLayout);
        HELPER.ERROR_HELPER_For_MOBILE_VALIDATION(binding.phoneNoEdt, binding.phoneNoEdtLayout);
        HELPER.ERROR_HELPER(binding.newPasswordEdt, binding.newPasswordLayout);
        HELPER.ERROR_HELPER(binding.confirmPassword, binding.confirmPasswordLayout);


        HELPER.FOCUS_HELPER(binding.scrollView,binding.nameEdt, binding.nameEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView,binding.lnameEdt, binding.lnameEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView,binding.displayNameEdt, binding.displayNameEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView,binding.emailEdt, binding.emailEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView,binding.phoneNoEdt, binding.phoneNoEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView,binding.newPasswordEdt, binding.newPasswordLayout);
        HELPER.FOCUS_HELPER(binding.scrollView,binding.confirmPassword, binding.confirmPasswordLayout);




        binding.includeBtn.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.includeBtn.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PROTOTYPE_MODE) {
                    confirmationDialog();
                } else {
                    if (!validations()) {
                        confirmationDialog();
                    }
                }
            }
        });
    }

    public boolean validations() {
        boolean isError = false;
        boolean isFocus = false;

        if (binding.nameEdt.getText().toString().trim().length() == 0) {
            isError = true;
            isFocus = true;
            binding.nameEdtLayout.setError("Enter First Name");
            binding.nameEdt.requestFocus();
        }
        if (binding.lnameEdt.getText().toString().trim().length() == 0) {
            isError = true;
            binding.lnameEdtLayout.setError("Enter Last Name");
            if (!isFocus) {
                isFocus = true;
                binding.lnameEdt.requestFocus();
            }
        }

        if (binding.displayNameEdt.getText().toString().trim().length() == 0) {
            isError = true;
            binding.displayNameEdtLayout.setError("Enter Company Name");
            if (!isFocus) {
                isFocus = true;
                binding.displayNameEdt.requestFocus();
            }
        }

        if (binding.emailEdt.getText().toString().trim().length() == 0) {
            isError = true;
            binding.emailEdtLayout.setError("Enter Email Id");
            if (!isFocus) {
                isFocus = true;
                binding.emailEdt.requestFocus();
            }
        }
        if (!Validators.Companion.isEmailValid(binding.emailEdt.getText().toString().trim())) {
            isError = true;
            binding.emailEdtLayout.setError("Enter Valid Email Id");
            if (!isFocus) {
                isFocus = true;
                binding.emailEdt.requestFocus();
            }
        }

        if (binding.phoneNoEdt.getText().toString().trim().length() == 0) {
            isError = true;
            binding.phoneNoEdtLayout.setError("Enter Phone No.");
            if (!isFocus) {
                isFocus = true;
                binding.phoneNoEdt.requestFocus();
            }
        }
        if (binding.phoneNoEdt.getText().toString().trim().length() < 14) {
            isError = true;
            binding.phoneNoEdtLayout.setError("Enter Valid Phone No.");
            if (!isFocus) {
                isFocus = true;
                binding.phoneNoEdt.requestFocus();
            }
        }

        if (binding.newPasswordEdt.getText().toString().trim().length() == 0) {
            isError = true;
            binding.newPasswordLayout.setError("Enter Password");
            if (!isFocus) {
                isFocus = true;
                binding.newPasswordEdt.requestFocus();
            }
        }
        if (!Validators.Companion.isValidPassword(binding.newPasswordEdt.getText().toString())) {
            isError = true;
            binding.newPasswordLayout.setError("Password should be at least 12 characters, at least one uppercase and one lowercase letter, one number and one special character.");
            if (!isFocus) {
                isFocus = true;
                binding.newPasswordEdt.requestFocus();
            }
        }

        if (binding.confirmPassword.getText().toString().length() == 0) {
            isError = true;
            binding.confirmPasswordLayout.setError("Enter Confirm Password");
            if (!isFocus) {
                isFocus = true;
                binding.confirmPassword.requestFocus();
            }
        }
        if (!binding.newPasswordEdt.getText().toString().equalsIgnoreCase(binding.confirmPassword.getText().toString())) {
            isError = true;
            binding.confirmPasswordLayout.setError("Password doest not match.");
            if (!isFocus) {
                isFocus = true;
                binding.confirmPassword.requestFocus();
            }
        }


        return isError;
    }

    DialogDiscardImageBinding discardImageBinding;

    public void confirmationDialog() {
        discardImageBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_discard_image, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(discardImageBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(discardImageBinding.getRoot());
        discardImageBinding.titleTxt.setText("Profile Update");
        discardImageBinding.subTitle.setText("Your Profile Updated Successfully.");
        discardImageBinding.noTxt.setVisibility(View.GONE);
        discardImageBinding.noTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });
        discardImageBinding.yesTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                onBackPressed();
            }
        });
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
}