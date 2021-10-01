package com.app.frimline.fragments;

import static android.Manifest.permission.RECEIVE_SMS;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.app.frimline.Common.APIs;
import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.MySingleton;
import com.app.frimline.Common.ObserverActionID;
import com.app.frimline.Common.PREF;
import com.app.frimline.Common.ResponseHandler;
import com.app.frimline.R;
import com.app.frimline.databinding.DialogDiscardImageBinding;
import com.app.frimline.databinding.FragmentLoginVMobileBinding;
import com.app.frimline.screens.CategoryRootActivity;
import com.app.frimline.screens.ForgotPasswordActivity;
import com.app.frimline.screens.OtpVerificationActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.hint.setText(Html.fromHtml("Please Sign in with <b>Mobile</b> to continue using<br>our app", Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.hint.setText(Html.fromHtml("Please Sign in with <b>Mobile</b> to continue using<br>our app"));
        }
        binding.includeBtn.button.setText(R.string.get_otp);
        binding.includeBtn.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.shipToDiffCheck.setButtonTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        HELPER.ERROR_HELPER_For_MOBILE_VALIDATION(binding.nameEdt, binding.nameEdtLayout);

        binding.nameEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));


        binding.includeBtn.button.setOnClickListener(v -> {
            if (PROTOTYPE_MODE) {
                HELPER.SIMPLE_ROUTE(getActivity(), OtpVerificationActivity.class);
            } else {
                if (binding.nameEdt.getText().toString().length() == 10) {
                    if (CONSTANT.API_MODE) {
                        if (pref.isAskOTP()) {
                            if (ContextCompat.checkSelfPermission(act, RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                                askDialogForPermission("Permission", getString(R.string.you_need_permission_of_otp_read));
                            } else {
                                signIn();
                            }
                        } else {
                            signIn();
                        }
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

        });

        binding.forgetPassword.setOnClickListener(v -> HELPER.SIMPLE_ROUTE(getActivity(), ForgotPasswordActivity.class));

    }

    public void askDialogForPermission(String title, String msg) {
        discardImageBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_discard_image, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(discardImageBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(discardImageBinding.getRoot());
        discardImageBinding.titleTxt.setText(title);
        HELPER.LOAD_HTML(discardImageBinding.subTitle, msg);
        discardImageBinding.yesTxt.setText(R.string.allow);
        discardImageBinding.noTxt.setText(R.string.cancel);
        discardImageBinding.noTxt.setOnClickListener(v -> {
            alertDialog.dismiss();
            signIn();
        });
        discardImageBinding.yesTxt.setOnClickListener(v -> {
            alertDialog.dismiss();
            ActivityCompat.requestPermissions(act, new String[]{RECEIVE_SMS}, CONSTANT.REQUEST_CODE_READ_SMS);
        });
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (!act.isDestroyed() && !act.isFinishing()) {
            alertDialog.show();
        }
    }


    private boolean isLoading = false;


    private void signIn() {

        if (isLoading)
            return;

        isLoading = true;
        HELPER.showLoadingTran(act);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.SIGN_IN_MOBILE, response -> {

            HELPER.print("Login", response);
            isLoading = false;
            HELPER.dismissLoadingTran();
            JSONObject jsonObject = ResponseHandler.createJsonObject(response);
            if (jsonObject != null && ResponseHandler.getString(jsonObject, "status").equals("OK")) {
                openSomeActivityForResult();
            } else {
                errorDialog("Error", ResponseHandler.getString(jsonObject, "message"));

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
                    }

                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return new HashMap<>();
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

    @Override
    public void update(Observable observable, Object data) {
        super.update(observable, data);
        if (frimline.getObserver().getValue() == ObserverActionID.SMS_PERMISSION_ACCEPTED) {
            act.runOnUiThread(this::signIn);
        } else if (frimline.getObserver().getValue() == ObserverActionID.SMS_PERMISSION_CANCELLED) {
            act.runOnUiThread(this::signIn);
        }
    }

    public void callApi() {
        signIn();
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

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {

                    Intent data = result.getData();
                    if (data != null && data.hasExtra("success")) {
                        act.onBackPressed();
                    }

                }
            });

    public void openSomeActivityForResult() {
        Intent intent = new Intent(act, OtpVerificationActivity.class);
        intent.putExtra("mobileNo", binding.nameEdt.getText().toString());
        someActivityResultLauncher.launch(intent);
    }

}