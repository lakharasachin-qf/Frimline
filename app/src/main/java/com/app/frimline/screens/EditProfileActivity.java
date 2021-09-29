package com.app.frimline.screens;

import android.animation.LayoutTransition;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.frimline.BaseActivity;
import com.app.frimline.Common.APIs;
import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.FRIMLINE;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.MySingleton;
import com.app.frimline.Common.ObserverActionID;
import com.app.frimline.Common.PREF;
import com.app.frimline.Common.ResponseHandler;
import com.app.frimline.Common.Validators;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivityEditProfileBinding;
import com.app.frimline.databinding.DialogDiscardImageBinding;
import com.app.frimline.models.Billing;
import com.app.frimline.models.ProfileModel;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends BaseActivity {
    DialogDiscardImageBinding discardImageBinding;
    private ActivityEditProfileBinding binding;
    private boolean isLoading = false;

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
        if (CONSTANT.API_MODE) {
            loadData();
        }
    }

    public void loadData() {
        ProfileModel model = prefManager.getUser();
        binding.nameEdt.setText(model.getFirstName());
        binding.lnameEdt.setText(model.getLastName());
        binding.displayNameEdt.setText(model.getDisplayName());
        binding.phoneNoEdt.setText(model.getPhoneNo());
        binding.emailEdt.setText(model.getEmail());

        binding.phoneNoEdt.setEnabled(false);
        binding.emailEdt.setEnabled(false);
        binding.phoneNoEdtLayout.setEnabled(false);
        binding.emailEdtLayout.setEnabled(false);

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

        HELPER.FOCUS_HELPER(binding.scrollView, binding.nameEdt, binding.nameEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView, binding.lnameEdt, binding.lnameEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView, binding.displayNameEdt, binding.displayNameEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView, binding.emailEdt, binding.emailEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView, binding.phoneNoEdt, binding.phoneNoEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView, binding.newPasswordEdt, binding.newPasswordLayout);
        HELPER.FOCUS_HELPER(binding.scrollView, binding.confirmPassword, binding.confirmPasswordLayout);
        binding.nameEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.lnameEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.displayNameEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.emailEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.phoneNoEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.newPasswordLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.confirmPasswordLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));

        binding.includeBtn.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.includeBtn.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validations()) {
                    if (CONSTANT.API_MODE) {
                        loadProfile();
                    } else {
                        confirmationDialog("Profile Update", getString(R.string.profile_update));
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
        if (binding.emailEdt.isEnabled()) {
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
        if (binding.phoneNoEdt.isEnabled()) {
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
        }
        if (!binding.newPasswordEdt.getText().toString().trim().isEmpty()) {

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
                binding.newPasswordLayout.setError("Password should be at least 8 characters, at least one uppercase and one lowercase letter, one number and one special character.");
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
            } else {
                if (!binding.newPasswordEdt.getText().toString().equals(binding.confirmPassword.getText().toString())) {
                    isError = true;
                    binding.confirmPasswordLayout.setError("Password doest not match.");
                    if (!isFocus) {
                        isFocus = true;
                        binding.confirmPassword.requestFocus();
                    }
                }

            }
        }


        return isError;
    }

    public void confirmationDialog(String title, String msg) {
        discardImageBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_discard_image, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(discardImageBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(discardImageBinding.getRoot());
        discardImageBinding.titleTxt.setText(title);
        discardImageBinding.subTitle.setText(msg);
        discardImageBinding.noTxt.setVisibility(View.GONE);
        discardImageBinding.noTxt.setOnClickListener(v -> alertDialog.dismiss());
        discardImageBinding.yesTxt.setOnClickListener(v -> {
            alertDialog.dismiss();
            FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.LOGOUT);
            onBackPressed();
        });
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    public void loadProfile() {

        if (!isLoading)
            isLoading = true;

        HELPER.showLoadingTran(act);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.UPDATE_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoading = false;
                HELPER.dismissLoadingTran();
                JSONObject object = ResponseHandler.createJsonObject(response);
                if (object != null && ResponseHandler.getString(object, "code").equals("200")) {
                    ProfileModel model = new ProfileModel();
                    model.setPhoneNo(ResponseHandler.getString(object, "user_phone"));
                    model.setDisplayName(ResponseHandler.getString(object, "user_display_name"));
                    model.setUserId(ResponseHandler.getString(object, "id"));
                    model.setEmail(ResponseHandler.getString(object, "email"));
                    model.setFirstName(ResponseHandler.getString(object, "first_name"));
                    model.setLastName(ResponseHandler.getString(object, "last_name"));
                    model.setRole(ResponseHandler.getString(object, "role"));
                    model.setUserName(ResponseHandler.getString(object, "username"));
                    model.setUserName(ResponseHandler.getString(object, "username"));
                    model.setAvatar(ResponseHandler.getString(object, "avatar_url"));

                    loadUSER(ResponseHandler.getString(object, "message"));
                }


            }


        },
                error -> {
                    error.printStackTrace();
                    HELPER.dismissLoadingTran();
                    isLoading = false;
                    confirmationDialog("Error", getString(R.string.we_are_getting_problem_in_update));

                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() {
                return getHeader();
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (!binding.newPasswordEdt.getText().toString().trim().isEmpty())
                    params.put("password", binding.newPasswordEdt.getText().toString());

                params.put("first_name", binding.nameEdt.getText().toString());
                params.put("last_name", binding.lnameEdt.getText().toString());
                params.put("display_name", binding.displayNameEdt.getText().toString());
                params.put("phone", binding.phoneNoEdt.getText().toString());

                return params;
            }
        };

        MySingleton.getInstance(act).addToRequestQueue(stringRequest);
    }


    private void loadUSER(String msg) {

        if (!isLoading)
            isLoading = true;
        HELPER.showLoadingTran(act);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoading = false;
                HELPER.dismissLoadingTran();
                JSONObject object = ResponseHandler.createJsonObject(response);
                if (object != null) {
                    ProfileModel model = new ProfileModel();
                    model.setPhoneNo(ResponseHandler.getString(object, "user_phone"));
                    model.setDisplayName(ResponseHandler.getString(object, "user_display_name"));
                    model.setUserId(ResponseHandler.getString(object, "id"));
                    model.setEmail(ResponseHandler.getString(object, "email"));
                    model.setFirstName(ResponseHandler.getString(object, "first_name"));
                    model.setLastName(ResponseHandler.getString(object, "last_name"));
                    model.setRole(ResponseHandler.getString(object, "role"));
                    model.setUserName(ResponseHandler.getString(object, "username"));
                    model.setUserName(ResponseHandler.getString(object, "username"));
                    model.setAvatar(ResponseHandler.getString(object, "avatar_url"));


                    Billing billingAddress = new Billing();
                    billingAddress.setFirstName(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "billing"), "first_name"));
                    billingAddress.setLastName(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "billing"), "last_name"));
                    billingAddress.setCompany(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "billing"), "company"));
                    billingAddress.setAddress1(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "billing"), "address_1"));
                    billingAddress.setAddress2(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "billing"), "address_2"));
                    billingAddress.setCity(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "billing"), "city"));
                    billingAddress.setPostCode(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "billing"), "postcode"));
                    billingAddress.setCountry(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "billing"), "country"));
                    billingAddress.setState(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "billing"), "state"));
                    billingAddress.setEmail(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "billing"), "email"));
                    billingAddress.setPhone(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "billing"), "phone"));
                    model.setBillingAddress(billingAddress);

                    billingAddress = new Billing();
                    billingAddress.setFirstName(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "first_name"));
                    billingAddress.setLastName(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "last_name"));
                    billingAddress.setCompany(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "company"));
                    billingAddress.setAddress1(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "address_1"));
                    billingAddress.setAddress2(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "address_2"));
                    billingAddress.setCity(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "city"));
                    billingAddress.setPostCode(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "postcode"));
                    billingAddress.setCountry(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "country"));
                    billingAddress.setState(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "state"));
                    model.setShippingAddress(billingAddress);

                    prefManager.setUser(model);
                    FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.LOGIN);
                    confirmationDialog("Profile Update", msg);

                }


            }


        },
                error -> {
                    error.printStackTrace();
                    isLoading = false;
                    HELPER.dismissLoadingTran();
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() {
                return getHeader();
            }

            @Override
            protected Map<String, String> getParams() {
                return new HashMap<>();
            }
        };

        MySingleton.getInstance(act).addToRequestQueue(stringRequest);
    }
}