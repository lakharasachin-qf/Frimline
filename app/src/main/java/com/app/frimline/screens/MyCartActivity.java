package com.app.frimline.screens;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.app.frimline.BaseActivity;
import com.app.frimline.R;
import com.app.frimline.adapters.MyCartAdapter;
import com.app.frimline.common.APIs;
import com.app.frimline.common.CONSTANT;
import com.app.frimline.common.FRIMLINE;
import com.app.frimline.common.HELPER;
import com.app.frimline.common.MySingleton;
import com.app.frimline.common.ObserverActionID;
import com.app.frimline.common.PREF;
import com.app.frimline.common.ResponseHandler;
import com.app.frimline.databinding.ActivityMyCartBinding;
import com.app.frimline.databinding.DialogDiscardImageBinding;
import com.app.frimline.models.DataTransferModel;
import com.app.frimline.models.homeFragments.CouponCodeModel;
import com.app.frimline.models.homeFragments.ProductModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_my_cart);
        makeStatusBarSemiTranspenret(binding.toolbarNavigation.toolbar);


        final ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 0.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(5000L);
        animator.addUpdateListener(animation -> {
            final float progress = (float) animation.getAnimatedValue();
            final float width = binding.first.getWidth();
            final float translationX = width * progress;
            binding.first.setTranslationX(translationX);
            binding.second.setTranslationX(translationX - width);
        });
        animator.start();

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
        binding.toolbarNavigation.backPress.setOnClickListener(v -> HELPER.ON_BACK_PRESS_ANIM(act));

        binding.toolbarNavigation.title.setText("Cart");
        changeColor();

        binding.applyCode.setOnClickListener(v -> {
            if (!binding.promoCodeEdt.getText().toString().trim().isEmpty()) {
                verifyCouponCode();
            } else {
                confirmationDialog("Coupon Code", getString(R.string.please_enter_the_coupon_code_first), CONSTANT.ACTION_3);
            }

        });
        binding.removePromo.setOnClickListener(v -> confirmationDialog("Coupon Code", getString(R.string.are_you_sure_to_remove_promo_code), CONSTANT.ACTION_2));

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
                if (discountType.equalsIgnoreCase("fixed_product") || discountType.contains("percent")) {
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
                public void viewCart(int position, ProductModel model) {
                    //  loadProductDetails();

                    Intent i = new Intent(act, ProductDetailActivity.class);
                    i.putExtra("qty", model.getQty());
                    i.putExtra("cartScreen", "yes");
                    i.putExtra("cartIncrement", "123");
                    i.putExtra("cartDecrement", "124");

                    i.putExtra("productPosition", "0");
                    i.putExtra("layoutType", String.valueOf(-1));
                    i.putExtra("productId", String.valueOf(model.getId()));
                    i.putExtra("itemPosition", String.valueOf(-1));
                    i.putExtra("adapterPosition", String.valueOf(-1));
                    i.putExtra("model", new Gson().toJson(model));
                    i.putExtra("addToCartID", String.valueOf(122));
                    i.putExtra("removeCartID", String.valueOf(121));

                    act.startActivity(i);
                    act.overridePendingTransition(R.anim.right_enter_second, R.anim.left_out_second);
                }

                @Override
                public void onDeleteAction(int position, ProductModel model) {
                    for (int i = 0; i < cartItemList.size(); i++) {
                        if (model.getId().equalsIgnoreCase(cartItemList.get(i).getId())) {
                            db.productEntityDao().deleteProduct(model.getId());
                            cartItemList.remove(i);

                            FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.CART_COUNTER_UPDATE);
                            FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.GLOBAL_CART_REFRESH);
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

            @Override
            public void viewCart(int position, ProductModel model) {

            }
        });

        binding.cartRecycler.setLayoutManager(new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false));
        binding.cartRecycler.setNestedScrollingEnabled(false);
        binding.cartRecycler.setAdapter(shopFilterAdapter);


    }

    @Override
    public void update(Observable observable, Object data) {
        super.update(observable, data);
        if (frimline.getObserver().getValue() == ObserverActionID.LOGIN) {
            if (!act.isFinishing() && !act.isDestroyed()) {
                if (isCouponCodeApplied) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            verifyCouponCode();
                        }
                    });
                }
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (frimline.getObserver().getValue() == 121) {
                    String productId = frimline.getObserver().getModel().getProductId();
                    //remove
                    for (int i = 0; i < cartItemList.size(); i++) {
                        if (productId.equalsIgnoreCase(cartItemList.get(i).getId())) {
                            db.productEntityDao().deleteProduct(productId);
                            cartItemList.remove(i);

                            FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.CART_COUNTER_UPDATE);
                            cartAdapter.notifyItemRemoved(i);
                            cartAdapter.notifyItemRangeChanged(i, cartItemList.size());

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
                if (frimline.getObserver().getValue() == 122) {
                    //added to cart

                    ArrayList<ProductModel> temp = HELPER.getCartList(db.productEntityDao().getAll());
                    cartItemList.clear();
                    cartItemList.addAll(temp);
                    cartAdapter.notifyItemChanged(0, cartItemList.size());
                    binding.boottomFooter.setVisibility(View.VISIBLE);
                    binding.cartRecycler.setVisibility(View.VISIBLE);
                    binding.scrollView.setVisibility(View.VISIBLE);
                    binding.NoDataFound.setVisibility(View.GONE);
                    applyCouponCalculation(isCouponCodeApplied);
                    setState();

                }

                if (frimline.getObserver().getValue() == 123) {//increment
                    String productId = frimline.getObserver().getModel().getProductId();
                    for (int i = 0; i < cartItemList.size(); i++) {
                        if (cartItemList.get(i).getId().equalsIgnoreCase(productId)) {
                            ProductModel model = HELPER.convertFromCartObject(db.productEntityDao().findProductByProductId(productId));
                            cartItemList.set(i, model);
                            cartAdapter.notifyItemChanged(i);
                            applyCouponCalculation(isCouponCodeApplied);
                            setState();
                            break;
                        }
                    }
                }
                if (frimline.getObserver().getValue() == 124) { // descrement
                    String productId = frimline.getObserver().getModel().getProductId();
                    for (int i = 0; i < cartItemList.size(); i++) {
                        if (cartItemList.get(i).getId().equalsIgnoreCase(productId)) {
                            ProductModel model = HELPER.convertFromCartObject(db.productEntityDao().findProductByProductId(productId));
                            cartItemList.set(i, model);
                            cartAdapter.notifyItemChanged(i);
                            applyCouponCalculation(isCouponCodeApplied);
                            setState();
                            break;
                        }
                    }
                }
            }
        });
    }

    public void setNoDataFound() {
        binding.boottomFooter.setVisibility(View.GONE);
        binding.scrollView.setVisibility(View.GONE);
        binding.NoDataFound.setVisibility(View.VISIBLE);
    }

    public void changeColor() {
        binding.boottomText.setTextColor((Color.parseColor(new PREF(act).getThemeColor())));
        binding.second.setTextColor((Color.parseColor(new PREF(act).getThemeColor())));
        binding.first.setTextColor((Color.parseColor(new PREF(act).getThemeColor())));
        HELPER.backgroundTint(act, binding.applyCode, true);
        HELPER.backgroundTint(act, binding.removePromo, true);
        HELPER.backgroundTint(act, binding.bottomSlider, true);
        HELPER.backgroundTint(act, binding.scrollView, true);
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
        discardImageBinding.noTxt.setOnClickListener(v -> alertDialog.dismiss());
        discardImageBinding.yesTxt.setText("Ok");
        if (action == CONSTANT.ACTION_2) {
            discardImageBinding.noTxt.setVisibility(View.VISIBLE);
        }
        discardImageBinding.yesTxt.setOnClickListener(v -> {
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


                    couponCodeModel = new CouponCodeModel();
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

                    couponCodeModel.setExcludeOnSaleItems(ResponseHandler.getBool(jArray.getJSONObject(0), "exclude_sale_items"));

                    int maxLimit = ResponseHandler.getString(jArray.getJSONObject(0), "usage_limit").isEmpty() ? 0 : Integer.parseInt(ResponseHandler.getString(jArray.getJSONObject(0), "usage_limit"));
                    int limitPerUser = ResponseHandler.getString(jArray.getJSONObject(0), "usage_limit_per_user").isEmpty() ? 0 : Integer.parseInt(ResponseHandler.getString(jArray.getJSONObject(0), "usage_limit_per_user"));

                    if (prefManager.isLogin()) {

                        if (maxLimit != 0 && limitPerUser != 0) {
                            JSONArray UsedArray = ResponseHandler.getJSONArray(jArray.getJSONObject(0), "used_by");
                            int userCount = 0;
                            for (int k = 0; k < UsedArray.length(); k++) {
                                if (UsedArray.getString(k).equalsIgnoreCase(prefManager.getUser().getUserId())) {
                                    userCount++;
                                }
                            }
                            if (UsedArray.length() >= maxLimit || userCount >= limitPerUser) {
                                couponCodeModel.setPerUserLimitExist(true);
                            }

                        } else if (maxLimit != 0) {
                            JSONArray UsedArray = ResponseHandler.getJSONArray(jArray.getJSONObject(0), "used_by");
                            int userCount = 0;
                            for (int k = 0; k < UsedArray.length(); k++) {
                                if (UsedArray.getString(k).equalsIgnoreCase(prefManager.getUser().getUserId())) {
                                    userCount++;
                                }
                            }

                            if (UsedArray.length() >= maxLimit) {
                                couponCodeModel.setPerUserLimitExist(true);
                            }
                        } else if (limitPerUser != 0) {
                            JSONArray UsedArray = ResponseHandler.getJSONArray(jArray.getJSONObject(0), "used_by");
                            int userCount = 0;
                            for (int k = 0; k < UsedArray.length(); k++) {
                                if (UsedArray.getString(k).equalsIgnoreCase(prefManager.getUser().getUserId())) {
                                    userCount++;
                                }
                            }

                            if (userCount >= limitPerUser) {
                                couponCodeModel.setPerUserLimitExist(true);
                            }
                        }


                    }


                    couponCodeModel.setMinAmount(String.format("%.2f", Double.parseDouble(ResponseHandler.getString(jArray.getJSONObject(0), "minimum_amount"))));
                    couponCodeModel.setMaxAmount(String.format("%.2f", Double.parseDouble(ResponseHandler.getString(jArray.getJSONObject(0), "maximum_amount"))));
                    isCouponCodeApplied = true;
                    for (String excludedId : couponCodeModel.getExcludeCategoryIds()) {
                        for (Iterator<String> it = couponCodeModel.getCategoryIds().iterator(); it.hasNext(); ) {
                            if (it.next().equalsIgnoreCase(excludedId)) {
                                it.remove();
                            }
                        }
                    }

                    for (String excludedId : couponCodeModel.getExcludeProductIds()) {
                        for (Iterator<String> it = couponCodeModel.getProductIds().iterator(); it.hasNext(); ) {
                            if (it.next().equalsIgnoreCase(excludedId)) {
                                it.remove();
                            }
                        }
                    }

                    String couponCode = ResponseHandler.getString(jArray.getJSONObject(0), "code");
                    couponCodeModel.setCouponCode(couponCode);
                    prefManager.setCouponCode(couponCode);
                    applyProductDiscount();

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
            //when coupon code applied go for check condition
            double promoDiscount = 0;
            if (couponCodeModel != null) {
                String msg = "Coupon code not applicable.";
                boolean canApplyMore = true;

                ArrayList<ProductModel> excludedList = new ArrayList<>();
                ArrayList<ProductModel> includeList = new ArrayList<>();

                if (Double.parseDouble(couponCodeModel.getMaxAmount()) < finalAmount && Double.parseDouble(couponCodeModel.getMaxAmount()) != 0) {
                    msg = "The maximum spend for this coupon is " + act.getString(R.string.Rs) + couponCodeModel.getMaxAmount();
                    canApplyMore = false;
                }

                if (Double.parseDouble(couponCodeModel.getMinAmount()) > finalAmount && Double.parseDouble(couponCodeModel.getMinAmount()) != 0) {
                    msg = "The minimum spend for this coupon is " + act.getString(R.string.Rs) + couponCodeModel.getMinAmount();
                    canApplyMore = false;
                }

                if (couponCodeModel.isPerUserLimitExist()) {
                    msg = "Coupon usage limit has been reached.";
                    canApplyMore = false;
                }

                boolean showError = false;
                ArrayList<ProductModel> duplicateCart = HELPER.getCartList(db.productEntityDao().getAll());
                if (canApplyMore) {

                    totalPrice = 0;
                    boolean canGoFeather = true;

                    if (couponCodeModel.getProductIds().size() != 0) {
                        int productFound = 0;
                        for (int i = 0; i < couponCodeModel.getProductIds().size(); i++) {
                            for (int k = 0; k < duplicateCart.size(); k++) {
                                if (couponCodeModel.getProductIds().get(i).equalsIgnoreCase(duplicateCart.get(k).getId())) {
                                    productFound++;
                                }
                            }
                        }
                        if (productFound == 0) {
                            canGoFeather = false;
                        }
                    }

                    if (couponCodeModel.getCategoryIds().size() != 0) {
                        int productFound = 0;
                        for (int i = 0; i < couponCodeModel.getCategoryIds().size(); i++) {
                            for (int k = 0; k < duplicateCart.size(); k++) {
                                for (int kk = 0; kk < duplicateCart.get(k).getAllCategoryArray().size(); kk++) {
                                    if (couponCodeModel.getCategoryIds().get(i).equalsIgnoreCase(duplicateCart.get(k).getAllCategoryArray().get(kk))) {
                                        productFound++;
                                    }
                                }
                            }
                        }
                        if (productFound == 0) {
                            canGoFeather = false;
                        }
                    }

                    if (canGoFeather) {
                        if (couponCodeModel.getProductIds().size() != 0) {
                            for (int i = 0; i < duplicateCart.size(); i++) {
                                for (int k = 0; k < couponCodeModel.getProductIds().size(); k++) {
                                    if (duplicateCart.get(i).getId().equalsIgnoreCase(couponCodeModel.getProductIds().get(k))) {
                                        includeList.add(duplicateCart.get(i));
                                    }
                                }
                            }
                        }

                        if (couponCodeModel.getCategoryIds().size() != 0) {
                            for (int i = 0; i < duplicateCart.size(); i++) {
                                ArrayList<String> productCatList = duplicateCart.get(i).getAllCategoryArray();
                                for (int k = 0; k < couponCodeModel.getCategoryIds().size(); k++) {
                                    String couponCategoryId = couponCodeModel.getCategoryIds().get(k);
                                    boolean isExist = false;
                                    for (int n = 0; n < productCatList.size(); n++) {
                                        String catId = productCatList.get(n);
                                        if (catId.equalsIgnoreCase(couponCategoryId)) {
                                            isExist = true;
                                            break;
                                        }
                                    }
                                    if (isExist) {
                                        includeList.add(duplicateCart.get(i));
                                    }
                                }
                            }
                        }

                        if (couponCodeModel.getProductIds().size() == 0 && couponCodeModel.getCategoryIds().size() == 0) {
                            includeList.addAll(duplicateCart);
                        }

                        if (couponCodeModel.getExcludeProductIds().size() != 0) {
                            for (int i = 0; i < duplicateCart.size(); i++) {
                                for (int k = 0; k < couponCodeModel.getExcludeProductIds().size(); k++) {
                                    if (duplicateCart.get(i).getId().equalsIgnoreCase(couponCodeModel.getExcludeProductIds().get(k))) {
                                        excludedList.add(duplicateCart.get(i));
                                    }
                                }
                            }

                            for (ProductModel excludedMode : excludedList) {
                                for (Iterator<ProductModel> it = includeList.iterator(); it.hasNext(); ) {
                                    if (it.next().getId().equalsIgnoreCase(excludedMode.getId())) {
                                        it.remove();
                                    }
                                }
                            }
                        }

                        if (couponCodeModel.getExcludeCategoryIds().size() != 0) {
                            for (int i = 0; i < duplicateCart.size(); i++) {
                                for (int k = 0; k < couponCodeModel.getExcludeCategoryIds().size(); k++) {
                                    String couponCatId = couponCodeModel.getExcludeCategoryIds().get(k);
                                    boolean isExist = false;
                                    for (int n = 0; n < duplicateCart.get(i).getAllCategoryArray().size(); n++) {
                                        String catId = duplicateCart.get(i).getAllCategoryArray().get(n);
                                        if (catId.equalsIgnoreCase(couponCatId)) {
                                            isExist = true;
                                            break;
                                        }
                                    }
                                    if (isExist) {
                                        excludedList.add(duplicateCart.get(i));
                                    }
                                }
                            }

                            for (ProductModel excludedMode : excludedList) {
                                for (Iterator<ProductModel> it = includeList.iterator(); it.hasNext(); ) {
                                    if (it.next().getId().equalsIgnoreCase(excludedMode.getId())) {
                                        it.remove();
                                    }
                                }
                            }
                        }

                        if (includeList.size() != 0) {
                            for (int k = 0; k < includeList.size(); k++) {
                                Log.e("ProductName ", includeList.get(k).getName());
                                Log.e("OnSale ", includeList.get(k).isOnSale() + "--");
                            }
                            for (int i = 0; i < cartItemList.size(); i++) {
                                boolean isIncluded = false;
                                for (int k = 0; k < includeList.size(); k++) {
                                    if (cartItemList.get(i).getId().equalsIgnoreCase(includeList.get(k).getId())) {
                                        isIncluded = true;
                                    }
                                }

                                if (couponCodeModel.isExcludeOnSaleItems()) {
                                    if (cartItemList.get(i).isOnSale()) {
                                        isIncluded = false;
                                    }
                                }

                                if (isIncluded) {
                                    if (discountType.contains("fixed_product")) {
                                        totalPrice = totalPrice + Double.parseDouble(cartItemList.get(i).getCalculatedAmount()) - (Integer.parseInt(cartItemList.get(i).getQty()) * couponDiscount);
                                        promoDiscount = promoDiscount + (Integer.parseInt(cartItemList.get(i).getQty()) * couponDiscount);
                                    } else {
                                        double calculateDiscount = (Double.parseDouble(cartItemList.get(i).getCalculatedAmount()) * couponDiscount) / 100;
                                        totalPrice = totalPrice + Double.parseDouble(cartItemList.get(i).getCalculatedAmount()) - calculateDiscount;
                                        promoDiscount = promoDiscount + calculateDiscount;
                                    }
                                } else {
                                    totalPrice = totalPrice + Double.parseDouble(cartItemList.get(i).getCalculatedAmount());
                                }
                            }
                        }

                        finalAmount = totalPrice;

                        if (discountType.contains("fixed_cart")) {
                            totalPrice = 0;
                            for (ProductModel productModel : cartItemList) {
                                totalPrice = totalPrice + Double.parseDouble(productModel.getCalculatedAmount());
                            }
                            promoDiscount = couponDiscount;
                            finalAmount = totalPrice - promoDiscount;
                            binding.couponAmoutTxt.setText("- " + act.getString(R.string.Rs) + HELPER.format.format(promoDiscount));
                            binding.couponHeading.setText("Coupon Discount (" + binding.promoCodeEdt.getText().toString() + ")");
                            binding.couponAmountLayer.setVisibility(View.VISIBLE);
                            binding.promoCodeContainer.setVisibility(View.GONE);
                            binding.appliedCodeSuccess.setVisibility(View.VISIBLE);
                        }

                        if (promoDiscount != 0) {
                            binding.couponAmoutTxt.setText("- " + act.getString(R.string.Rs) + HELPER.format.format(promoDiscount));
                            binding.couponHeading.setText("Coupon Discount (" + binding.promoCodeEdt.getText().toString() + ")");
                            binding.couponAmountLayer.setVisibility(View.VISIBLE);
                            binding.promoCodeContainer.setVisibility(View.GONE);
                            binding.appliedCodeSuccess.setVisibility(View.VISIBLE);
                        } else {
                            showError = true;
                        }

                    } else {
                        showError = true;
                    }

                } else {
                    showError = true;
                }
                if (showError) {
                    confirmationDialog("Coupon Code", msg, CONSTANT.NO_ACTION);
                    binding.promoCodeContainer.setVisibility(View.VISIBLE);
                    binding.appliedCodeSuccess.setVisibility(View.GONE);
                    binding.promoCodeEdt.setText("");
                    binding.couponHeading.setText("Coupon");
                    binding.couponAmoutTxt.setText(act.getString(R.string.Rs) + "0.00");
                    binding.couponAmountLayer.setVisibility(View.GONE);
                    discount = "0";
                    couponCodeModel = null;
                    promoCode = "";
                    discountType = "";
                    isCouponCodeApplied = false;
                    prefManager.setCouponCode("");
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


/*                 int maxLimit = ResponseHandler.getString(jArray.getJSONObject(0), "usage_limit"));
                    int limitPerUser = ResponseHandler.getString(jArray.getJSONObject(0), "usage_limit_per_user"));
                    if (prefManager.isLogin()) {

                        if (maxLimit != 0 && limitPerUser != 0) {
                            JSONArray UsedArray = ResponseHandler.getJSONArray(jArray.getJSONObject(0), "used_by");
                            int userCount = 0;
                            for (int k = 0; k < UsedArray.length(); k++) {
                                if (UsedArray.getString(k).equalsIgnoreCase(prefManager.getUser().getUserId())) {
                                    userCount++;
                                }
                            }
                            if (UsedArray.length() >= maxLimit || userCount >= limitPerUser) {
                                couponCodeModel.setPerUserLimitExist(true);
                            }

                        } else if (maxLimit != 0) {
                            JSONArray UsedArray = ResponseHandler.getJSONArray(jArray.getJSONObject(0), "used_by");
                            int userCount = 0;
                            for (int k = 0; k < UsedArray.length(); k++) {
                                if (UsedArray.getString(k).equalsIgnoreCase(prefManager.getUser().getUserId())) {
                                    userCount++;
                                }
                            }

                            if (UsedArray.length() >= maxLimit) {
                                couponCodeModel.setPerUserLimitExist(true);
                            }
                        } else if (limitPerUser != 0) {
                            JSONArray UsedArray = ResponseHandler.getJSONArray(jArray.getJSONObject(0), "used_by");
                            int userCount = 0;
                            for (int k = 0; k < UsedArray.length(); k++) {
                                if (UsedArray.getString(k).equalsIgnoreCase(prefManager.getUser().getUserId())) {
                                    userCount++;
                                }
                            }

                            if (userCount >= limitPerUser) {
                                couponCodeModel.setPerUserLimitExist(true);
                            }
                        }


                    }*/