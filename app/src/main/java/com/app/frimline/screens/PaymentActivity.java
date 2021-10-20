package com.app.frimline.screens;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpHeaderParser;
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
import com.app.frimline.databinding.DialogDiscardImageBinding;
import com.app.frimline.databinding.DialogOrderSuccessBinding;
import com.app.frimline.databinding.DialogTermsConditionBinding;
import com.app.frimline.models.HomeFragements.CouponCodeModel;
import com.app.frimline.models.HomeFragements.ProductModel;
import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PaymentActivity extends BaseActivity implements PaymentResultWithDataListener {
    MyCartAdapter cartAdapter;
    ArrayList<ProductModel> cartItemList;
    private ActivityPaymentBinding binding;
    private boolean isLoading = false;

    private JSONObject orderParam;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Checkout.preload(getApplicationContext());
        binding = DataBindingUtil.setContentView(act, R.layout.activity_payment);
        makeStatusBarSemiTranspenret(binding.toolbarNavigation.toolbar);
        ((ViewGroup) findViewById(R.id.bottomSlider)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        binding.shippingLabelHint.setSelected(true);

        binding.boottomFooter.setOnClickListener(v -> {
            //generateRazorpayOrderId();
            if (binding.acceptTerms.isChecked()) {
                if (binding.payOnlineRadioBtn.isChecked()) {
                    createPayment();
                } else {
                    finalOrderDone();
                }
            } else {
                errorDialog("Error", getString(R.string.accept_term_n_condition), 1);
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


        binding.paymentModeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == binding.payCodRadioBtn.getId()) {
                isCodAvailable = true;
                if (codChargesAmount.isEmpty()) {
                    getCODCharges();
                } else {
                    calculateCart();
                }
            } else {
                isCodAvailable = false;
                calculateCart();
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
                orderParam.put("line_items", productArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            binding.payOnlineRadioBtn.setChecked(true);

            if (promoCodeObject != null) {
                JSONObject couponCode = new JSONObject();
                try {
                    couponCode.put("code", promoCodeObject.getString("code"));
                    if (promoCodeObject.getString("discountType").equalsIgnoreCase("percent"))
                        couponCode.put("amount", promoCodeObject.getString("discount") + "%");
                    else
                        couponCode.put("amount", promoCodeObject.getString("discount"));

                    couponCode.put("type", promoCodeObject.getString("discountType"));
                    orderParam.put("coupon_apply", couponCode);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

        SpannableString ss = new SpannableString("I have read and agree to the website terms and conditions");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                showTermsNConditions();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setFakeBoldText(true);
            }
        };
        ss.setSpan(clickableSpan, 29, 57, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 29, 29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor(prefManager.getThemeColor())), 29, 29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                binding.acceptTerms.setChecked(!binding.acceptTerms.isChecked());
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);

            }
        };

        ss.setSpan(clickableSpan2, 0, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.acceptTermsLabel.setText(ss);
        binding.acceptTermsLabel.setMovementMethod(LinkMovementMethod.getInstance());
        binding.acceptTermsLabel.setHighlightColor(Color.TRANSPARENT);

    }

    private JSONObject orderCreated;

    private void createOrder() {
        if (!isLoading)
            isLoading = true;
        HELPER.showLoadingTran(act);

        HELPER.print("orderPosting", orderParam.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, APIs.CREATE_ORDER, orderParam, response -> {
            HELPER.print("response", response.toString());
            HELPER.dismissLoadingTran();
            orderCreated = response;
            isShippingChargeAvailable = true;
            calculateCart();
        }, error -> {
            error.printStackTrace();
            HELPER.dismissLoadingTran();
            isLoading = false;
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return getHeader();
            }
        };
        //9938
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
                if (ResponseHandler.getString(object, "status").equalsIgnoreCase("200")) {
                    isCodAvailable = true;
                    codChargesAmount = ResponseHandler.getString(object, "cod_charge");
                }
                calculateCart();
            }

        }, error -> {
            error.printStackTrace();
            HELPER.dismissLoadingTran();
            isLoading = false;
        }
        ) {
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

    private String razorpayOrderId;

    private void generateRazorpayOrderId() {
        if (!isLoading)
            isLoading = true;
        HELPER.showLoadingTran(act);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.COD_CHARGES, response -> {
            HELPER.dismissLoadingTran();
            JSONObject object = ResponseHandler.createJsonObject(response);
            if (object != null) {
                if (object.has("id")) {
                    razorpayOrderId = ResponseHandler.getString(object, "id");
                    createPayment();
                }
            }

        }, error -> {
            error.printStackTrace();
            HELPER.dismissLoadingTran();
            isLoading = false;
            NetworkResponse response = error.networkResponse;
            if (response != null && response.statusCode == 400) {
                try {
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    JSONObject jsonObject = new JSONObject(jsonString);
                    errorDialog("Error", ResponseHandler.getString(jsonObject, "message"), 0);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }

            }

        }
        ) {
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

    DialogDiscardImageBinding discardImageBinding;

    public void errorDialog(String title, String msg, int action) {
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

    boolean isCodAvailable = false;
    boolean isShippingChargeAvailable = false;
    JSONObject promoCodeObject;
    String codChargesAmount = "";

    public void init() {
        try {
            if (getIntent().hasExtra("promoCode") && getIntent().getStringExtra("promoCode") != null && !getIntent().getStringExtra("promoCode").isEmpty())
                promoCodeObject = new JSONObject(getIntent().getStringExtra("promoCode"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    double finalAmountToPay;
    String roundedOFFAmount;
    String finalAmountAfterRoundOFF;
    boolean fixedProductCoupon = false;
    CouponCodeModel couponCodeModel;

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public void calculateCart() {

        double finalAmount;

        double totalPrice = 0; // sum of all product calculated price [ qty * actual price]


        for (ProductModel productModel : cartItemList) {
            totalPrice = totalPrice + Double.parseDouble(productModel.getCalculatedAmount());
        }
        finalAmount = totalPrice;
        binding.subTotal.setText(act.getString(R.string.Rs) + HELPER.format.format(finalAmount));


        if (promoCodeObject != null) {
            double couponDiscount = Double.parseDouble(ResponseHandler.getString(promoCodeObject, "discount"));
            double promoDiscount = 0;
            if (ResponseHandler.getString(promoCodeObject, "discountType").contains("fixed_cart")) {

                promoDiscount = couponDiscount;
                finalAmount = totalPrice - couponDiscount;
                binding.couponLayout.setVisibility(View.VISIBLE);
            } else {

                couponCodeModel = gson.fromJson(getIntent().getStringExtra("modelCoupon"), CouponCodeModel.class);

                if (couponCodeModel != null) {

                    String msg = "Coupon code not applicable.";
                    boolean canApplyMore = true;
                    boolean isMinMaxAvailable = false;

                    ArrayList<ProductModel> excludedList = new ArrayList<>();
                    ArrayList<ProductModel> includeList = new ArrayList<>();

                    if (Double.parseDouble(couponCodeModel.getMaxAmount()) < finalAmount && Double.parseDouble(couponCodeModel.getMaxAmount()) != 0) {
                        msg = "The maximum spend for this coupon is " + act.getString(R.string.Rs) + couponCodeModel.getMaxAmount();
                        canApplyMore = false;
                    } else if (Double.parseDouble(couponCodeModel.getMaxAmount()) != 0 && Double.parseDouble(couponCodeModel.getMaxAmount()) >= finalAmount) {
                        isMinMaxAvailable = true;
                    }

                    if (Double.parseDouble(couponCodeModel.getMinAmount()) > finalAmount && Double.parseDouble(couponCodeModel.getMinAmount()) != 0) {
                        msg = "The minimum spend for this coupon is " + act.getString(R.string.Rs) + couponCodeModel.getMinAmount();
                        canApplyMore = false;
                    } else if (Double.parseDouble(couponCodeModel.getMinAmount()) != 0 && Double.parseDouble(couponCodeModel.getMinAmount()) <= finalAmount) {
                        isMinMaxAvailable = true;
                    }

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
                                isMinMaxAvailable = false;
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
                                isMinMaxAvailable = false;
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
                                isMinMaxAvailable = false;
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
                                isMinMaxAvailable = false;
                                for (ProductModel excludedMode : excludedList) {
                                    for (Iterator<ProductModel> it = includeList.iterator(); it.hasNext(); ) {
                                        if (it.next().getId().equalsIgnoreCase(excludedMode.getId())) {
                                            it.remove();
                                        }
                                    }
                                }
                            }

                            if (includeList.size() != 0) {
                                for (int i = 0; i < cartItemList.size(); i++) {
                                    boolean isIncluded = false;
                                    for (int k = 0; k < includeList.size(); k++) {
                                        if (cartItemList.get(i).getId().equalsIgnoreCase(includeList.get(k).getId())) {
                                            isIncluded = true;
                                        }
                                    }
                                    if (isIncluded) {
                                        if (ResponseHandler.getString(promoCodeObject, "discountType").contains("fixed_product")) {
                                            totalPrice = totalPrice + Double.parseDouble(cartItemList.get(i).getCalculatedAmount()) - (Integer.parseInt(cartItemList.get(i).getQty()) * couponDiscount);
                                            promoDiscount = promoDiscount + (Integer.parseInt(cartItemList.get(i).getQty()) * couponDiscount);
                                        } else {
                                            double totalCouponDiscountToCalculate = Integer.parseInt(cartItemList.get(i).getQty()) * couponDiscount;
                                            double calculateDiscount = (Double.parseDouble(cartItemList.get(i).getCalculatedAmount()) * totalCouponDiscountToCalculate) / 100;
                                            totalPrice = totalPrice + Double.parseDouble(cartItemList.get(i).getCalculatedAmount()) - calculateDiscount;
                                            promoDiscount = promoDiscount + calculateDiscount;
                                        }
                                    } else {
                                        totalPrice = totalPrice + Double.parseDouble(cartItemList.get(i).getCalculatedAmount());
                                    }

                                }
                            }

                            finalAmount = totalPrice;
                            if (promoDiscount != 0) {
                                binding.couponLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }

            binding.couponAmoutTxt.setText("- " + act.getString(R.string.Rs) + HELPER.format.format(promoDiscount));
            binding.couponHeading.setText("Coupon Discount (" + ResponseHandler.getString(promoCodeObject, "code") + ")");

        } else {
            binding.couponHeading.setText("Coupon");
            binding.couponAmoutTxt.setText(act.getString(R.string.Rs) + "0.00");
            binding.couponLayout.setVisibility(View.GONE);
        }

        if (isCodAvailable) {
            binding.codLayout.setVisibility(View.VISIBLE);
            double codCharges = Double.parseDouble(codChargesAmount);
            finalAmount = finalAmount + codCharges;
            binding.CODAmount.setText(act.getString(R.string.Rs) + HELPER.format.format(codCharges));
            binding.codLayout.setVisibility(View.VISIBLE);
        } else {
            binding.codLayout.setVisibility(View.GONE);
        }

        double shippingCharges;
        double roundedOffValue = 0;

        if (isShippingChargeAvailable) {
            String shippingChargesStr = (ResponseHandler.getString(orderCreated, "shipping_total").isEmpty() ? "0" : ResponseHandler.getString(orderCreated, "shipping_total"));
            shippingCharges = Double.parseDouble(shippingChargesStr);
            finalAmount = finalAmount + shippingCharges;
            binding.shippingChargeAmount.setText("+" + act.getString(R.string.Rs) + HELPER.format.format(shippingCharges));
            binding.shippingLayout.setVisibility(View.VISIBLE);
            double afterRoundOff = Double.parseDouble(HELPER.format.format(Math.round(finalAmount)));
            roundedOffValue = afterRoundOff - finalAmount;
            finalAmount = afterRoundOff;
            binding.shippingLayout.setVisibility(View.VISIBLE);
            if (shippingCharges == 0) {
                binding.shippingChargeAmount.setText("FREE");
            }
        } else {
            binding.shippingLayout.setVisibility(View.VISIBLE);
            binding.shippingChargeAmount.setText("FREE");

        }

        binding.roundedAmount.setText(String.format("%.2f", roundedOffValue));
        if (roundedOffValue != 0 && !String.format("%.2f", roundedOffValue).contains("-")) {
            binding.roundedAmount.setText("+" + String.format("%.2f", roundedOffValue));
        }
        if (roundedOffValue == 0) {
            binding.roundedAmount.setText("0");
        }
        binding.totalTopMRP.setText("Total : " + act.getString(R.string.Rs) + String.format("%.2f", finalAmount));
        binding.finalAmoutPrice.setText(act.getString(R.string.Rs) + String.format("%.2f", finalAmount));
        binding.headingSection.setText("Price Details (" + cartItemList.size() + ")");

        if (cartItemList.size() == 1)
            binding.headingSection.setText("Price Details (" + cartItemList.size() + " Item)");
        else
            binding.headingSection.setText("Price Details (" + cartItemList.size() + " Items)");

        roundedOFFAmount = String.format("%.2f", roundedOffValue);
        finalAmountAfterRoundOFF = String.format("%.2f", finalAmount);

    }


    public void createPayment() {
        Checkout checkout = new Checkout();
        checkout.setImage(R.drawable.logo_demo);
        final Activity activity = this;
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Frimline");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //options.put("orderId", razorpayOrderId);
            options.put("theme.color", prefManager.getThemeColor());
            options.put("currency", "INR");
            int finalAmount = (int) (Double.parseDouble(finalAmountAfterRoundOFF) * 100);
            options.put("amount", String.valueOf(finalAmount));
            HELPER.print("Param", options.toString());
            checkout.open(activity, options);
        } catch (Exception e) {
            e.printStackTrace();
            HELPER.print("TAG", "Error in starting Razorpay Checkout" + e);
        }
    }


    String transactionId;

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try {
            isOnlinePaymentSuccess = true;
            transactionId = paymentData.getPaymentId();
            HELPER.print("Payment Success", gson.toJson(paymentData));
            finalOrderDone();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        isOnlinePaymentSuccess = false;
        HELPER.print("Payment Fail", s);
        errorDialog("Order Fail!", getString(R.string.your_order_is_cancel), 1);
        // finalOrderDone();
    }

    boolean isOnlinePaymentSuccess = false;

    private void finalOrderDone() {
        if (!isLoading)
            isLoading = true;
        HELPER.showLoadingTran(act);
        try {

            orderParam.put("order_id", ResponseHandler.getString(orderCreated, "id"));
            orderParam.put("shipping_charges", ResponseHandler.getString(orderCreated, "shipping_total"));
            orderParam.put("round_off", roundedOFFAmount);
            orderParam.put("round_total", finalAmountAfterRoundOFF);
            orderParam.put("cart_discount", ResponseHandler.getString(orderCreated, "discount_total"));

            if (binding.payCodRadioBtn.isChecked()) {
                orderParam.put("payment_method", "cod");
                double codAmount = Double.parseDouble(codChargesAmount);
                orderParam.put("cod_charges", String.format("%.2f", codAmount));
                orderParam.put("status", "1");
            }
            if (binding.payOnlineRadioBtn.isChecked()) {

                orderParam.put("payment_method", "razorpay");
                orderParam.put("transaction_id", transactionId);

                if (isOnlinePaymentSuccess)
                    orderParam.put("status", "1");
                else
                    orderParam.put("status", "0");

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        HELPER.print("orderPosting", orderParam.toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, APIs.COMPLETE_ORDER, orderParam, response -> {
            HELPER.print("order done", response.toString());
            HELPER.dismissLoadingTran();

            if (binding.payCodRadioBtn.isChecked()) {
                paymentSuccess();
                db.productEntityDao().deleteAll();
                prefManager.setCouponCode("");
            }
            if (binding.payOnlineRadioBtn.isChecked()) {

                if (isOnlinePaymentSuccess) {
                    paymentSuccess();
                    db.productEntityDao().deleteAll();
                }
            }

        }, error -> {
            error.printStackTrace();
            HELPER.dismissLoadingTran();
            isLoading = false;
            NetworkResponse response = error.networkResponse;
            if (response != null && response.statusCode == 400) {
                try {
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    JSONObject jsonObject = new JSONObject(jsonString);
                    errorDialog("Error", ResponseHandler.getString(jsonObject, "message"), 0);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
            if (response != null && response.statusCode == 500) {
                try {
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    JSONObject jsonObject = new JSONObject(jsonString);
                    HELPER.print("ERROR", ResponseHandler.getString(jsonObject, "message"));
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }

        }) {
            @Override
            public Map<String, String> getHeaders() {
                return getHeader();
            }
        };
        MySingleton.getInstance(act).addToRequestQueue(request);
    }

    DialogOrderSuccessBinding orderSuccessBinding;

    public void paymentSuccess() {
        orderSuccessBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_order_success, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(orderSuccessBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(orderSuccessBinding.getRoot());

        VectorChildFinder vector = new VectorChildFinder(act, R.drawable.ic_order_confirmed, orderSuccessBinding.illustation);
        VectorDrawableCompat.VFullPath path1 = vector.findPathByName("primary");
        path1.setFillColor(Color.parseColor(new PREF(act).getThemeColor()));
        path1 = vector.findPathByName("primary1");
        path1.setFillColor(Color.parseColor(new PREF(act).getThemeColor()));

        orderSuccessBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                Intent i = new Intent(act, CategoryLandingActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("targetCategory", "yes");
                i.putExtra("fragment", "order");
                startActivity(i);

            }
        });
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }


    public class MyWebChromeClient extends WebChromeClient {
        public void onProgressChanged(WebView view, int newProgress) {
            termsConditionBinding.screenLoader.setVisibility(View.VISIBLE);
            termsConditionBinding.screenLoader.setProgress(newProgress);
        }
    }

    public class webClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            termsConditionBinding.screenLoader.setVisibility(View.GONE);
            termsConditionBinding.staticPagesWebView.setVisibility(View.VISIBLE);
        }
    }

    private DialogTermsConditionBinding termsConditionBinding;

    public void showTermsNConditions() {
        HELPER.dismissLoadingTran();
        termsConditionBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_terms_condition, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(termsConditionBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(termsConditionBinding.getRoot());
        termsConditionBinding.staticPagesWebView.setWebChromeClient(new MyWebChromeClient());
        termsConditionBinding.staticPagesWebView.setWebViewClient(new webClient());
        termsConditionBinding.staticPagesWebView.getSettings().setLoadWithOverviewMode(true);
        termsConditionBinding.staticPagesWebView.getSettings().setSupportZoom(true);
        termsConditionBinding.dialogCloseImg.setImageTintList(ColorStateList.valueOf(Color.parseColor(prefManager.getThemeColor())));
        termsConditionBinding.staticPagesWebView.getSettings().setJavaScriptEnabled(true);
        termsConditionBinding.staticPagesWebView.loadUrl(CONSTANT.TERM_N_CONDITIONS);
        termsConditionBinding.screenLoader.getIndeterminateDrawable().setColorFilter(Color.parseColor(prefManager.getThemeColor()), android.graphics.PorterDuff.Mode.MULTIPLY);
        termsConditionBinding.screenLoader.setProgressTintList(ColorStateList.valueOf(Color.parseColor(prefManager.getThemeColor())));
        termsConditionBinding.dialogCloseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

}