package com.app.frimline.screens;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.frimline.BaseActivity;
import com.app.frimline.Common.APIs;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.Common.ResponseHandler;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivityPaymentBinding;
import com.app.frimline.databinding.DialogDiscardImageBinding;

import org.json.JSONObject;

import java.util.Map;

public class PaymentActivity extends BaseActivity {
    JSONObject orderParam;
    DialogDiscardImageBinding discardImageBinding;
    private ActivityPaymentBinding binding;
    private boolean isLoading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_payment);
        makeStatusBarSemiTranspenret(binding.toolbarNavigation.toolbar);
        binding.toolbarNavigation.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.ON_BACK_PRESS_ANIM(act);
            }
        });

        orderParam = ResponseHandler.createJsonObject(getIntent().getStringExtra("orderParam"));
        binding.toolbarNavigation.title.setText("Payment");
        changeTheme();
        paymentDone();
    }

    public void changeTheme() {
        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
    }

    private void paymentDone() {

        if (!isLoading)
            isLoading = true;
        //   HELPER.showLoadingTran(act);

        Log.e("orderParam", orderParam.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(APIs.CREATE_ORDER, orderParam, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                HELPER.dismissLoadingTran();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.wtf(error.getMessage(), "utf-8");
                HELPER.dismissLoadingTran();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeader();
            }
        };

        //  MySingleton.getInstance(act).addToRequestQueue(jsonObjectRequest);
    }

    public void infoAlert(String title, String msg) {
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
                Intent data = new Intent();
                if (title.equalsIgnoreCase("Error")) {
                    data.putExtra("failed", "1");
                    setResult(RESULT_CANCELED, data);
                } else {
                    data.putExtra("success", "1");
                    setResult(RESULT_OK, data);
                }
                finish();


            }
        });
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
}