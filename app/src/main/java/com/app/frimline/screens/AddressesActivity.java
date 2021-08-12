package com.app.frimline.screens;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.frimline.BaseActivity;
import com.app.frimline.Common.APIs;
import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.MySingleton;
import com.app.frimline.Common.PREF;
import com.app.frimline.Common.ResponseHandler;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivityAddressesBinding;
import com.app.frimline.databinding.IncludeActivityToolbarLayoutBinding;
import com.app.frimline.models.Billing;
import com.app.frimline.models.ProfileModel;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddressesActivity extends BaseActivity {
    private ActivityAddressesBinding binding;
    private IncludeActivityToolbarLayoutBinding toolbarBinding;
    private boolean isBilling = false;
    private boolean isShipping = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_addresses);
        //setStatusBarTransparent();

        makeStatusBarSemiTranspenret(binding.toolbar.toolbar);

        binding.toolbar.title.setText("Addresses");
        binding.toolbar.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.ON_BACK_PRESS_ANIM(act);
            }
        });

        changeTheme();
        if (CONSTANT.API_MODE) {
            stopShimmer();
            loadData();
        }
    }

    public void startShimmer() {
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();
        binding.scrollView.setVisibility(View.GONE);

    }

    public void stopShimmer() {
        binding.shimmerViewContainer.setVisibility(View.GONE);
        binding.shimmerViewContainer.stopShimmer();
        binding.scrollView.setVisibility(View.VISIBLE);

    }

    public void loadData() {
        prefManager = new PREF(act);
        String billingAddress = "";
        Billing billing = prefManager.getUser().getBillingAddress();
        if (!billing.getAddress1().isEmpty()) {
            billingAddress = billingAddress + billing.getAddress1();
        }
        if (!billing.getAddress2().isEmpty()) {
            billingAddress = billingAddress + ", " + billing.getAddress2();
        }

        if (!billing.getCity().isEmpty()) {
            billingAddress = billingAddress + ", " + billing.getCity();
        }
        if (!billing.getState().isEmpty()) {
            billingAddress = billingAddress + ", " + billing.getState();
        }
        if (!billing.getCountry().isEmpty()) {
            billingAddress = billingAddress + ", " + billing.getCountry();
        }
        if (!billing.getPostCode().isEmpty()) {
            billingAddress = billingAddress + " - " + billing.getPostCode();
        }

        if (!billingAddress.isEmpty()) {
            isBilling = true;
            binding.billingTxt.setText(billingAddress);
            binding.actionAdd1.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_edit_black_24dp));
        } else {
            binding.billingTxt.setVisibility(View.GONE);
            binding.actionAdd1.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_add_black_24dp));
        }


        String shippingAddress = "";
        billing = prefManager.getUser().getShippingAddress();
        Log.e("getShippingAddress", gson.toJson(billing));
        if (!billing.getAddress1().isEmpty()) {
            shippingAddress = shippingAddress + billing.getAddress1();
        }
        if (!billing.getAddress2().isEmpty()) {
            shippingAddress = shippingAddress + ", " + billing.getAddress2();
        }

        if (!billing.getCity().isEmpty()) {
            shippingAddress = shippingAddress + ", " + billing.getCity();
        }
        if (!billing.getState().isEmpty()) {
            shippingAddress = shippingAddress + ", " + billing.getState();
        }
        if (!billing.getCountry().isEmpty()) {
            shippingAddress = shippingAddress + ", " + billing.getCountry();
        }
        if (!billing.getPostCode().isEmpty()) {
            shippingAddress = shippingAddress + " - " + billing.getPostCode();
        }

        if (!shippingAddress.isEmpty()) {
            isShipping = true;
            binding.shippingTxt.setVisibility(View.VISIBLE);
            binding.shippingTxt.setText(shippingAddress);
            binding.actionAdd2.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_edit_black_24dp));
        } else {
            binding.shippingTxt.setVisibility(View.GONE);
            binding.actionAdd2.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_add_black_24dp));
        }


    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null && data.hasExtra("success")) {
                            startShimmer();
                            loadProfile();
                        }

                    }
                }
            });

    public void changeTheme() {
        binding.actionAdd1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.actionAdd2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));

        binding.actionAdd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CONSTANT.API_MODE) {
                    Intent intent = new Intent(act, BillingAddressActivity.class);
                    intent.putExtra("isBilling", isBilling);
                    someActivityResultLauncher.launch(intent);
                } else {
                    HELPER.SIMPLE_ROUTE(act, BillingAddressActivity.class);
                }

            }
        });
        binding.actionAdd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CONSTANT.API_MODE) {
                    Intent intent = new Intent(act, BillingAddressActivity.class);
                    intent.putExtra("isShipping", isShipping);
                    someActivityResultLauncher.launch(intent);
                } else {
                    HELPER.SIMPLE_ROUTE(act, BillingAddressActivity.class);
                }
            }
        });
    }

    private boolean isLoading = false;

    private void loadProfile() {

        if (!isLoading)
            isLoading = true;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoading = false;
                stopShimmer();

                JSONObject object = ResponseHandler.createJsonObject(response);
                if (object != null) {
                    ProfileModel model = new ProfileModel();
                    model.setUserId(ResponseHandler.getString(object, "id"));
                    model.setDisplayName(prefManager.getUser().getDisplayName());
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

                    Billing shippingAddress = new Billing();
                    shippingAddress.setFirstName(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "first_name"));
                    shippingAddress.setLastName(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "last_name"));
                    shippingAddress.setCompany(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "company"));
                    shippingAddress.setAddress1(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "address_1"));
                    shippingAddress.setAddress2(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "address_2"));
                    shippingAddress.setCity(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "city"));
                    shippingAddress.setPostCode(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "postcode"));
                    shippingAddress.setCountry(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "country"));
                    shippingAddress.setState(ResponseHandler.getString(ResponseHandler.getJSONObject(object, "shipping"), "state"));
                    model.setShippingAddress(shippingAddress);

                    prefManager.setUser(model);
                    loadData();

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