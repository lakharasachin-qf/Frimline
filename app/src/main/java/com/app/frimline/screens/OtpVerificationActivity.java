package com.app.frimline.screens;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECEIVE_SMS;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.app.frimline.Common.SMSListener;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivityOtpVerificationBinding;
import com.app.frimline.databinding.DialogDiscardImageBinding;
import com.app.frimline.intefaces.Common;
import com.app.frimline.models.Billing;
import com.app.frimline.models.ProfileModel;
import com.app.frimline.views.OTPListener;
import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class OtpVerificationActivity extends BaseActivity {
    Handler handler;
    Runnable r;
    DialogDiscardImageBinding discardImageBinding;
    private ActivityOtpVerificationBinding binding;
    private boolean isLoading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_otp_verification);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        changeTheme();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.backPress.getLayoutParams();
        layoutParams.topMargin = HELPER.getStatusBarHeight(act);
        binding.backPress.setLayoutParams(layoutParams);

        binding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        HELPER.LOAD_HTML(binding.mobileNoLabel, "Enter the OTP sent to <b>" + getIntent().getStringExtra("mobileNo") + "</b>");

        if (ContextCompat.checkSelfPermission(getApplicationContext(), RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
//            if (prefManager.isAskOTP()) {
//                askDialogForPermission("Permission", "You need to allow access to the permission for auto-read otp", 0);
//            }
        } else {
            SMSListener.bindListener(new Common.OTPListener() {
                @Override
                public void onOTPReceived(String extractedOTP) {
                    binding.otpView.setOTP(extractedOTP);
                }
            });
        }
        binding.otpView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(@NonNull String otp) {
                    HELPER.closeKeyboard(binding.otpView,act);
            }
        });
    }

    @Override
    protected void onDestroy() {
        SMSListener.unbindListener();
        super.onDestroy();
    }

    private void signIn() {
        if (!isLoading)
            isLoading = true;
        HELPER.showLoadingTran(act);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.SIGN_IN_MOBILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Login", response);
                isLoading = false;
                HELPER.dismissLoadingTran();
                JSONObject jsonObject = ResponseHandler.createJsonObject(response);
                if (jsonObject != null && ResponseHandler.getString(jsonObject, "status").equals("OK")) {
                    Toast.makeText(act, "Otp Send to your number", Toast.LENGTH_SHORT).show();
                } else {
                    HELPER.dismissLoadingTran();
                    errorDialog("Error", ResponseHandler.getString(jsonObject, "message"));
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
                                errorDialog("Error", ResponseHandler.getString(jsonObject, "message"));
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
                params.put("mobile", getIntent().getStringExtra("mobileNo"));
                return params;
            }
        };

        MySingleton.getInstance(act).addToRequestQueue(stringRequest);
    }

    public void changeTheme() {
        PREF pref = new PREF(act);

        binding.progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        binding.progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor(pref.getThemeColor()), android.graphics.PorterDuff.Mode.MULTIPLY);

        binding.otpLayout.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
        binding.containerVerified.setVisibility(View.GONE);
        binding.includeBtn.button.setText("Verify & Proceed");

        binding.includeBtn.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        binding.includeBtn1.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        binding.includeBtn1.button.setText("Done");
        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        binding.backgroundLayar2.setImageTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        binding.otpIcon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        binding.otpVerfiedIcon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        binding.otpTitle.setTextColor((Color.parseColor(pref.getThemeColor())));
        binding.verifiedTxt.setTextColor((Color.parseColor(pref.getThemeColor())));
        binding.resend.setTextColor((Color.parseColor(pref.getThemeColor())));

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


        binding.includeBtn.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpVerification();
                binding.container.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.containerVerified.setVisibility(View.GONE);


            }
        });

        binding.resendLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        binding.includeBtn1.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("success", "1");
                setResult(RESULT_OK, data);
                finish();

            }
        });

    }

    public void launch() {
        handler = new Handler();
        if (r != null)
            handler.removeCallbacks(r);

        r = new Runnable() {
            public void run() {
                binding.container.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);
                binding.containerVerified.setVisibility(View.VISIBLE);


            }
        };

        handler.postDelayed(r, 1000);


    }

    private void otpVerification() {

        if (!isLoading)
            isLoading = true;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.OTP_VERIFICATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Login", response);
                isLoading = false;

                JSONObject jsonObject = ResponseHandler.createJsonObject(response);
                if (jsonObject != null) {
                    binding.container.setVisibility(View.GONE);
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.containerVerified.setVisibility(View.GONE);

                    ProfileModel model = new ProfileModel();
                    model.setToken(ResponseHandler.getString(jsonObject, "token"));
                    model.setEmail(ResponseHandler.getString(jsonObject, "email"));
                    model.setDisplayName(ResponseHandler.getString(jsonObject, "user_display_name"));
                    prefManager.setUser(model);
                    prefManager.setLogin(true);
                    prefManager.setUserToken(model.getToken());
                    loadProfile();

                } else {
                    errorDialog("Error", ResponseHandler.getString(jsonObject, "message"));

                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        isLoading = false;
                        NetworkResponse response = error.networkResponse;
                        if (response!=null && response.statusCode == 400) {
                            try {
                                String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                                JSONObject jsonObject = new JSONObject(jsonString);
                                errorDialog("Error", ResponseHandler.getString(jsonObject, "message"));
                                binding.container.setVisibility(View.VISIBLE);
                                binding.progressBar.setVisibility(View.GONE);
                                binding.containerVerified.setVisibility(View.GONE);

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
                params.put("mobile", getIntent().getStringExtra("mobileNo"));
                params.put("otp", String.valueOf(binding.otpView.getOtp()));
                Log.e("param", params.toString());
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
//                if (!title.equalsIgnoreCase("Error")) {
//                    Intent data = new Intent();
//                    data.putExtra("success", "1");
//                    setResult(RESULT_OK, data);
//                    finish();
//
//                }

            }
        });
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }


    private void loadProfile() {

        if (!isLoading)
            isLoading = true;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoading = false;

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
                    prefManager.setUser(model);
                    FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.LOGIN);
                    FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.ADD_MENU);
                    Intent data = new Intent();
                    data.putExtra("success", "1");
                    setResult(RESULT_OK, data);
                    finish();
