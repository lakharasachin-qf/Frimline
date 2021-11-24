package com.app.frimline.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.app.frimline.R;
import com.app.frimline.databaseHelper.CartRoomDatabase;
import com.app.frimline.databinding.FragmentCategoryRootBinding;
import com.app.frimline.databinding.FragmentHomeBinding;
import com.app.frimline.models.categoryRootFragments.CategorySingleModel;
import com.app.frimline.models.homeFragments.ProductModel;
import com.app.frimline.models.homeFragments.TradingStoriesModel;
import com.app.frimline.models.HomeModel;
import com.app.frimline.models.LAYOUT_TYPE;
import com.app.frimline.models.OutCategoryModel;
import com.app.frimline.models.roomModels.ProductEntity;
import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HELPER {

    public static Dialog dialog;
    public static DecimalFormat format = new DecimalFormat("0.00");

    public static void print(String tag, String message) {
        Log.e(tag, message);
    }

    /**
     * INTENT-EVENT
     */
    public static void SIMPLE_ROUTE(Activity act, Class routeName) {
        Intent i = new Intent(act, routeName);
        act.startActivity(i);
        act.overridePendingTransition(R.anim.right_enter_second, R.anim.left_out_second);
    }

    public static void ROUTE(Activity act, Class routeName) {
        act.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
        act.finish();
        Intent i = new Intent(act, routeName);
        act.startActivity(i);
        act.overridePendingTransition(R.anim.right_enter_second, R.anim.left_out_second);
    }

    public static void ROUTE_ANIM(Activity act, Class routeName) {
        Intent i = new Intent(act, routeName);
        act.startActivity(i);
        act.overridePendingTransition(R.anim.act_left_out, R.anim.act_right_enter);
    }

    public static void ON_BACK_PRESS(Activity act) {
        act.finish();
        //   act.overridePendingTransition(R.anim.act_left_out, R.anim.act_right_enter);
    }

    public static void ON_BACK_PRESS_ANIM(Activity act) {
        act.finish();
        act.overridePendingTransition(R.anim.act_left_out, R.anim.act_right_enter);
    }

    //for Edittext Color Effect
    public static void SET_STYLE(Activity act, EditText editText, View view) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    view.setBackground(ContextCompat.getDrawable(act, R.drawable.shape_editext_background_active));
                    GradientDrawable drawable = (GradientDrawable) view.getBackground();
                    drawable.setStroke(3, Color.parseColor(new PREF(act).getThemeColor()));
                } else {
                    view.setBackground(ContextCompat.getDrawable(act, R.drawable.shape_editext_background));
                }
            }
        });
    }

    //CategoryRoot
    public static void changeTheme(Activity act, String primaryColor) {
        ImageView logo = act.findViewById(R.id.logo);
        VectorChildFinder vector = new VectorChildFinder(act, R.drawable.ic_logo_green, logo);
        VectorDrawableCompat.VFullPath path1 = vector.findPathByName("background");
        path1.setFillColor(Color.parseColor(primaryColor));
        logo.invalidate();
    }

    //Category Root Fragment
    public static void changeThemeCategoryRootFragment(FragmentCategoryRootBinding binding, String primaryColor) {
        binding.headingUnderlineView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(primaryColor)));
        binding.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(primaryColor)));
    }

    //Home Fragment
    public static void changeThemeHomeFragment(FragmentHomeBinding binding, String primaryColor) {
        // binding.headingUnderlineView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(primaryColor)));
    }



    public static int getStatusBarHeight(Activity act) {
        int result = 0;
        int resourceId = act.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = act.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static AlertDialog createAlert(Activity act, String title, String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(act)
                .setTitle(title)
                .setIcon(null)
                .setMessage(msg);


        return alertDialog.create();
    }

    public static void ERROR_HELPER(TextInputEditText editText, TextInputLayout nameEdtLayout) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameEdtLayout.setError("");
                nameEdtLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public static void FOCUS_HELPER(ScrollView scrollView, TextInputEditText editText, TextInputLayout nameEdtLayout) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    int scrollTo = ((View) v.getParent().getParent()).getTop() + v.getTop();

                    scrollView.smoothScrollTo(0, scrollTo);
                    // scrollView.scrollTo(editText.getLeft(), editText.getBottom());
                }
            }
        });
    }

    public static void FOCUS_HELPER(NestedScrollView scrollView, TextInputEditText editText, TextInputLayout nameEdtLayout) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    int scrollTo = ((View) v.getParent().getParent()).getTop() + v.getTop();
                    scrollView.smoothScrollTo(0, scrollTo);
                }
            }
        });
    }

    public static void ERROR_HELPER_For_MOBILE_VALIDATION(TextInputEditText editText, TextInputLayout nameEdtLayout) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameEdtLayout.setError("");
                nameEdtLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void setWhiteNavigationBar(@NonNull Dialog dialog, Activity act) {
        Window window = dialog.getWindow();
        if (window != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            GradientDrawable dimDrawable = new GradientDrawable();
            // ...customize your dim effect here
            GradientDrawable navigationBarDrawable = new GradientDrawable();
            navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
            navigationBarDrawable.setColor(act.getColor(R.color.grey));
            // navigationBarDrawable.setTint(act.getColor(R.color.WhiteColor));
            Drawable[] layers = {dimDrawable, navigationBarDrawable};
            LayerDrawable windowBackground = new LayerDrawable(layers);
            windowBackground.setLayerInsetTop(1, metrics.heightPixels);
            window.setBackgroundDrawable(windowBackground);
        }
    }

    public static void showLoadingTran(Activity act) {

        if (dialog != null && dialog.isShowing())
            return;

        if (act.isDestroyed() || act.isFinishing())
            return;


        dialog = new Dialog(act);
        dialog.getWindow().setBackgroundDrawableResource(
                R.color.colorProgressBackground);

        View view = LayoutInflater.from(act).inflate(R.layout.progress_bar_layout, null);
        dialog.setContentView(view);
        ProgressBar progressBar = view.findViewById(R.id.progressBar2);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor(new PREF(act).getThemeColor()), android.graphics.PorterDuff.Mode.MULTIPLY);
        progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        dialog.setCancelable(false);
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Showing Alert Message
                try {
                    if (dialog != null && !dialog.isShowing())
                        dialog.show();
                } catch (WindowManager.BadTokenException e) {
                    e.printStackTrace();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void dismissLoadingTran() {
        try {
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void LOAD_HTML(TextView textView, String data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(Html.fromHtml(data));
        }
    }

    /**
     * Determine if the device is a tablet (i.e. it has a large screen).
     *
     * @param context The calling context.
     */

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static ProductEntity convertToCartObject(ProductModel model) {
        ProductEntity entity = new ProductEntity();
        entity.setId(model.getId());
        entity.setQty(model.getQty());
        entity.setRating(model.getRating());
        entity.setCalculatedAmount(model.getCalculatedAmount());
        entity.setName(model.getName());
        entity.setAllCatList(model.getAllCategoryArray());
        entity.setSlug(model.getSlug());
        entity.setDescription(model.getDescription());
        entity.setShortDescription(model.getShortDescription());
        entity.setRegularPrice(model.getRegularPrice());
        entity.setPrice(model.getPrice());
        entity.setOnSale(model.isOnSale());

        entity.setPriceHtml(model.getPriceHtml());
        entity.setCategoryId(model.getCategoryId());
        entity.setSubCategoryId(model.getSubCategoryId());
        entity.setCategoryName(model.getCategoryName());
        entity.setStockStatus(model.getStockStatus());
        entity.setProductImagesList(model.getProductImagesList());
        entity.setTagsModel(model.getTagsModel());
        entity.setAttribute(model.getAttribute());

        return entity;
    }

    public static ProductModel convertFromCartObject(ProductEntity model) {
        ProductModel cartProduct = new ProductModel();
        cartProduct.setId(model.getId());
        cartProduct.setOnSale(model.isOnSale());
        cartProduct.setCartId(model.getCartId());
        cartProduct.setCalculatedAmount(model.getCalculatedAmount());
        cartProduct.setRating(model.getRating());
        cartProduct.setName(model.getName());
        cartProduct.setQty(model.getQty());
        cartProduct.setSlug(model.getSlug());
        cartProduct.setAllCategoryArray(model.getAllCatList());
        cartProduct.setDescription(model.getDescription());
        cartProduct.setShortDescription(model.getShortDescription());
        cartProduct.setRegularPrice(model.getRegularPrice());
        cartProduct.setPrice(model.getPrice());

        cartProduct.setPriceHtml(model.getPriceHtml());
        cartProduct.setCategoryId(model.getCategoryId());
        cartProduct.setSubCategoryId(model.getSubCategoryId());
        cartProduct.setCategoryName(model.getCategoryName());
        cartProduct.setStockStatus(model.getStockStatus());
        cartProduct.setProductImagesList(model.getProductImagesList());
        cartProduct.setTagsModel(model.getTagsModel());
        cartProduct.setAttribute(model.getAttribute());

        return cartProduct;
    }

    public static void changeCartCounter(Activity act) {
        CartRoomDatabase cartRoomDatabase = CartRoomDatabase.getAppDatabase(act);
        TextView cartCounterTop = act.findViewById(R.id.cartCounterTop);
        TextView cartCounterNav = act.findViewById(R.id.cartCounterNav);
        RelativeLayout cartBackgroundLayar = act.findViewById(R.id.cartBackgroundLayar);
        RelativeLayout cartBackgroundLayar2 = act.findViewById(R.id.cartBackgroundLayar2);

        int count = cartRoomDatabase.productEntityDao().getAll().size();

        if (count != 0) {
            cartCounterTop.setText(String.valueOf(count));
            cartCounterNav.setText(String.valueOf(count));
            cartBackgroundLayar.setVisibility(View.VISIBLE);
            cartBackgroundLayar2.setVisibility(View.VISIBLE);
        } else {
            cartBackgroundLayar.setVisibility(View.INVISIBLE);
            cartBackgroundLayar2.setVisibility(View.INVISIBLE);
        }
    }

    public static void changeCartCounterToolbar(Activity act) {
        CartRoomDatabase cartRoomDatabase = CartRoomDatabase.getAppDatabase(act);
        TextView cartCounterTop = act.findViewById(R.id.cartCounterTop);

        RelativeLayout cartBackgroundLayar = act.findViewById(R.id.cartBackgroundLayar);


        int count = cartRoomDatabase.productEntityDao().getAll().size();

        if (count != 0) {
            cartCounterTop.setText(String.valueOf(count));

            cartBackgroundLayar.setVisibility(View.VISIBLE);

        } else {
            cartBackgroundLayar.setVisibility(View.INVISIBLE);

        }
    }

    public static ArrayList<ProductModel> getCartList(List<ProductEntity> all) {
        ArrayList<ProductModel> cartList = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            cartList.add(convertFromCartObject(all.get(i)));
        }
        return cartList;
    }

    public static String incrementAction(ProductModel model) {
        double aDouble = Double.parseDouble(model.getPrice());
        int qty = Integer.parseInt(model.getQty());
        double finalAmount = aDouble * qty;
        return format.format((finalAmount));
    }

    public static void backgroundTint(Activity act, View view, boolean theme) {
        if (theme)
            view.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        else
            view.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
    }

    public static void imageTint(Activity act, ImageView view, boolean theme) {
        if (theme)
            view.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        else
            view.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getCategoryColor())));
    }


    public static ArrayList<HomeModel> setAdapterForProduct() {
        ArrayList<HomeModel> productArray3 = new ArrayList<>();

        ArrayList<com.app.frimline.models.ProductModel> productModelArrayList = new ArrayList<>();
        com.app.frimline.models.ProductModel productModel = new com.app.frimline.models.ProductModel();
        productModelArrayList.add(productModel);
        productModelArrayList.add(productModel);
        productModelArrayList.add(productModel);

        HomeModel homeModel = new HomeModel();

        ArrayList<OutCategoryModel> innerDataList = new ArrayList<>();
        OutCategoryModel outCategoryModel = new OutCategoryModel();
        outCategoryModel.setName("");
        innerDataList.add(outCategoryModel);
        outCategoryModel = new OutCategoryModel();
        outCategoryModel.setName("");
        innerDataList.add(outCategoryModel);
        outCategoryModel = new OutCategoryModel();
        outCategoryModel.setName("");
        innerDataList.add(outCategoryModel);
        homeModel.setProductList(innerDataList);
        homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_THREE_PRODUCT);
        homeModel.setProductModels(productModelArrayList);
        productArray3.add(homeModel);
        productArray3.add(homeModel);
        productArray3.add(homeModel);
        return productArray3;
    }

    public static ArrayList<com.app.frimline.models.homeFragments.ProductModel> setAdapterForTopRattedProduct() {
        ArrayList<com.app.frimline.models.homeFragments.ProductModel> modelArrayList = new ArrayList<>();
        modelArrayList.add(new com.app.frimline.models.homeFragments.ProductModel());
        modelArrayList.add(new com.app.frimline.models.homeFragments.ProductModel());
        modelArrayList.add(new com.app.frimline.models.homeFragments.ProductModel());
        modelArrayList.add(new com.app.frimline.models.homeFragments.ProductModel());
        modelArrayList.add(new com.app.frimline.models.homeFragments.ProductModel());
        return modelArrayList;
    }


    public static ArrayList<TradingStoriesModel> setAdapterForTrendingProduct() {
        ArrayList<TradingStoriesModel> modelArrayList = new ArrayList<>();
        modelArrayList.add(new TradingStoriesModel());
        modelArrayList.add(new TradingStoriesModel());
        modelArrayList.add(new TradingStoriesModel());
        modelArrayList.add(new TradingStoriesModel());
        modelArrayList.add(new TradingStoriesModel());
        return modelArrayList;
    }

    public static ArrayList<CategorySingleModel> setAdapterForOurProduct() {
        ArrayList<CategorySingleModel> modelArrayList = new ArrayList<>();
        modelArrayList.add(new CategorySingleModel());
        modelArrayList.add(new CategorySingleModel());
        modelArrayList.add(new CategorySingleModel());
        return modelArrayList;
    }

    public static void closeKeyboard(View view, Activity act) {
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public static String convertDate(String dateStr) {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        Date result = null;
        String convertedDate = "";
        if (dateStr != null) {
            try {
                result = df.parse(dateStr);
                System.out.println("date:" + result);
                //@SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                // sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                System.out.println(sdf.format(result)); // prints date in the format sdf
                convertedDate = sdf.format(result);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return convertedDate;
    }

    public static String getConsecutiveOTP(String message) {
        String otp = "";
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(message);
        while (m.find()) {

            if (m.group().length() == 4) {
                otp = m.group();
            }
            break;
        }
        return otp;
    }

}
