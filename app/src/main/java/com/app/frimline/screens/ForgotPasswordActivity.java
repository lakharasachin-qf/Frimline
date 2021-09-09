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
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.MySingleton;
import com.app.frimline.Common.PREF;
import com.app.frimline.Common.ResponseHandler;
import com.app.frimline.Common.Validators;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivityMobileVerificationBinding;
import com.app.frimline.databinding.DialogDiscardImageBinding;
import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends BaseActivity {
    DialogDiscardImageBinding discardImageBinding;
    private ActivityMobileVerificationBinding binding;
    private boolean isLoading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_mobile_verification);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ((ViewGroup) findViewById(R.id.containerLinear)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.backPress.getLayoutParams();
        layoutParams.topMargin = HELPER.getStatusBarHeight(act);
        binding.backPress.setLayoutParams(layoutParams);

        binding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        changeTheme();
    }

    public void changeTheme() {
        PREF pref = new PREF(act);

        binding.includeBtn.button.setText("Reset");
        binding.includeBtn1.button.setText("Done");
        binding.includeBtn1.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.includeBtn.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PROTOTYPE_MODE) {
                    HELPER.SIMPLE_ROUTE(act, ResetPasswordActivity.class);


                } else {
//                    if (binding.phoneNoEdt.getText().toString().length() == 10) {
//                        HELPER.SIMPLE_ROUTE(act, OtpVerificationActivity.class);
//                        finish();
//                    } else if (binding.phoneNoEdt.getText().toString().trim().length() == 0) {
//                        binding.phoneNoEdt.requestFocus();
//                        binding.phoneNoEdtLayout.setError("Enter Mobile No.");
//                    } else {
//                        binding.phoneNoEdt.requestFocus();
//                        binding.phoneNoEdtLayout.setError("Enter Valid Mobile No.");
//                    }

                    if (Validators.Companion.isEmailValid(binding.phoneNoEdt.getText().toString())) {

                        if (CONSTANT.API_MODE) {
                            lostPassword();
                        } else {
                            HELPER.SIMPLE_ROUTE(act, ResetPasswordActivity.class);
                            finish();
                        }

                    } else if (binding.phoneNoEdt.getText().toString().trim().length() == 0) {
                        binding.phoneNoEdt.requestFocus();
                        binding.phoneNoEdtLayout.setError("Enter email id");
                    } else {
                        binding.phoneNoEdt.requestFocus();
                        binding.phoneNoEdtLayout.setError("Enter valid Email Id");
                    }
                }
            }
        });
        binding.includeBtn1.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        binding.includeBtn.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        binding.phoneNoEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        binding.backgroundLayar2.setImageTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        binding.otpIcon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        binding.otpTitle.setTextColor((Color.parseColor(pref.getThemeColor())));
        binding.verifiedTxt.setTextColor((Color.parseColor(pref.getThemeColor())));
        binding.otpVerfiedIcon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));

        VectorChildFinder vector = new VectorChildFinder(act, R.drawable.ic_mobile_password, binding.otpIcon);
        VectorDrawableCompat.VFullPath path1 = vector.findPathByName("colorGreen");
        path1.setFillColor(Color.parseColor(new PREF(act).getThemeColor()));
        path1 = vector.findPathByName("colorGreen1");
        path1.setFillColor(Color.parseColor(new PREF(act).getThemeColor()));



        vector = new VectorChildFinder(act, R.drawable.ic_otp_verified, binding.otpVerfiedIcon);
        path1 = vector.findPathByName("colorGreen1");
        path1.setFillColor(Color.parseColor(new PREF(act).getThemeColor()));

        path1 = vector.findPathByName("colorGreen2");
        path1.setFillColor(Color.parseColor(new PREF(act).getThemeColor()));

        path1 = vector.findPathByName("colorGreen3");
        path1.setFillColor(Color.parseColor(new PREF(act).getThemeColor()));

        path1 = vector.findPathByName("colorGreen4");
        path1.setFillColor(Color.parseColor(new PREF(act).getThemeColor()));
        path1 = vector.findPathByName("colorGreen5");
        path1.setFillColor(Color.parseColor(new PREF(act).getThemeColor()));

        HELPER.ERROR_HELPER_For_MOBILE_VALIDATION(binding.phoneNoEdt, binding.phoneNoEdtLayout);

    }

    private void lostPassword() {

        if (!isLoading)
            isLoading = true;
        HELPER.showLoadingTran(act);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.FORGOT_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Login", response);
                isLoading = false;
                HELPER.dismissLoadingTran();
                JSONObject jsonObject = ResponseHandler.createJsonObject(response);
                if (jsonObject != null && jsonObject.has("code")) {
                    if (ResponseHandler.getString(jsonObject, "code").equals("200")) {
                        //errorDialog("Forgot Password", ResponseHandler.getString(jsonObject, "msg"), true);
                        binding.verifiedTxt.setText("Email Sent");
                        binding.msgSent.setText("We sent you a reset password link on entered email id. Click on the link and reset your password");
                        binding.resetPasswordContainer.setVisibility(View.GONE);
                        binding.containerVerified.setVisibility(View.VISIBLE);
                    } else {
                        errorDialog("Forgot Password", ResponseHandler.getString(jsonObject, "msg"), false);
                    }

                } else {
                    HELPER.dismissLoadingTran();
                    errorDialog("Error", ResponseHandler.getString(jsonObject, "msg"), false);

                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        isLoading = false;
                        HELPER.dismissLoadingTran();
                        NetworkResponse response = error.networkResponse;
                        if (response!=null && response.statusCode == 400) {

                            try {
                                String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                                JSONObject jsonObject = new JSONObject(jsonString);
                                errorDialog("Error", ResponseHandler.getString(jsonObject, "msg"), false);
                            } catch (UnsupportedEncodingException | JSONException e) {
                                e.printStackTrace();
                            }
                            Log.e("Error", gson.toJson(response.headers));
                            Log.e("allHeaders", gson.toJson(response.allHeaders));
                        }

                    }
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", binding.phoneNoEdt.getText().toString());
                return params;
            }
        };

        MySingleton.getInstance(act).addToRequestQueue(stringRequest);
    }

    public void errorDialog(String title, String msg, boolean isSuccess) {
        discardImageBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_discard_image, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(discardImageBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(discardImageBinding.getRoot());

        discardImageBinding.titleTxt.setText(title);
        HELPER.LOAD_HTML(discardImageBinding.subTitle, msg);
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


            }
        });
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

}