//
//                    binding.container.setVisibility(View.GONE);
//                    binding.progressBar.setVisibility(View.GONE);
//                    binding.containerVerified.setVisibility(View.VISIBLE);

                }

            }


        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        isLoading = false;
                    }
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = getHeader();
                Log.e("Header", params.toString());
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        MySingleton.getInstance(act).addToRequestQueue(stringRequest);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean targetSetting = false;
        if (requestCode == CONSTANT.REQUEST_CODE_READ_SMS) {
            if (grantResults.length > 0) {
                boolean cameraGrant = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (!cameraGrant) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(CAMERA)) {
                            askDialogForPermission("Permission", "You need to allow access to the permission for auto-read otp",1);
                        } else {
                            targetSetting = true;
                        }

                    }
                }
            }
        }

    }

     public void askDialogForPermission(String title, String msg,int code) {
        discardImageBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_discard_image, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(discardImageBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(discardImageBinding.getRoot());

        discardImageBinding.titleTxt.setText(title);
        HELPER.LOAD_HTML(discardImageBinding.subTitle, msg);
        discardImageBinding.yesTxt.setText("Allow");
        discardImageBinding.noTxt.setText("Cancel");
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
                if (code == 0){
                    ActivityCompat.requestPermissions(act, new String[]{RECEIVE_SMS}, CONSTANT.REQUEST_CODE_READ_SMS);
                }else {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    act.startActivityForResult(intent, CONSTANT.REQUEST_CODE_READ_SMS);
                }
            }
        });
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

}