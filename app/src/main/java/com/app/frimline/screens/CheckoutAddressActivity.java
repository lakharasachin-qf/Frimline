package com.app.frimline.screens;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.Common.ResponseHandler;
import com.app.frimline.R;
import com.app.frimline.databinding.ActivityCheckoutAddressBinding;
import com.app.frimline.fragments.ChooseListBottomFragment;
import com.app.frimline.intefaces.OnItemSelectListener;
import com.app.frimline.models.Billing;
import com.app.frimline.models.CountryModel;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckoutAddressActivity extends BaseActivity implements OnItemSelectListener {
    //Show Fragment For BrandList
    ChooseListBottomFragment bottomSheetFragment;
    JSONObject orderParam;
    private ActivityCheckoutAddressBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_checkout_address);
        makeStatusBarSemiTranspenret(binding.toolbarNavigation.toolbar);
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
                    // shipToDifferentAddress(true);
                } else {
                    // shipToDifferentAddress(false);
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
                    Intent i = new Intent(act, CategoryLandingActivity.class);
                    i.putExtra("targetCategory", "Yes");
                    i.putExtra("fragment", "Home");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {
//                    if (CONSTANT.API_MODE) {
//                        if (!validations()) {
//
//                            Intent i = new Intent(act, PaymentActivity.class);
//                            i.putExtra("orderParam", getShippingAddress().toString());
//                            startActivity(i);
//                        }
//                    } else {
                    if (!validations()) {
                        Intent i = new Intent(act, CategoryLandingActivity.class);
                        i.putExtra("targetCategory", "Yes");
                        i.putExtra("fragment", "Home");
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                    // }
                }
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        changeTheme();
        //setSavedCheckoutAddress();

    }

    public void setSavedCheckoutAddress() {
        Billing shipping = prefManager.getUser().getShippingAddress();
        if (shipping != null) {
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

    public void shipToDifferentAddress(boolean shipToDifferent) {
        if (shipToDifferent) {
//            binding.nameEdt.setText("");
//            binding.lnameEdt.setText("");
//            binding.companyEdt.setText("");
//            binding.countryEdt.setText("");
//            binding.streetEdt.setText("");
//            binding.streetEdt2.setText("");
//            binding.cityEdt.setText("");
//            binding.postalCodeEdt.setText("");
//            binding.stateEdt.setText("");
        } else {
            if (getIntent().hasExtra("orderParam")) {
                orderParam = ResponseHandler.createJsonObject(getIntent().getStringExtra("orderParam"));
                binding.nameEdt.setText(ResponseHandler.getString(orderParam, "first_name"));
                binding.lnameEdt.setText(ResponseHandler.getString(orderParam, "last_name"));
                binding.companyEdt.setText(ResponseHandler.getString(orderParam, "company"));
                binding.countryEdt.setText(ResponseHandler.getString(orderParam, "country"));
                binding.streetEdt.setText(ResponseHandler.getString(orderParam, "address_1"));
                binding.streetEdt2.setText(ResponseHandler.getString(orderParam, "address_2"));
                binding.cityEdt.setText(ResponseHandler.getString(orderParam, "city"));
                binding.postalCodeEdt.setText(ResponseHandler.getString(orderParam, "postcode"));
                binding.stateEdt.setText(ResponseHandler.getString(orderParam, "state"));
            } else {
                orderParam = new JSONObject();
                Billing model = prefManager.getUser().getBillingAddress();
                if (model != null) {
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
            }


        }

    }

    public JSONObject getShippingAddress() {
        JSONObject shipping = new JSONObject();
        try {
            shipping.put("first_name", binding.nameEdt.getText().toString());
            shipping.put("last_name", binding.lnameEdt.getText().toString());
            shipping.put("address_1", binding.streetEdt.getText().toString());
            shipping.put("address_2", binding.streetEdt2.getText().toString());
            shipping.put("city", binding.cityEdt.getText().toString());
            shipping.put("state", binding.stateEdt.getText().toString());
            shipping.put("postcode", binding.postalCodeEdt.getText().toString());
            shipping.put("country", binding.countryEdt.getText().toString());
            orderParam.put("shipping", shipping);


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
                showFragmentList();
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
            if (binding.postalCodeEdt.getText().toString().trim().length() < 6) {
                isError = true;
                binding.postalCodeEdtLayout.setError("Enter 6 digit postcode");
                if (!isFocus) {
                    isFocus = true;
                    binding.postalCodeEdt.requestFocus();
                }
            }
        }

        return isError;
    }

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
}