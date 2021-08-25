package com.app.frimline.fragments;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.app.frimline.Common.APIs;
import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.FRIMLINE;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.MySingleton;
import com.app.frimline.Common.ObserverActionID;
import com.app.frimline.Common.PREF;
import com.app.frimline.Common.ResponseHandler;
import com.app.frimline.R;
import com.app.frimline.databinding.DialogDiscardImageBinding;
import com.app.frimline.databinding.FragmentLoginVMobileBinding;
import com.app.frimline.models.Billing;
import com.app.frimline.models.ProfileModel;
import com.app.frimline.screens.CategoryRootActivity;
import com.app.frimline.screens.ForgotPasswordActivity;
import com.app.frimline.screens.OtpVerificationActivity;
import com.app.frimline.screens.SignupActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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

        binding.nameEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));


        binding.includeBtn.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PROTOTYPE_MODE) {
                    HELPER.SIMPLE_ROUTE(getActivity(), OtpVerificationActivity.class);
                } else {
                    if (binding.nameEdt.getText().toString().length() == 10) {
                        if (CONSTANT.API_MODE) {
                            signIn();
                        } else {
                            HELPER.SIMPLE_ROUTE(getActivity(), CategoryRootActivity.class);
                            act.finish();
                        }
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


    private boolean isLoading = false;


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
                    openSomeActivityForResult();


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
                        if (response.statusCode == 400) {
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
                params.put("mobile", binding.nameEdt.getText().toString());
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

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent data = result.getData();
                        if (data.hasExtra("success")) {
                            FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.LOGIN);
                            act.onBackPressed();
                        }

                    }
                }
            });

    public void openSomeActivityForResult() {
        Intent intent = new Intent(act, OtpVerificationActivity.class);
        intent.putExtra("mobileNo",binding.nameEdt.getText().toString());
        someActivityResultLauncher.launch(intent);
    }

    private void loadProfile() {

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
                    act.onBackPressed();

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
}