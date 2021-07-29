package com.app.frimline.screens;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.Common.Validators;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivityCheckoutAddressBinding;
import com.app.frimline.fragments.ChooseListBottomFragment;
import com.app.frimline.intefaces.OnItemSelectListener;
import com.app.frimline.models.CountryModel;

public class CheckoutAddressActivity extends BaseActivity implements OnItemSelectListener {
    //Show Fragment For BrandList
    ChooseListBottomFragment bottomSheetFragment;
    private ActivityCheckoutAddressBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_checkout_address);
        makeStatusBarSemiTranspenret(binding.toolbarNavigation.toolbar);
        binding.toolbarNavigation.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.ON_BACK_PRESS_ANIM(act);
            }
        });

        binding.toolbarNavigation.title.setText("Checkout");
        binding.shipToDiffCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.otherInfo.setVisibility(View.VISIBLE);
                } else {
                    binding.otherInfo.setVisibility(View.GONE);
                }
            }
        });
        ((ViewGroup) findViewById(R.id.containerLinear)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        ((ViewGroup) findViewById(R.id.otherInfo)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        binding.includeBtn.button.setText("Proceed");
        binding.includeBtn.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PROTOTYPE_MODE) {
                    Intent i = new Intent(act, CategoryLandingActivity.class);
                    i.putExtra("targetCategory", "Yes");
                    i.putExtra("fragment", "Home");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {
                    if (!validations()) {
                        Intent i = new Intent(act, CategoryLandingActivity.class);
                        i.putExtra("targetCategory", "Yes");
                        i.putExtra("fragment", "Home");
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                }
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        changeTheme();
    }

    public void changeTheme() {
        binding.otherInfo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    binding.otherNoteEdtLayout.setBackground(ContextCompat.getDrawable(act, R.drawable.shape_editext_background_active));
                    GradientDrawable drawable = (GradientDrawable) binding.otherNoteEdtLayout.getBackground();
                    drawable.setStroke(2, Color.parseColor(new PREF(act).getThemeColor()));
                } else {
                    binding.otherNoteEdtLayout.setBackground(ContextCompat.getDrawable(act, R.drawable.shape_editext_background));
                }
            }
        });
        binding.shipToDiffCheck.setButtonTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.includeBtn.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));

        HELPER.ERROR_HELPER(binding.nameEdt, binding.nameEdtLayout);
        HELPER.ERROR_HELPER(binding.lnameEdt, binding.lnameEdtLayout);
        HELPER.ERROR_HELPER(binding.companyEdt, binding.companyEdtLayout);
        HELPER.ERROR_HELPER(binding.countryEdt, binding.countryEdtLayout);
        HELPER.ERROR_HELPER(binding.streetEdt, binding.streetEdtLayout);
        HELPER.ERROR_HELPER(binding.cityEdt, binding.cityEdtLayout);
        HELPER.ERROR_HELPER(binding.postalCodeEdt, binding.postalCodeEdtLayout);
        HELPER.ERROR_HELPER_For_MOBILE_VALIDATION(binding.phoneNoEdt, binding.phoneNoEdtLayout);
        HELPER.ERROR_HELPER(binding.emailEdt, binding.emailEdtLayout);

        HELPER.FOCUS_HELPER(binding.scrollView,binding.nameEdt, binding.nameEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView,binding.lnameEdt, binding.lnameEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView,binding.companyEdt, binding.companyEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView,binding.countryEdt, binding.countryEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView,binding.streetEdt, binding.streetEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView,binding.cityEdt, binding.cityEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView,binding.postalCodeEdt, binding.postalCodeEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView,binding.phoneNoEdt, binding.phoneNoEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView,binding.emailEdt, binding.emailEdtLayout);


        binding.countryEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentList();
            }
        });
    }

    public boolean validations() {

        boolean isError = false;
        boolean isFocus = false;

        if (binding.otherInfo.getVisibility() == View.VISIBLE) {
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

//            if (binding.companyEdt.getText().toString().trim().length() == 0) {
//                isError = true;
//                binding.companyEdtLayout.setError("Enter Company Name");
//                if (!isFocus) {
//                    isFocus = true;
//                    binding.companyEdt.requestFocus();
//                }
//            }

            if (binding.countryEdt.getText().toString().trim().length() == 0) {
                isError = true;
                binding.countryEdtLayout.setError("Enter Country");
                if (!isFocus) {
                    isFocus = true;
                    binding.countryEdt.requestFocus();
                }
            }
            if (binding.streetEdt.getText().toString().trim().length() == 0) {
                isError = true;
                binding.streetEdtLayout.setError("Enter Street");
                if (!isFocus) {
                    isFocus = true;
                    binding.streetEdt.requestFocus();
                }
            }
            if (binding.cityEdt.getText().toString().trim().length() == 0) {
                isError = true;
                binding.cityEdtLayout.setError("Enter City");
                if (!isFocus) {
                    isFocus = true;
                    binding.cityEdt.requestFocus();
                }
            }
            if (binding.postalCodeEdt.getText().toString().trim().length() == 0) {
                isError = true;
                binding.postalCodeEdtLayout.setError("Enter Postal Code.");
                if (!isFocus) {
                    isFocus = true;
                    binding.postalCodeEdt.requestFocus();
                }
            }
            if (binding.postalCodeEdt.getText().toString().trim().length() < 6) {
                isError = true;
                binding.postalCodeEdtLayout.setError("Enter 6 digit pincode");
                if (!isFocus) {
                    isFocus = true;
                    binding.postalCodeEdt.requestFocus();
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
            if (binding.phoneNoEdt.getText().toString().trim().length() < 10) {
                isError = true;
                binding.phoneNoEdtLayout.setError("Enter Valid Phone No.");
                if (!isFocus) {
                    isFocus = true;
                    binding.phoneNoEdt.requestFocus();
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
        }

        return isError;
    }

    public void showFragmentList() {
        bottomSheetFragment = new ChooseListBottomFragment();
        if (bottomSheetFragment.isVisible()) {
            bottomSheetFragment.dismiss();
        }
        if (bottomSheetFragment.isAdded()) {
            bottomSheetFragment.dismiss();
        }
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public void onItemSelect(CountryModel model, int postion) {
        binding.countryEdt.setText(model.getName());
        bottomSheetFragment.dismiss();
    }
}