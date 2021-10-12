package com.app.frimline.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
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
import com.app.frimline.R;
import com.app.frimline.adapters.MyCartAdapter;
import com.app.frimline.databinding.ActivityMyCartBinding;
import com.app.frimline.databinding.DialogDiscardImageBinding;
import com.app.frimline.models.DataTransferModel;
import com.app.frimline.models.HomeFragements.CouponCodeModel;
import com.app.frimline.models.HomeFragements.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyCartActivity extends BaseActivity {
    MyCartAdapter cartAdapter;
    ArrayList<ProductModel> cartItemList;
    DialogDiscardImageBinding discardImageBinding;
    private ActivityMyCartBinding binding;
    private boolean isCouponCodeApplied = false;
    private String promoCode;
    private String discount = "0";
    private String discountType = "";
    private boolean isLoading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_my_cart);
        makeStatusBarSemiTranspenret(binding.toolbarNavigation.toolbar);

        binding.boottomFooter.setOnClickListener(v -> {
            if (prefManager.isLogin()) {
                if (CONSTANT.API_MODE) {
                    proceedToPay();
                } else {
                    HELPER.SIMPLE_ROUTE(act, BillingAddressActivity.class);
                }
            } else {
                HELPER.SIMPLE_ROUTE(act, LoginActivity.class);
            }
        });
        binding.toolbarNavigation.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.ON_BACK_PRESS_ANIM(act);
            }
        });

        binding.toolbarNavigation.title.setText("Cart");
        changeColor();


        binding.applyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.promoCodeEdt.getText().toString().trim().isEmpty()) {
                    verifyCouponCode();
                } else {
                    confirmationDialog("Coupon Code", getString(R.string.please_enter_the_coupon_code_first), CONSTANT.ACTION_3);
                }

            }
        });
        binding.removePromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialog("Coupon Code", getString(R.string.are_you_sure_to_remove_promo_code), CONSTANT.ACTION_2);
            }
        });

        if (CONSTANT.API_MODE) {
            loadData();
        } else {
            setAdapter();
        }

        if (prefManager.getCouponCode() != null && !prefManager.getCouponCode().isEmpty()) {
            binding.promoCodeEdt.setText(prefManager.getCouponCode());
            verifyCouponCode();
        }

    }

    public void proceedToPay() {
        try {
            Intent intent = new Intent(act, BillingAddressActivity.class);
            JSONObject couponObject = new JSONObject();
            if (isCouponCodeApplied) {
                couponObject.put("code", promoCode);
                couponObject.put("discount", discount);
                couponObject.put("discountType", discountType);

                intent.putExtra("promoCode", couponObject.toString());
                if (discountType.equalsIgnoreCase("fixed_product")) {
                    intent.putExtra("modelCoupon", gson.toJson(couponCodeModel));
                }
            }
            act.startActivity(intent);
            act.overridePendingTransition(R.anim.right_enter_second, R.anim.left_out_second);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            cartAdapter = new MyCartAdapter(cartItemList, act, "cart");
            cartAdapter.setActionsListener(new MyCartAdapter.setActionsListener() {
                @Override
                public void onDeleteAction(int position, ProductModel model) {
                    for (int i = 0; i < cartItemList.size(); i++) {
                        if (model.getId().equalsIgnoreCase(cartItemList.get(i).getId())) {
                            db.productEntityDao().deleteProduct(model.getId());
                            cartItemList.remove(i);

                            FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.CART_COUNTER_UPDATE);
                            cartAdapter.notifyItemRemoved(position);
                            cartAdapter.notifyItemRangeChanged(position, cartItemList.size());


                            if (getIntent().hasExtra("dataModel")) {
                                DataTransferModel dataTransferModel = gson.fromJson(getIntent().getStringExtra("dataModel"), DataTransferModel.class);
                                FRIMLINE.getInstance().getObserver().setValue(Integer.parseInt(dataTransferModel.getCartRemovedId()), dataTransferModel);
                            }
                            if (cartItemList.size() == 0) {
                                prefManager.setCouponCode("");
                                setNoDataFound();
                                return;
                            }
                            applyCouponCalculation(isCouponCodeApplied);
                            setState();
                            break;
                        }
                    }
                }

                @Override
                public void onCartUpdate(int position, ProductModel model) {
                    applyCouponCalculation(isCouponCodeApplied);
                    setState();

                }
            });

            binding.cartRecycler.setLayoutManager(new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false));
            binding.cartRecycler.setNestedScrollingEnabled(false);
            binding.cartRecycler.setAdapter(cartAdapter);
            setState();


            applyCouponCalculation(isCouponCodeApplied);

        } else {
            setNoDataFound();
        }
    }


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


        MyCartAdapter shopFilterAdapter = new MyCartAdapter(arrayList, act, "cart");
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
        HELPER.backgroundTint(act, binding.applyCode, true);
        HELPER.backgroundTint(act, binding.removePromo, true);
        HELPER.backgroundTint(act, binding.bottomSlider, true);
        HELPER.backgroundTint(act, binding.scrollView, true);
        //binding.successAppliedCode.setTextColor(Color.parseColor(prefManager.getThemeColor()));
    }

    public void confirmationDialog(String title, String msg, int action) {
        HELPER.dismissLoadingTran();
        discardImageBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_discard_image, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(discardImageBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(discardImageBinding.getRoot());
        discardImageBinding.titleTxt.setText(title);
        discardImageBinding.subTitle.setText(msg);
        discardImageBinding.noTxt.setVisibility(View.GONE);
        discardImageBinding.noTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });
        discardImageBinding.yesTxt.setText("Ok");
        discardImageBinding.yesTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();


                if (action != CONSTANT.NO_ACTION) {
                    if (action == CONSTANT.ACTION_2) {
                        HELPER.showLoadingTran(act);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                HELPER.dismissLoadingTran();
                                isCouponCodeApplied = false;
                                applyCouponCalculation(false);
                                Toast.makeText(act, "Coupon Code removed.", Toast.LENGTH_SHORT).show();
                                binding.promoCodeContainer.setVisibility(View.VISIBLE);
                                binding.appliedCodeSuccess.setVisibility(View.GONE);
                                prefManager.setCouponCode("");
                            }
                        }, 1000);

                    }
                }


            }
        });
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }


    CouponCodeModel couponCodeModel = new CouponCodeModel();

    private void verifyCouponCode() {
        if (isLoading)
            return;

        isLoading = true;

        HELPER.showLoadingTran(act);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.VALIDATE_CODE, response -> {
            HELPER.print("coupon", response);
            isLoading = false;
            HELPER.dismissLoadingTran();
            try {
                JSONArray jArray = new JSONArray(response);
                if (jArray.length() != 0) {
                    discount = ResponseHandler.getString(jArray.getJSONObject(0), "amount");
                    promoCode = ResponseHandler.getString(jArray.getJSONObject(0), "code");
                    discountType = ResponseHandler.getString(jArray.getJSONObject(0), "discount_type");


                    JSONArray productArray = ResponseHandler.getJSONArray(jArray.getJSONObject(0), "product_ids");
                    ArrayList<String> productId = new ArrayList<>();
                    for (int k = 0; k < productArray.length(); k++) {
                        productId.add(productArray.getString(k));
                    }
                    couponCodeModel.setProductIds(productId);


                    JSONArray excludeProductArray = ResponseHandler.getJSONArray(jArray.getJSONObject(0), "excluded_product_ids");
                    ArrayList<String> excludeProductId = new ArrayList<>();
                    for (int k = 0; k < excludeProductArray.length(); k++) {
                        excludeProductId.add(excludeProductArray.getString(k));
                    }
                    couponCodeModel.setExcludeProductIds(excludeProductId);


                    JSONArray categoryArray = ResponseHandler.getJSONArray(jArray.getJSONObject(0), "product_categories");
                    ArrayList<String> categoryId = new ArrayList<>();
                    for (int k = 0; k < categoryArray.length(); k++) {
                        categoryId.add(categoryArray.getString(k));
                    }
                    couponCodeModel.setCategoryIds(categoryId);


                    JSONArray excludeCategoryArray = ResponseHandler.getJSONArray(jArray.getJSONObject(0), "excluded_product_categories");
                    ArrayList<String> excludeCategoryId = new ArrayList<>();
                    for (int k = 0; k < excludeCategoryArray.length(); k++) {
                        excludeCategoryId.add(excludeCategoryArray.getString(k));
                    }
                    couponCodeModel.setExcludeCategoryIds(excludeCategoryId);


                    JSONArray emailArray = ResponseHandler.getJSONArray(jArray.getJSONObject(0), "email_restrictions");
                    ArrayList<String> emailId = new ArrayList<>();
                    for (int k = 0; k < emailArray.length(); k++) {
                        emailId.add(emailArray.getString(k));
                    }
                    couponCodeModel.setEmailIds(emailId);

                    couponCodeModel.setMinAmount(String.format("%.2f", Double.parseDouble(ResponseHandler.getString(jArray.getJSONObject(0), "minimum_amount"))));
                    couponCodeModel.setMaxAmount(String.format("%.2f", Double.parseDouble(ResponseHandler.getString(jArray.getJSONObject(0), "maximum_amount"))));
                    Log.e("couponCodeModel", gson.toJson(couponCodeModel));
                    isCouponCodeApplied = true;
                    applyProductDiscount();

                    prefManager.setCouponCode(binding.promoCodeEdt.getText().toString().trim());

                } else {
                    JSONObject object = ResponseHandler.createJsonObject(response);
                    confirmationDialog("Coupon Code", getString(R.string.coupon_code_not_applicable), CONSTANT.NO_ACTION);
                }
            } catch (JSONException e) {
                e.printStackTrace();
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
                            confirmationDialog("Coupon Code", ResponseHandler.getString(jsonObject, "message"), CONSTANT.NO_ACTION);
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
                params.put("coupon_code", binding.promoCodeEdt.getText().toString().trim());
                return params;
            }
        };

        MySingleton.getInstance(act).addToRequestQueue(stringRequest);
    }


    public void applyProductDiscount() {
        double totalPrice = 0;

        for (ProductModel productModel : cartItemList) {
            if (isCouponCodeApplied) {
                productModel.setCalculatedWithDiscount(productModel.getCalculatedAmount());
            }

            totalPrice = totalPrice + Double.parseDouble(productModel.getCalculatedAmount());
        }

        applyCouponCalculation(isCouponCodeApplied);

    }


    double fromDiscountCalu = 0;
    ArrayList<ProductModel> tempArray = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    public void applyCouponCalculation(boolean apply) {
        double finalAmount;
        double couponDiscount = Double.parseDouble(discount);
        double totalPrice = 0;
        isCouponCodeApplied = apply;
        for (ProductModel productModel : cartItemList) {
            totalPrice = totalPrice + Double.parseDouble(productModel.getCalculatedAmount());
        }

        finalAmount = totalPrice;

        binding.totalPrice.setText(act.getString(R.string.Rs) + HELPER.format.format(finalAmount));

        if (apply) {
            double promoDiscount = 0;
            if (discountType.contains("percent")) {
                promoDiscount = (totalPrice * couponDiscount) / 100;
                finalAmount = totalPrice - promoDiscount;
                binding.couponAmoutTxt.setText("- " + act.getString(R.string.Rs) + HELPER.format.format(promoDiscount));
                binding.couponHeading.setText("Coupon Discount (" + binding.promoCodeEdt.getText().toString() + ")");
                binding.couponAmountLayer.setVisibility(View.VISIBLE);
                binding.promoCodeContainer.setVisibility(View.GONE);
                binding.appliedCodeSuccess.setVisibility(View.VISIBLE);
            } else if (discountType.contains("fixed_cart")) {
                promoDiscount = couponDiscount;
                finalAmount = totalPrice - promoDiscount;
                binding.couponAmoutTxt.setText("- " + act.getString(R.string.Rs) + HELPER.format.format(promoDiscount));
                binding.couponHeading.setText("Coupon Discount (" + binding.promoCodeEdt.getText().toString() + ")");
                binding.couponAmountLayer.setVisibility(View.VISIBLE);
                binding.promoCodeContainer.setVisibility(View.GONE);
                binding.appliedCodeSuccess.setVisibility(View.VISIBLE);
            } else {

                if (couponCodeModel != null) {

                    String msg = "Coupon code not applicable.";

                    boolean canApplyMore = true;
                    boolean isMinMaxAvailable = false;

                    ArrayList<ProductModel> cartProducts = HELPER.getCartList(db.productEntityDao().getAll());
                    ArrayList<ProductModel> excludedList = new ArrayList<>();
                    ArrayList<ProductModel> includeList = new ArrayList<>();


                    if (Double.parseDouble(couponCodeModel.getMaxAmount()) < finalAmount && Double.parseDouble(couponCodeModel.getMaxAmount()) != 0) {
                        msg = "The maximum spend for this coupon is " + act.getString(R.string.Rs) + couponCodeModel.getMaxAmount();
                        canApplyMore = false;
                    }else if (Double.parseDouble(couponCodeModel.getMaxAmount()) != 0 && Double.parseDouble(couponCodeModel.getMaxAmount()) >= finalAmount ){
                        isMinMaxAvailable=true;
                    }

                    if (Double.parseDouble(couponCodeModel.getMinAmount()) > finalAmount && Double.parseDouble(couponCodeModel.getMinAmount()) != 0) {
                        msg = "The minimum spend for this coupon is " + act.getString(R.string.Rs) + couponCodeModel.getMinAmount();
                        canApplyMore = false;
                    }else if (Double.parseDouble(couponCodeModel.getMinAmount()) != 0 && Double.parseDouble(couponCodeModel.getMinAmount()) <= finalAmount ){
                        isMinMaxAvailable=true;
                    }


                    if (canApplyMore) {
                        totalPrice = 0;
                        if (couponCodeModel.getExcludeCategoryIds().size() != 0) {
                            for (int i = 0; i < cartItemList.size(); i++) {
                                for (int k = 0; k < couponCodeModel.getExcludeCategoryIds().size(); k++) {
                                    if (cartItemList.get(i).getCategoryId().equalsIgnoreCase(couponCodeModel.getExcludeCategoryIds().get(k))) {
                                        excludedList.add(cartItemList.get(i));
                                        HELPER.print("Exclu-cat",cartItemList.get(i).getName()+ " "+cartItemList.get(i).getCategoryName());
                                    }
                                }
                            }
                        }
                        if (couponCodeModel.getExcludeProductIds().size() != 0) {
                            for (int i = 0; i < cartItemList.size(); i++) {
                                for (int k = 0; k < couponCodeModel.getExcludeProductIds().size(); k++) {
                                    if (cartItemList.get(i).getId().equalsIgnoreCase(couponCodeModel.getExcludeProductIds().get(k))) {
                                        excludedList.add(cartItemList.get(i));
                                        HELPER.print("Exclu-cat",cartItemList.get(i).getName()+ " "+cartItemList.get(i).getCategoryName());
                                    }
                                }
                            }
                        }
                        if (couponCodeModel.getCategoryIds().size() != 0) {
                            for (int i = 0; i < cartItemList.size(); i++) {
                                for (int k = 0; k < couponCodeModel.getCategoryIds().size(); k++) {
                                    if (cartItemList.get(i).getCategoryId().equalsIgnoreCase(couponCodeModel.getCategoryIds().get(k))) {
                                        includeList.add(cartItemList.get(i));
                                        HELPER.print("Include-cat",cartItemList.get(i).getName()+ " "+cartItemList.get(i).getCategoryName());
                                    }
                                }
                            }
                        }
                        if (couponCodeModel.getProductIds().size() != 0) {
                            for (int i = 0; i < cartItemList.size(); i++) {
                                for (int k = 0; k < couponCodeModel.getProductIds().size(); k++) {
                                    if (cartItemList.get(i).getId().equalsIgnoreCase(couponCodeModel.getProductIds().get(k))) {
                                        includeList.add(cartItemList.get(i));
                                        HELPER.print("Include-cat",cartItemList.get(i).getName()+ " "+cartItemList.get(i).getCategoryName());
                                    }
                                }
                            }
                        }


                        if (includeList.size() != 0 || excludedList.size() != 0) {

                            for (int i = 0; i < cartItemList.size(); i++) {
                                HELPER.print("Include-cat",cartItemList.get(i).getName()+ "- "+cartItemList.get(i).getCategoryId());
                                boolean isExcluded = false;
                                for (int k = 0; k < excludedList.size(); k++) {
                                    if (cartItemList.get(i).getId().equalsIgnoreCase(excludedList.get(k).getId())) {
                                        isExcluded = true;
                                    }
                                }
                                boolean isIncluded = false;
                                for (int k = 0; k < includeList.size(); k++) {
                                    if (cartItemList.get(i).getId().equalsIgnoreCase(includeList.get(k).getId())) {
                                        isIncluded = true;
                                    }
                                }
                                if (!isExcluded && isIncluded) {
                                    totalPrice = totalPrice + Double.parseDouble(cartItemList.get(i).getCalculatedAmount()) - (Integer.parseInt(cartItemList.get(i).getQty()) * couponDiscount);
                                    promoDiscount = promoDiscount + (Integer.parseInt(cartItemList.get(i).getQty()) * couponDiscount);
                                }
                                else {
                                    totalPrice = totalPrice + Double.parseDouble(cartItemList.get(i).getCalculatedAmount());
                                }
                            }
                        } else {

                            for (int i = 0; i < cartItemList.size(); i++) {
                                if(isMinMaxAvailable){

                                    totalPrice = totalPrice + Double.parseDouble(cartItemList.get(i).getCalculatedAmount()) - (Integer.parseInt(cartItemList.get(i).getQty()) * couponDiscount);
                                    promoDiscount = promoDiscount + (Integer.parseInt(cartItemList.get(i).getQty()) * couponDiscount);
                                }else {
                                    totalPrice = totalPrice + Double.parseDouble(cartItemList.get(i).getCalculatedAmount()); //- (Integer.parseInt(cartItemList.get(i).getQty()) * couponDiscount);
                                    promoDiscount = 0;
                                }
                            }
                        }

                        finalAmount = totalPrice;
                        if (promoDiscount != 0) {
                            binding.couponAmoutTxt.setText("- " + act.getString(R.string.Rs) + HELPER.format.format(promoDiscount));
                            binding.couponHeading.setText("Coupon Discount (" + binding.promoCodeEdt.getText().toString() + ")");
                            binding.couponAmountLayer.setVisibility(View.VISIBLE);
                            binding.promoCodeContainer.setVisibility(View.GONE);
                            binding.appliedCodeSuccess.setVisibility(View.VISIBLE);
                        } else {
                            confirmationDialog("Coupon Code", msg, CONSTANT.NO_ACTION);
                            binding.promoCodeContainer.setVisibility(View.VISIBLE);
                            binding.appliedCodeSuccess.setVisibility(View.GONE);
                            binding.promoCodeEdt.setText("");
                            binding.couponHeading.setText("Coupon");
                            binding.couponAmoutTxt.setText(act.getString(R.string.Rs) + "0.00");
                            binding.couponAmountLayer.setVisibility(View.GONE);
                        }
                    } else {
                        confirmationDialog("Coupon Code", msg, CONSTANT.NO_ACTION);
                        binding.promoCodeContainer.setVisibility(View.VISIBLE);
                        binding.appliedCodeSuccess.setVisibility(View.GONE);
                        binding.promoCodeEdt.setText("");
                        binding.couponHeading.setText("Coupon");
                        binding.couponAmoutTxt.setText(act.getString(R.string.Rs) + "0.00");
                        binding.couponAmountLayer.setVisibility(View.GONE);
                    }
                }
            }
            binding.successAppliedCode.setSelected(true);
            HELPER.LOAD_HTML(binding.successAppliedCode, "<font color = '" + prefManager.getThemeColor() + "'><b>" + binding.promoCodeEdt.getText().toString() + "</b></font> code applied successfully");
            HELPER.LOAD_HTML(binding.couponHeading, "Coupon Discount <b>(" + binding.promoCodeEdt.getText().toString() + ")<b>");

        } else {
            binding.promoCodeContainer.setVisibility(View.VISIBLE);
            binding.appliedCodeSuccess.setVisibility(View.GONE);
            binding.promoCodeEdt.setText("");
            binding.couponHeading.setText("Coupon");
            binding.couponAmoutTxt.setText(act.getString(R.string.Rs) + "0.00");
            binding.couponAmountLayer.setVisibility(View.GONE);
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