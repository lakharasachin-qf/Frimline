package com.app.frimline.screens;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.MySingleton;
import com.app.frimline.Common.PREF;
import com.app.frimline.Common.ResponseHandler;
import com.app.frimline.Common.Validators;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivityBillingAddressBinding;
import com.app.frimline.databinding.DialogDiscardImageBinding;
import com.app.frimline.fragments.ChooseListBottomFragment;
import com.app.frimline.intefaces.OnItemSelectListener;
import com.app.frimline.models.Billing;
import com.app.frimline.models.CountryModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class BillingAddressActivity extends BaseActivity implements OnItemSelectListener {
    private ActivityBillingAddressBinding binding;
    //SachinAbc@123456789

    private int flag = -1;  // 0 = for billings add or edit ,  1 = for shipping add or edit
    private boolean isEdit = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_billing_address);
        makeStatusBarSemiTranspenret(binding.toolbarNavigation.toolbar);
        binding.toolbarNavigation.title.setText("Checkout");

        if (getIntent().hasExtra("isBilling")) {
            flag = 0;
            binding.toolbarNavigation.title.setText("Add Billing Address");
            if (getIntent().getBooleanExtra("isBilling", false)) {
                isEdit = true; //comes for edit billing address
                binding.toolbarNavigation.title.setText("Edit Billing Address");
                Billing model = prefManager.getUser().getBillingAddress();
                binding.nameEdt.setText(model.getFirstName());
                binding.lnameEdt.setText(model.getLastName());
                binding.companyEdt.setText(model.getCompany());
                binding.countryEdt.setText(model.getCountry());
                binding.streetEdt.setText(model.getAddress1());
                binding.streetEdt2.setText(model.getAddress2());
                binding.cityEdt.setText(model.getCity());
                binding.postalCodeEdt.setText(model.getPostCode());
                binding.stateEdt.setText(model.getState());
                binding.phoneNoEdt.setText(model.getPhone());
                binding.emailEdt.setText(model.getEmail());
            }
            binding.billingSectionOther.setVisibility(View.VISIBLE);
        }
        if (getIntent().hasExtra("isShipping")) {
            flag = 1;
            binding.toolbarNavigation.title.setText("Add Shipping Address");
            if (getIntent().getBooleanExtra("isShipping", false)) {
                isEdit = true; //comes for edit billing address
                binding.toolbarNavigation.title.setText("Edit Shipping Address");
                Billing model = prefManager.getUser().getShippingAddress();
                binding.nameEdt.setText(model.getFirstName());
                binding.lnameEdt.setText(model.getLastName());
                binding.companyEdt.setText(model.getCompany());
                binding.countryEdt.setText(model.getCountry());
                binding.streetEdt.setText(model.getAddress1());
                binding.streetEdt2.setText(model.getAddress2());
                binding.cityEdt.setText(model.getCity());
                binding.postalCodeEdt.setText(model.getPostCode());
                binding.stateEdt.setText(model.getState());


            }
            binding.billingSectionOther.setVisibility(View.GONE);
        }

        binding.toolbarNavigation.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.ON_BACK_PRESS_ANIM(act);
            }
        });
        binding.includeBtn.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PROTOTYPE_MODE) {
                    HELPER.SIMPLE_ROUTE(act, CheckoutAddressActivity.class);
                } else {
                    if (!validations()) {
                        if (flag == 0) {
                            //edit or add billing address
                            update(APIs.UPDATE_BILLING_ADDRESS);
                        } else if (flag == 1) {
                            //edit or add shipping address
                            update(APIs.UPDATE_SHIPPING_ADDRESS);
                        } else
                            HELPER.SIMPLE_ROUTE(act, CheckoutAddressActivity.class);
                    }
                }

            }
        });
        binding.includeBtn.button.setText("Next");
        ((ViewGroup) findViewById(R.id.containerLinear)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        changeTheme();
    }

    private void changeTheme() {
        PREF pref = new PREF(act);
        binding.includeBtn.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        HELPER.ERROR_HELPER(binding.nameEdt, binding.nameEdtLayout);
        HELPER.ERROR_HELPER(binding.lnameEdt, binding.lnameEdtLayout);
        HELPER.ERROR_HELPER(binding.companyEdt, binding.companyEdtLayout);
        HELPER.ERROR_HELPER(binding.countryEdt, binding.countryEdtLayout);
        HELPER.ERROR_HELPER(binding.streetEdt, binding.streetEdtLayout);
        HELPER.ERROR_HELPER(binding.streetEdt2, binding.streetEdtLayout2);
        HELPER.ERROR_HELPER(binding.cityEdt, binding.cityEdtLayout);
        HELPER.ERROR_HELPER(binding.stateEdt, binding.stateEdtLayout);
        HELPER.ERROR_HELPER(binding.postalCodeEdt, binding.postalCodeEdtLayout);
        HELPER.ERROR_HELPER_For_MOBILE_VALIDATION(binding.phoneNoEdt, binding.phoneNoEdtLayout);
        HELPER.ERROR_HELPER(binding.emailEdt, binding.emailEdtLayout);


        HELPER.FOCUS_HELPER(binding.scrollView, binding.nameEdt, binding.nameEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView, binding.lnameEdt, binding.lnameEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView, binding.companyEdt, binding.companyEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView, binding.countryEdt, binding.countryEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView, binding.streetEdt, binding.streetEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView, binding.streetEdt2, binding.streetEdtLayout2);
        HELPER.FOCUS_HELPER(binding.scrollView, binding.cityEdt, binding.cityEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView, binding.stateEdt, binding.stateEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView, binding.postalCodeEdt, binding.postalCodeEdtLayout);
        //HELPER.FOCUS_HELPER(binding.scrollView, binding.phoneNoEdt, binding.phoneNoEdtLayout);
        // HELPER.FOCUS_HELPER(binding.scrollView, binding.emailEdt, binding.emailEdtLayout);


        binding.nameEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.lnameEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.companyEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.countryEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.streetEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.streetEdtLayout2.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.cityEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.stateEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.postalCodeEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.phoneNoEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.emailEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));


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

