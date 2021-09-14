package com.app.frimline.screens;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Toast;

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
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.MySingleton;
import com.app.frimline.Common.PREF;
import com.app.frimline.Common.ResponseHandler;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivityCheckoutAddressBinding;
import com.app.frimline.databinding.DialogDiscardImageBinding;
import com.app.frimline.fragments.ChooseListBottomFragment;
import com.app.frimline.intefaces.OnItemSelectListener;
import com.app.frimline.models.Billing;
import com.app.frimline.models.CountryModel;
import com.app.frimline.models.StateModel;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckoutAddressActivity extends BaseActivity implements OnItemSelectListener {
    //Show Fragment For BrandList
    ChooseListBottomFragment bottomSheetFragment;
    JSONObject orderParam;
    boolean isloadedShippingAddressBeforAnyChange = false;  // jo initial address show krave baki user e change kryo hoy to every time initial address show nthi kravanu
    DialogDiscardImageBinding discardImageBinding;
    private ActivityCheckoutAddressBinding binding;
    private ArrayList<StateModel> stateModelArrayList = new ArrayList<>();
    private boolean isLoading = false;
    private ArrayList<CountryModel> countryList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_checkout_address);
        makeStatusBarSemiTranspenret(binding.toolbarNavigation.toolbar);
        TypeToken<ArrayList<CountryModel>> typeToken = new TypeToken<ArrayList<CountryModel>>() {
        };
        countryList = gson.fromJson(getIntent().getStringExtra("countryList"), typeToken.getType());
        if (countryList == null) {
            getCountryState();
        }
        binding.toolbarNavigation.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.ON_BACK_PRESS_ANIM(act);
            }
        });

        if (getIntent().hasExtra("orderParam"))
            orderParam = ResponseHandler.createJsonObject(getIntent().getStringExtra("orderParam"));
        else {
            orderParam = new JSONObject();

            Billing model = prefManager.getUser().getBillingAddress();
            try {
                JSONObject billing = new JSONObject();
                billing.put("first_name", model.getFirstName());
                billing.put("last_name", model.getLastName());
                billing.put("company", model.getCompany());
                billing.put("country", model.getCountry());
                billing.put("address_1", model.getAddress1());
                billing.put("address_2", model.getAddress2());
                billing.put("city", model.getCity());
                billing.put("postcode", model.getPostCode());
                billing.put("state", model.getState());
                billing.put("phone", model.getPhone());
                billing.put("email", model.getEmail());
                orderParam.put("billing", billing);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        binding.toolbarNavigation.title.setText("Checkout");
        binding.shipToDiffCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.otherInfo.setVisibility(View.VISIBLE);
                    if (CONSTANT.API_MODE) {
                        shipToDifferentAddress(true);
                    }
                    // shipToDifferentAddress(true);
                } else {
                    // shipToDifferentAddress(false);
                    if (CONSTANT.API_MODE) {
                        shipToDifferentAddress(false);
                    }
                    binding.otherInfo.setVisibility(View.GONE);
                }
            }
        });
        ((ViewGroup) findViewById(R.id.containerLinear)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        ((ViewGroup) findViewById(R.id.otherInfo)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        binding.includeBtn.button.setText("Proceed");
        binding.includeBtn.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PROTOTYPE_MODE) {
                    Intent i = new Intent(act, CategoryRootActivity.class);
                    i.putExtra("targetCategory", "Yes");
                    i.putExtra("fragment", "Home");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {
                    if (CONSTANT.API_MODE) {
                        if (binding.shipToDiffCheck.isChecked()) {
                            if (!validations()) {
                                verifyPostcode(binding.postalCodeEdt.getText().toString());

                            }
                        } else {
                            Intent i = new Intent(act, PaymentActivity.class);
                            i.putExtra("promoCode", getIntent().getStringExtra("promoCode"));
                            i.putExtra("orderParam", getShippingAddress().toString());
                            startActivity(i);
                        }
                    } else {
                        if (!validations()) {
                            Intent i = new Intent(act, CategoryRootActivity.class);
                            i.putExtra("targetCategory", "Yes");
                            i.putExtra("fragment", "Home");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                    }
                }
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        changeTheme();
        //setSavedCheckoutAddress();


    }


    /*  billing.put("first_name", binding.nameEdt.getText().toString());
            billing.put("last_name", binding.lnameEdt.getText().toString());
            billing.put("address_1", binding.streetEdt.getText().toString());
            billing.put("address_2", binding.streetEdt2.getText().toString());
            billing.put("city", binding.cityEdt.getText().toString());
            billing.put("state", binding.stateEdt.getText().toString());
            billing.put("postcode", binding.postalCodeEdt.getText().toString());
            billing.put("company", binding.companyEdt.getText().toString());
            billing.put("country", binding.countryEdt.getText().toString());
            billing.put("email", binding.emailEdt.getText().toString());
            billing.put("phone", binding.phoneNoEdt.getText().toString());*/

    public boolean checkIsShippingAddressEmpty() {
        int count = 0;
        Billing shipping = prefManager.getUser().getShippingAddress();
        if (shipping.getFirstName().isEmpty()) {
            count++;
        }

        if (shipping.getLastName().isEmpty()) {
            count++;
        }

        if (shipping.getCountry().isEmpty()) {
            count++;
        }

        if (shipping.getAddress1().isEmpty()) {
            count++;
        }
        return count > 2;
    }

    public void setSavedCheckoutAddress() {
        Billing shipping = prefManager.getUser().getShippingAddress();
        if (shipping != null && !checkIsShippingAddressEmpty()) {
            binding.nameEdt.setText(shipping.getFirstName());
            binding.lnameEdt.setText(shipping.getLastName());
            binding.companyEdt.setText(shipping.getCompany());
            binding.countryEdt.setText(shipping.getCountry());
            binding.streetEdt.setText(shipping.getAddress1());
            binding.streetEdt2.setText(shipping.getAddress2());
            binding.cityEdt.setText(shipping.getCity());
            binding.postalCodeEdt.setText(shipping.getPostCode());
            binding.stateEdt.setText(shipping.getState());
        } else {
            if (getIntent().hasExtra("orderParam")) {
                orderParam = ResponseHandler.createJsonObject(getIntent().getStringExtra("orderParam"));
                binding.nameEdt.setText(ResponseHandler.getString(ResponseHandler.getJSONObject(orderParam, "billing"), "first_name"));
                binding.lnameEdt.setText(ResponseHandler.getString(ResponseHandler.getJSONObject(orderParam, "billing"), "last_name"));
                binding.companyEdt.setText(ResponseHandler.getString(ResponseHandler.getJSONObject(orderParam, "billing"), "company"));
                binding.countryEdt.setText(ResponseHandler.getString(ResponseHandler.getJSONObject(orderParam, "billing"), "country"));
                binding.streetEdt.setText(ResponseHandler.getString(ResponseHandler.getJSONObject(orderParam, "billing"), "address_1"));
                binding.streetEdt2.setText(ResponseHandler.getString(ResponseHandler.getJSONObject(orderParam, "billing"), "address_2"));
                binding.cityEdt.setText(ResponseHandler.getString(ResponseHandler.getJSONObject(orderParam, "billing"), "city"));
                binding.postalCodeEdt.setText(ResponseHandler.getString(ResponseHandler.getJSONObject(orderParam, "billing"), "postcode"));
                binding.stateEdt.setText(ResponseHandler.getString(ResponseHandler.getJSONObject(orderParam, "billing"), "state"));
            }
        }
    }

    public void shipToDifferentAddress(boolean shipToDifferent) {
        if (shipToDifferent) {
            Billing shipping = prefManager.getUser().getShippingAddress();
            if (shipping != null && !isloadedShippingAddressBeforAnyChange && !checkIsShippingAddressEmpty()) {
                isloadedShippingAddressBeforAnyChange = true;
                binding.nameEdt.setText(shipping.getFirstName());
                binding.lnameEdt.setText(shipping.getLastName());
                binding.companyEdt.setText(shipping.getCompany());
                binding.countryEdt.setText(shipping.getCountry());
                binding.streetEdt.setText(shipping.getAddress1());
                binding.streetEdt2.setText(shipping.getAddress2());
                binding.cityEdt.setText(shipping.getCity());
                binding.postalCodeEdt.setText(shipping.getPostCode());
                binding.stateEdt.setText(shipping.getState());
            }
        }
    }

    public JSONObject getShippingAddress() {
        JSONObject shipping = new JSONObject();
        try {
            if (binding.shipToDiffCheck.isChecked()) {
                shipping.put("first_name", binding.nameEdt.getText().toString());
                shipping.put("last_name", binding.lnameEdt.getText().toString());
                shipping.put("address_1", binding.streetEdt.getText().toString());
                shipping.put("address_2", binding.streetEdt2.getText().toString());
                shipping.put("city", binding.cityEdt.getText().toString());
                shipping.put("state", binding.stateEdt.getText().toString());
                shipping.put("postcode", binding.postalCodeEdt.getText().toString());
                shipping.put("country", binding.countryEdt.getText().toString());
                orderParam.put("shipping", shipping);
            }else {
                shipping.put("first_name", binding.nameEdt.getText().toString());
                shipping.put("last_name", binding.lnameEdt.getText().toString());
                shipping.put("address_1", binding.streetEdt.getText().toString());
                shipping.put("address_2", binding.streetEdt2.getText().toString());
                shipping.put("city", binding.cityEdt.getText().toString());
                shipping.put("state", binding.stateEdt.getText().toString());
                shipping.put("postcode", binding.postalCodeEdt.getText().toString());
                shipping.put("country", binding.countryEdt.getText().toString());
                orderParam.put("shipping", orderParam.getJSONObject("billing"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return orderParam;
    }

    public void changeTheme() {
        binding.otherInfo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    binding.otherNoteEdtLayout.setBackground(ContextCompat.getDrawable(act, R.drawable.shape_editext_background_active));
                    GradientDrawable drawable = (GradientDrawable) binding.otherNoteEdtLayout.getBackground();
                    drawable.setStroke(2, Color.parseColor(new PREF(act).getThemeColor()));
                } else {
                    binding.otherNoteEdtLayout.setBackground(ContextCompat.getDrawable(act, R.drawable.shape_editext_background));
                }
            }
        });
        binding.shipToDiffCheck.setButtonTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.includeBtn.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));

        HELPER.ERROR_HELPER(binding.nameEdt, binding.nameEdtLayout);
        HELPER.ERROR_HELPER(binding.lnameEdt, binding.lnameEdtLayout);
        HELPER.ERROR_HELPER(binding.companyEdt, binding.companyEdtLayout);
        HELPER.ERROR_HELPER(binding.countryEdt, binding.countryEdtLayout);
        HELPER.ERROR_HELPER(binding.streetEdt, binding.streetEdtLayout);
        HELPER.ERROR_HELPER(binding.streetEdt2, binding.streetEdtLayout2);
        HELPER.ERROR_HELPER(binding.cityEdt, binding.cityEdtLayout);
        HELPER.ERROR_HELPER(binding.postalCodeEdt, binding.postalCodeEdtLayout);

        HELPER.FOCUS_HELPER(binding.scrollView, binding.nameEdt, binding.nameEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView, binding.lnameEdt, binding.lnameEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView, binding.companyEdt, binding.companyEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView, binding.countryEdt, binding.countryEdtLayout);

        HELPER.FOCUS_HELPER(binding.scrollView, binding.streetEdt, binding.streetEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView, binding.streetEdt2, binding.streetEdtLayout2);
        HELPER.FOCUS_HELPER(binding.scrollView, binding.cityEdt, binding.cityEdtLayout);
        HELPER.FOCUS_HELPER(binding.scrollView, binding.postalCodeEdt, binding.postalCodeEdtLayout);


        binding.nameEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.lnameEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.companyEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.countryEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.streetEdtLayout2.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.streetEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.cityEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        binding.postalCodeEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));

        binding.countryEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentList(CONSTANT.COUNTRY, "Country");
            }
        });
    }

    public boolean validations() {

        boolean isError = false;
        boolean isFocus = false;

        if (binding.otherInfo.getVisibility() == View.VISIBLE) {
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


            if (binding.countryEdt.getText().toString().trim().length() == 0) {
                isError = true;
                binding.countryEdtLayout.setError("Select Country");
                if (!isFocus) {
                    isFocus = true;
                    binding.countryEdt.requestFocus();
                }
            }
            if (binding.streetEdt.getText().toString().trim().length() == 0) {
                isError = true;
                binding.streetEdtLayout.setError("Enter Street");
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
            if (binding.postalCodeEdt.getText().toString().trim().length() == 0) {
                isError = true;
                binding.postalCodeEdtLayout.setError("Enter postcode.");
                if (!isFocus) {
                    isFocus = true;
                    binding.postalCodeEdt.requestFocus();
                }
            }

        }

        return isError;
    }

    public void showFragmentList(int flag, String title) {
        HELPER.closeKeyboard(binding.countryEdt, act);
        bottomSheetFragment = new ChooseListBottomFragment(title, countryList, flag, stateModelArrayList);

        if (bottomSheetFragment.isVisible()) {
            bottomSheetFragment.dismiss();
        }
        if (bottomSheetFragment.isAdded()) {
            bottomSheetFragment.dismiss();
        }

        if (!bottomSheetFragment.isVisible()) {
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        }
    }

    @Override
    public void onItemSelect(CountryModel model, int postion, int flag) {
        if (flag == CONSTANT.COUNTRY) {
            binding.countryEdt.setText(model.getName());
            if (model.getModels() != null && model.getModels().size() != 0) {
                stateModelArrayList = model.getModels();
                binding.stateEdt.setClickable(true);
                binding.stateEdt.setFocusable(false);
                binding.stateEdt.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_dropdown, 0);
                binding.stateEdt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFragmentList(CONSTANT.STATE, "State");
                    }
                });
            } else {
                binding.stateEdt.setClickable(false);
                binding.stateEdt.setFocusable(true);
                binding.stateEdt.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                binding.stateEdt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }
        bottomSheetFragment.dismiss();
    }

    @Override
    public void onItemSelect(StateModel model, int postion, int flag) {
        if (flag == CONSTANT.STATE) {
            binding.stateEdt.setText(model.getStateName());
        }
        bottomSheetFragment.dismiss();
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
            }
        });
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    private void verifyPostcode(String postCode) {

        if (!isLoading)
            isLoading = true;
        HELPER.showLoadingTran(act);

        //HELPER.showLoadingTran(act);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.VERIFY_POSTCODE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                HELPER.dismissLoadingTran();
                JSONObject object = ResponseHandler.createJsonObject(response);
                if (object != null) {
                    if (ResponseHandler.getString(object, "status").equals("1")) {

                        Intent i = new Intent(act, PaymentActivity.class);
                        i.putExtra("promoCode", getIntent().getStringExtra("promoCode"));
                        i.putExtra("orderParam", getShippingAddress().toString());
                        startActivity(i);

                    } else {
                        infoAlert("Error", ResponseHandler.getString(object, "msg"));
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
                        if (response!=null && response.statusCode == 400) {

                            try {
                                String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                                JSONObject jsonObject = new JSONObject(jsonString);
                                Log.e("jsosnErir", jsonString);
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
                params.put("shipping_postcode", postCode);

                return params;
            }
        };
        MySingleton.getInstance(act).addToRequestQueue(stringRequest);
    }

    private void getCountryState() {

        if (!isLoading)
            isLoading = true;
        HELPER.showLoadingTran(act);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.GET_COUNTRY_STATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                HELPER.dismissLoadingTran();
                countryList = ResponseHandler.parseCountryState(response);
                Log.e("Response", String.valueOf(countryList.size()));

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        HELPER.dismissLoadingTran();
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