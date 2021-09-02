package com.app.frimline.screens;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.app.frimline.BaseActivity;
import com.app.frimline.Common.APIs;
import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.MySingleton;
import com.app.frimline.Common.PREF;
import com.app.frimline.Common.ResponseHandler;
import com.app.frimline.R;
import com.app.frimline.adapters.MyCartAdapter;
import com.app.frimline.databinding.ActivityPaymentBinding;
import com.app.frimline.models.HomeFragements.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends BaseActivity {
    MyCartAdapter cartAdapter;
    ArrayList<ProductModel> cartItemList;
    private ActivityPaymentBinding binding;
    private boolean isLoading = false;

    private JSONObject orderParam;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_payment);
        makeStatusBarSemiTranspenret(binding.toolbarNavigation.toolbar);

        binding.boottomFooter.setOnClickListener(v -> {
            if (prefManager.isLogin()) {
                HELPER.SIMPLE_ROUTE(act, BillingAddressActivity.class);
            } else {
                HELPER.SIMPLE_ROUTE(act, LoginActivity.class);
            }
        });
        binding.toolbarNavigation.backPress.setOnClickListener(v -> HELPER.ON_BACK_PRESS_ANIM(act));
        init();
        binding.toolbarNavigation.title.setText("Checkout");
        changeColor();
        orderParam = ResponseHandler.createJsonObject(getIntent().getStringExtra("orderParam"));

        if (CONSTANT.API_MODE) {
            loadData();
        } else {
            setAdapter();
        }
        if (getIntent().hasExtra("orderParam")) {
            Log.e("orderParam", getIntent().getStringExtra("orderParam"));
        }


        binding.paymentModeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == binding.payCodRadioBtn.getId()) {
                if (codChargesAmount.isEmpty()) {
                    getCODCharges();
                } else {
                    isCodAvailable = true;
                    calculateCart();
                }
            } else {
                isCodAvailable = false;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        prefManager = new PREF(act);
    }

    public void loadData() {
        int count = db.productEntityDao().getAll().size();
        if (count != 0) {
            cartItemList = HELPER.getCartList(db.productEntityDao().getAll());
            cartAdapter = new MyCartAdapter(cartItemList, act, "payment");
            binding.cartRecycler.setLayoutManager(new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false));
            binding.cartRecycler.setNestedScrollingEnabled(false);
            binding.cartRecycler.setAdapter(cartAdapter);
            try {
                JSONArray productArray = new JSONArray();
                for (int i = 0; i < cartItemList.size(); i++) {
                    JSONObject object = new JSONObject();
                    object.put("product_id", cartItemList.get(i).getId());
                    object.put("quantity", cartItemList.get(i).getQty());
                    productArray.put(object);
                }
                orderParam.put("line_items",productArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            createOrder();
            setState();
        } else {
            setNoDataFound();
        }
    }

    @SuppressLint("SetTextI18n")
    public void setState() {
        if (cartItemList.size() == 1)
            binding.itemCount.setText(cartItemList.size() + " Item");
        else
            binding.itemCount.setText(cartItemList.size() + " Items");

    }

    public void setAdapter() {

        ArrayList<ProductModel> arrayList = new ArrayList<>();
        ProductModel homeModel = new ProductModel();
        homeModel.setName("0");
        arrayList.add(homeModel);
        homeModel = new ProductModel();
        homeModel.setName("1");
        arrayList.add(homeModel);
        homeModel = new ProductModel();
        homeModel.setName("2");
        arrayList.add(homeModel);

        homeModel = new ProductModel();
        homeModel.setName("3");
        arrayList.add(homeModel);


        MyCartAdapter shopFilterAdapter = new MyCartAdapter(arrayList, act, "payment");
        shopFilterAdapter.setActionsListener(new MyCartAdapter.setActionsListener() {
            @Override
            public void onDeleteAction(int position, ProductModel model) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (model.getName().equalsIgnoreCase(arrayList.get(i).getName())) {
                        arrayList.remove(i);
                        shopFilterAdapter.notifyItemRemoved(position);
                        shopFilterAdapter.notifyItemRangeChanged(position, arrayList.size());
                        if (arrayList.size() == 0) {
                            setNoDataFound();
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCartUpdate(int position, ProductModel model) {

            }
        });

        binding.cartRecycler.setLayoutManager(new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false));
        binding.cartRecycler.setNestedScrollingEnabled(false);
        binding.cartRecycler.setAdapter(shopFilterAdapter);


    }

    public void setNoDataFound() {
        binding.boottomFooter.setVisibility(View.GONE);
        binding.scrollView.setVisibility(View.GONE);
        binding.NoDataFound.setVisibility(View.VISIBLE);

    }

    public void changeColor() {
        binding.boottomText.setTextColor((Color.parseColor(new PREF(act).getThemeColor())));
        binding.payCodRadioBtn.setButtonTintList(ColorStateList.valueOf((Color.parseColor(new PREF(act).getThemeColor()))));
        binding.payOnlineRadioBtn.setButtonTintList(ColorStateList.valueOf((Color.parseColor(new PREF(act).getThemeColor()))));
        binding.acceptTerms.setButtonTintList(ColorStateList.valueOf((Color.parseColor(new PREF(act).getThemeColor()))));
        HELPER.backgroundTint(act, binding.bottomSlider, true);
        HELPER.backgroundTint(act, binding.scrollView, true);

        binding.acceptTermsLayout.setOnClickListener(v -> binding.acceptTerms.setChecked(!binding.acceptTerms.isChecked()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.acceptTermsLabel.setText(Html.fromHtml("I have read and agree to the <a href='' style='color:" + prefManager.getThemeColor() + "; text-decoration: none'><b>website terms and conditions</b></a>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.acceptTermsLabel.setText(Html.fromHtml("I have read and agree to the <a href=''><font color='" + prefManager.getThemeColor() + "'<b>website terms and conditions</b></font></a>"));
        }
    }


    private void createOrder() {
        if (!isLoading)
            isLoading = true;
        HELPER.showLoadingTran(act);
        Log.e("orderPosting",orderParam.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, APIs.CREATE_ORDER, orderParam, response -> {
            HELPER.dismissLoadingTran();
            if (response != null) {
                isCodAvailable = true;
                calculateCart();
            }
        }, error -> {
            error.printStackTrace();
            HELPER.dismissLoadingTran();
            isLoading = false;
            NetworkResponse response = error.networkResponse;
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return getHeaders();
            }
        };
        MySingleton.getInstance(act).addToRequestQueue(request);
    }

    private void getCODCharges() {
        if (!isLoading)
            isLoading = true;
        HELPER.showLoadingTran(act);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.COD_CHARGES, response -> {
            HELPER.dismissLoadingTran();
            JSONObject object = ResponseHandler.createJsonObject(response);
            if (object != null) {
                isCodAvailable = true;
                calculateCart();
            }

        },
                error -> {
                    error.printStackTrace();
                    HELPER.dismissLoadingTran();
                    isLoading = false;
                    NetworkResponse response = error.networkResponse;
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() {
                return getHeader();
            }

            @Override
            protected Map<String, String> getParams() {
                return new HashMap<>();
            }
        };

        MySingleton.getInstance(act).addToRequestQueue(stringRequest);
    }

    boolean isCodAvailable = false;
    boolean isShippingChargeAvailable = false;
    JSONObject promoCodeObject;
    String codChargesAmount = "10";
    String shippingChargesAmount = "10";

    public void init() {
        try {
            promoCodeObject = new JSONObject(getIntent().getStringExtra("promoCode"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        calculateCart();
    }

    @SuppressLint("SetTextI18n")
    public void calculateCart() {

        double finalAmount;

        double totalPrice = 0; // sum of all product calculated price [ qty * actual price]


        for (ProductModel productModel : cartItemList) {
            totalPrice = totalPrice + Double.parseDouble(productModel.getCalculatedAmount());
        }
        finalAmount = totalPrice;
        binding.subTotal.setText(act.getString(R.string.Rs) + HELPER.format.format(finalAmount));


        if (getIntent().hasExtra("promoCode")) {
            double couponDiscount = Double.parseDouble(ResponseHandler.getString(promoCodeObject, "discount"));
            double promoDiscount = 0;
            if (ResponseHandler.getString(promoCodeObject, "discountType").contains("flat")) {
                promoDiscount = (totalPrice - couponDiscount);
                finalAmount = totalPrice - promoDiscount;
                Log.e("Final Apply Code", String.valueOf(finalAmount));

            } else if (ResponseHandler.getString(promoCodeObject, "discountType").contains("percent")) {
                promoDiscount = (totalPrice * couponDiscount) / 100;
                finalAmount = totalPrice - promoDiscount;
                Log.e("Final Apply Code", String.valueOf(finalAmount));

            }

            binding.couponAmoutTxt.setText(act.getString(R.string.Rs) + HELPER.format.format(promoDiscount));
            binding.couponHeading.setText("Coupon Discount (" + ResponseHandler.getString(promoCodeObject, "code") + ")");
            binding.promoCodeContainer.setVisibility(View.GONE);
        } else {
            binding.promoCodeContainer.setVisibility(View.VISIBLE);
            binding.couponHeading.setText("Coupon");
            binding.couponAmoutTxt.setText(act.getString(R.string.Rs) + "0.00");

            binding.couponLayout.setVisibility(View.GONE);
        }

        if (isCodAvailable) {
            binding.codLayout.setVisibility(View.VISIBLE);
            binding.couponAmoutTxt.setText(act.getString(R.string.Rs) + HELPER.format.format(codChargesAmount));
            double codCharges = Double.parseDouble(codChargesAmount);
            finalAmount = finalAmount + codCharges;
        } else {
            binding.codLayout.setVisibility(View.GONE);
        }


        binding.totalTopMRP.setText("Total : " + act.getString(R.string.Rs) + HELPER.format.format(finalAmount));
        binding.finalAmoutPrice.setText(act.getString(R.string.Rs) + HELPER.format.format(finalAmount));
        binding.headingSection.setText("Price Details (" + cartItemList.size() + ")");

        if (cartItemList.size() == 1)
            binding.headingSection.setText("Price Details (" + cartItemList.size() + " Item)");
        else
            binding.headingSection.setText("Price Details (" + cartItemList.size() + " Items)");
    }

}