//        if (binding.companyEdt.getText().toString().trim().length() == 0) {
//            isError = true;
//            binding.companyEdtLayout.setError("Enter Company Name");
//            if (!isFocus) {
//                isFocus = true;
//                binding.companyEdt.requestFocus();
//            }
//        }

        if (binding.countryEdt.getText().toString().trim().length() == 0) {
            isError = true;
            binding.countryEdtLayout.setError("Enter Select");
            if (!isFocus) {
                isFocus = true;
                binding.countryEdt.requestFocus();
            }
        }
        if (binding.streetEdt.getText().toString().trim().length() == 0) {
            isError = true;
            binding.streetEdtLayout.setError("Enter Street 1");
            if (!isFocus) {
                isFocus = true;
                binding.streetEdt.requestFocus();
            }
        }
        if (binding.streetEdt2.getText().toString().trim().length() == 0) {
            isError = true;
            binding.streetEdtLayout2.setError("Enter Street 2");
            if (!isFocus) {
                isFocus = true;
                binding.streetEdt2.requestFocus();
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
        if (binding.stateEdt.getText().toString().trim().length() == 0) {
            isError = true;
            binding.stateEdtLayout.setError("Enter state");
            if (!isFocus) {
                isFocus = true;
                binding.stateEdt.requestFocus();
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
        if (flag == 0) {
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

    //Show Fragment For BrandList
    ChooseListBottomFragment bottomSheetFragment;

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


    private boolean isLoading = false;

    private void update(String url) {

        if (!isLoading)
            isLoading = true;
        HELPER.showLoadingTran(act);


        //HELPER.showLoadingTran(act);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);

                HELPER.dismissLoadingTran();
// {"code":200,"message":"Billing Info Update Successfully","data":{"status":200}}
                JSONObject object = ResponseHandler.createJsonObject(response);
                if (object != null) {
                    if (ResponseHandler.getString(object, "code").equals("200")) {
                        infoAlert("Success", ResponseHandler.getString(object, "message"));
                    }
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        HELPER.dismissLoadingTran();
                        isLoading = false;
                        NetworkResponse response = error.networkResponse;
                        if (response.statusCode == 400) {
                            try {
                                String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                                JSONObject jsonObject = new JSONObject(jsonString);
                                Log.e("jsosnErir", jsonString.toString());
                                infoAlert("Error", ResponseHandler.getString(jsonObject, "message"));
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
                Map<String, String> params = getHeader();
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("first_name", binding.nameEdt.getText().toString());
                params.put("last_name", binding.lnameEdt.getText().toString());
                params.put("company", binding.companyEdt.getText().toString());
                params.put("address_1", binding.streetEdt.getText().toString());
                params.put("address_2", binding.streetEdt2.getText().toString());
                params.put("city", binding.cityEdt.getText().toString());
                params.put("postcode", binding.postalCodeEdt.getText().toString());
                params.put("state", binding.stateEdt.getText().toString());
                params.put("country", binding.countryEdt.getText().toString());

                if (flag == 0) {
                    params.put("email", binding.emailEdt.getText().toString());
                    params.put("phone", binding.phoneNoEdt.getText().toString());
                }
                return params;
            }
        };
        MySingleton.getInstance(act).addToRequestQueue(stringRequest);
    }

    DialogDiscardImageBinding discardImageBinding;

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