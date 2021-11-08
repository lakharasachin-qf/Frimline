package com.app.frimline.fragments;

import android.animation.LayoutTransition;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.app.frimline.common.APIs;
import com.app.frimline.common.CONSTANT;
import com.app.frimline.common.FRIMLINE;
import com.app.frimline.common.HELPER;
import com.app.frimline.common.MySingleton;
import com.app.frimline.common.ObserverActionID;
import com.app.frimline.common.PREF;
import com.app.frimline.common.ResponseHandler;
import com.app.frimline.R;
import com.app.frimline.databinding.DialogDiscardImageBinding;
import com.app.frimline.databinding.FragmentLoginVEmailBinding;
import com.app.frimline.models.Billing;
import com.app.frimline.models.ProfileModel;
import com.app.frimline.screens.CategoryRootActivity;
import com.app.frimline.screens.ForgotPasswordActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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
        binding.includeBtn.button.setText(R.string.sign_in);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.hint.setText(Html.fromHtml("Please Sign in with <b>Email</b> to continue using<br>our app", Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.hint.setText(Html.fromHtml("Please Sign in with <b>Email</b> to continue using<br>our app"));
        }
        binding.includeBtn.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.shipToDiffCheck.setButtonTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        HELPER.ERROR_HELPER(binding.userNameEdt, binding.userNameEdtLayout);
        HELPER.ERROR_HELPER(binding.confirmPassword, binding.confirmPasswordLayout);

        HELPER.ERROR_HELPER(binding.userNameEdt, binding.userNameEdtLayout);
        HELPER.ERROR_HELPER(binding.confirmPassword, binding.confirmPasswordLayout);

        binding.userNameEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.confirmPasswordLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));

        binding.userNameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.userNameEdtLayout.setError("");
                binding.userNameEdtLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (pref.getPassword(s.toString()) != null && !pref.getPassword(s.toString()).isEmpty()) {
                    binding.confirmPassword.setText(pref.getPassword(binding.userNameEdt.getText().toString()));
                    HELPER.closeKeyboard(binding.confirmPassword, act);
                }
            }
        });

        binding.forgetPassword.setOnClickListener(v -> HELPER.SIMPLE_ROUTE(getActivity(), ForgotPasswordActivity.class));
        binding.includeBtn.button.setOnClickListener(v -> {
            if (PROTOTYPE_MODE) {
                HELPER.SIMPLE_ROUTE(getActivity(), CategoryRootActivity.class);
                act.finish();
            } else {
                if (!validations()) {
                    if (CONSTANT.API_MODE) {

                        signIn();
                    } else {
                        HELPER.SIMPLE_ROUTE(getActivity(), CategoryRootActivity.class);
                        act.finish();
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
                binding.confirmPassword.requestFocus();
            }
            binding.confirmPasswordLayout.setErrorEnabled(true);
            binding.confirmPasswordLayout.setError("Enter password");
        }

        return isError;
    }


    private boolean isLoading = false;


    private void signIn() {

        if (!isLoading)
            isLoading = true;
        HELPER.showLoadingTran(act);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.SIGN_IN, response -> {
            HELPER.print("response", response);
            isLoading = false;

            JSONObject jsonObject = ResponseHandler.createJsonObject(response);
            if (jsonObject != null && !jsonObject.has("code")) {
                if (binding.shipToDiffCheck.isChecked()) {
                    pref.addRememberMe(binding.userNameEdt.getText().toString(), binding.confirmPassword.getText().toString());
                }
                ProfileModel model = new ProfileModel();
                model.setToken(ResponseHandler.getString(jsonObject, "token"));
                model.setEmail(ResponseHandler.getString(jsonObject, "email"));
                model.setDisplayName(ResponseHandler.getString(jsonObject, "user_display_name"));
                pref.setUser(model);
                pref.setLogin(true);
                pref.setUserToken(model.getToken());
                loadProfile();

            } else {
                HELPER.dismissLoadingTran();
                if (jsonObject != null && ResponseHandler.getString(jsonObject, "code").contains("invalid_username") || ResponseHandler.getString(jsonObject, "code").contains("invalid_email")) {
                    errorDialog("Error", ResponseHandler.getString(jsonObject, "message"));
                }
                if (ResponseHandler.getString(jsonObject, "code").contains("password")) {
                    errorDialog("Error", ResponseHandler.getString(jsonObject, "message"));
                }
            }

        },
                error -> {
                    error.printStackTrace();
                    isLoading = false;
                    HELPER.dismissLoadingTran();
                    NetworkResponse response = error.networkResponse;
                    if (response != null && response.statusCode == 400) {
                        try {
                            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            JSONObject jsonObject = new JSONObject(jsonString);
                            errorDialog("Error", ResponseHandler.getString(jsonObject, "message"));
                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                        }
                        HELPER.print("Error", gson.toJson(response.headers));

                    }

                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() {
                return new HashMap<>();
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", binding.userNameEdt.getText().toString());
                params.put("password", binding.confirmPassword.getText().toString());
                return params;
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    DialogDiscardImageBinding discardImageBinding;

    public void errorDialog(String title, String msg) {
        discardImageBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_discard_image, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(discardImageBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(discardImageBinding.getRoot());

        discardImageBinding.titleTxt.setText(title);
        HELPER.LOAD_HTML(discardImageBinding.subTitle, msg);
        discardImageBinding.yesTxt.setText(R.string.ok);
        discardImageBinding.noTxt.setVisibility(View.GONE);
        discardImageBinding.noTxt.setOnClickListener(v -> alertDialog.dismiss());
        discardImageBinding.yesTxt.setOnClickListener(v -> alertDialog.dismiss());
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (!act.isDestroyed() && !act.isFinishing()) {
            alertDialog.show();
        }
    }


    private void loadProfile() {

        if (!isLoading)
            isLoading = true;
        HELPER.showLoadingTran(act);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.PROFILE, response -> {
            isLoading = false;
            HELPER.dismissLoadingTran();
            JSONObject object = ResponseHandler.createJsonObject(response);
            if (object != null) {

                ProfileModel model = new ProfileModel();
                model.setUserId(ResponseHandler.getString(object, "id"));
                model.setEmail(ResponseHandler.getString(object, "email"));
                model.setPhoneNo(ResponseHandler.getString(object, "user_phone"));
                model.setDisplayName(ResponseHandler.getString(object, "user_display_name"));
                model.setFirstName(ResponseHandler.getString(object, "first_name"));
                model.setLastName(ResponseHandler.getString(object, "last_name"));
                model.setRole(ResponseHandler.getString(object, "role"));
                model.setUserName(ResponseHandler.getString(object, "username"));
                model.setAvatar(ResponseHandler.getString(object, "avatar_url"));
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
                pref.setUser(model);
                FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.LOGIN);
                FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.ADD_MENU);
                act.onBackPressed();

            }

        },
                error -> {
                    error.printStackTrace();
                    isLoading = false;
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