package com.app.frimline.screens;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
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
import com.app.frimline.databinding.ActivitySignupBinding;
import com.app.frimline.databinding.DialogDiscardImageBinding;
import com.app.frimline.models.Billing;
import com.app.frimline.models.ProfileModel;
import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends BaseActivity {
    DialogDiscardImageBinding discardImageBinding;
    private ActivitySignupBinding binding;
    private boolean isLoading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_signup);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ((ViewGroup) findViewById(R.id.containerLinear)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        changeTheme();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        loginWidth();
    }

    public void loginWidth() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int fragmentWidth = (width / 6) + 20;

        if (HELPER.isTablet(act)) {
            fragmentWidth = (width / 6) + 30;
            RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams) binding.logo.getLayoutParams();
            layoutParams.topMargin = fragmentWidth;
            binding.logo.setLayoutParams(layoutParams);
        }


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
                    if (!validations()) {
                        if (CONSTANT.API_MODE) {
                            signUp();
                        } else {
                            onBackPressed();
                        }
                    }
                }
            }
        });
        HELPER.ERROR_HELPER(binding.nameTxt, binding.nameTxtLayout);
        HELPER.ERROR_HELPER(binding.emailEdt, binding.emailEdtLayout);
        HELPER.ERROR_HELPER(binding.phoneNoEdt, binding.phoneNoEdtLayout);
        HELPER.ERROR_HELPER(binding.newPasswordEdt, binding.newPasswordLayout);


        HELPER.FOCUS_HELPER(binding.scrollView, binding.nameTxt, binding.nameTxtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView, binding.emailEdt, binding.emailEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView, binding.phoneNoEdt, binding.phoneNoEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView, binding.newPasswordEdt, binding.newPasswordLayout);


        binding.nameTxtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.emailEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.phoneNoEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.newPasswordLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));


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

        } else if (!Validators.Companion.isEmailValid(binding.emailEdt.getText().toString())) {
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
        } else if (binding.phoneNoEdt.getText().toString().length() < 10) {
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
        } else if (!Validators.Companion.isValidPassword(binding.newPasswordEdt.getText().toString())) {
            isError = true;
            if (!isFocus) {
                isFocus = true;
                binding.newPasswordEdt.requestFocus();
            }
            binding.newPasswordLayout.setError("Password should be at least 8 characters, at least one uppercase and one lowercase letter, one number and one special character.");

            //  binding.newPasswordLayout.setError("Enter valid password");
            binding.newPasswordLayout.setErrorEnabled(true);
        }


        return isError;

    }

    private void signUp() {

        if (!isLoading)
            isLoading = true;
        HELPER.showLoadingTran(act);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.SIGN_UP, response -> {

            Log.e("Sign up - Response", response);
            isLoading = false;
            binding.screenLoader.setVisibility(View.GONE);
            binding.scrollView.setVisibility(View.VISIBLE);
            JSONObject object = ResponseHandler.createJsonObject(response);
            if (ResponseHandler.getString(object, "code").equalsIgnoreCase("200")) {
                ProfileModel model = ResponseHandler.parseSignUpResponse(response);
                if (model != null) {
                    prefManager.setUserToken(model.getToken());
                    prefManager.setUser(model);
                    prefManager.setLogin(true);
                    loadProfile(ResponseHandler.getString(object, "message"));
                }
            } else {
                HELPER.dismissLoadingTran();
                errorDialog("Error", ResponseHandler.getString(object, "message"));
            }
        }, error -> {
            HELPER.dismissLoadingTran();
            NetworkResponse response = error.networkResponse;
            error.printStackTrace();
            if (response!=null && response.statusCode == 400) {
                try {
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    JSONObject jsonObject = new JSONObject(jsonString);
                    errorDialog("Error", ResponseHandler.getString(jsonObject, "message"));
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
                Log.e("Error", gson.toJson(response.headers));
                Log.e("allHeaders", gson.toJson(response.allHeaders));

            }
            isLoading = false;
            binding.screenLoader.setVisibility(View.GONE);
            binding.scrollView.setVisibility(View.VISIBLE);
        }
        ) {
            /**
             * Passing some request headers*
             */

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", binding.nameTxt.getText().toString());
                params.put("email", binding.emailEdt.getText().toString());
                params.put("password", binding.newPasswordEdt.getText().toString());
                params.put("phone", binding.phoneNoEdt.getText().toString());
                return params;
            }
        };

        MySingleton.getInstance(act).addToRequestQueue(stringRequest);
    }

    public void errorDialog(String title, String msg) {
        discardImageBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_discard_image, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(discardImageBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(discardImageBinding.getRoot());

        discardImageBinding.titleTxt.setText(title);
        discardImageBinding.subTitle.setText(msg);
        discardImageBinding.yesTxt.setText("Ok");
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
                if (!title.equalsIgnoreCase("Error")) {
                    FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.LOGIN);
                    FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.ADD_MENU);

                    Intent data = new Intent();
                    data.putExtra("success", "1");
                    setResult(RESULT_OK, data);
                    finish();
                }

            }
        });
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    private void loadProfile(String message) {

        if (!isLoading)
            isLoading = true;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoading = false;
                HELPER.dismissLoadingTran();
                binding.screenLoader.setVisibility(View.GONE);

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
                    errorDialog("Sign Up", message);

                }


            }


        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        HELPER.dismissLoadingTran();
                        isLoading = false;
                        binding.screenLoader.setVisibility(View.GONE);
                    }
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return getHeader();
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        MySingleton.getInstance(act).addToRequestQueue(stringRequest);
    }
}