package com.app.frimline.screens;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.ColorUtils;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.adapters.OrderImageSliderAdpater;
import com.app.frimline.adapters.ProductDetailsTabAdapter;
import com.app.frimline.databinding.ActivityOrderHistoryViewBinding;
import com.app.frimline.databinding.DialogDiscardImageBinding;
import com.app.frimline.fragments.aboutProducts.AdditionalInfoFragment;
import com.app.frimline.fragments.aboutProducts.DescriptionFragment;
import com.app.frimline.fragments.aboutProducts.HowToUseFragment;
import com.app.frimline.fragments.aboutProducts.IngredientsFragment;
import com.app.frimline.fragments.aboutProducts.QnAFragment;
import com.app.frimline.fragments.aboutProducts.ReviewsFragment;
import com.app.frimline.models.OrderModel;
import com.app.frimline.views.WrapContentHeightViewPager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class OrderHistoryViewActivity extends BaseActivity {
    BottomSheetBehavior sheetBehavior;
    LinearLayout layoutBottomSheet;
    private ActivityOrderHistoryViewBinding binding;
    private OrderModel model;
    private final DescriptionFragment descriptionFragment = new DescriptionFragment();
    private final HowToUseFragment howToUseFragment = new HowToUseFragment();
    private final IngredientsFragment ingredientsFragment = new IngredientsFragment();
    private final AdditionalInfoFragment additionalInfoFragment = new AdditionalInfoFragment();
    private final ReviewsFragment reviewsFragment = new ReviewsFragment();
    private final QnAFragment qnAFragment = new QnAFragment();
    private int indicatorWidth;
    private final int REQUEST_SETTINGS = 0005;
    private final int REQUESTED_STORAGE = 0003;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_order_history_view);
        makeStatusBarSemiTranspenret(binding.toolbarNavigation.toolbar);
        binding.toolbarNavigation.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.ON_BACK_PRESS(act);
            }
        });
        binding.toolbarNavigation.title.setText("Order Details");
        layoutBottomSheet = findViewById(R.id.bottom_sheet);

        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        TextView headingTitle = findViewById(R.id.headingTitle);
        RelativeLayout bottomMinState = findViewById(R.id.bottomMinState);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {

                    case BottomSheetBehavior.STATE_HIDDEN:
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                    case BottomSheetBehavior.STATE_DRAGGING:
                        headingTitle.setVisibility(View.VISIBLE);
                        bottomMinState.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        headingTitle.setVisibility(View.GONE);
                        bottomMinState.setVisibility(View.VISIBLE);
                    }
                    break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        layoutBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBottomSheet();
            }
        });
        setupTabIcons();
        changeTheme();

        if (CONSTANT.API_MODE) {
            model = gson.fromJson(getIntent().getStringExtra("orderModel"), OrderModel.class);
            loadData();
        }
    }

    @SuppressLint("SetTextI18n")
    public void loadData() {
        ViewPager productImagesSlider = findViewById(R.id.productImagesSlider);
        DotsIndicator dotsIndicator = findViewById(R.id.dots_indicator);
        OrderImageSliderAdpater sliderAdapter = new OrderImageSliderAdpater(model.getProductsList(), act);
        productImagesSlider.setAdapter(sliderAdapter);
        dotsIndicator.setViewPager(productImagesSlider);

        TextView orderId = findViewById(R.id.orderId);
        HELPER.LOAD_HTML(orderId, "Order Id : " + model.getOrderNumber());

        CardView deliveredCardview = findViewById(R.id.deliveredCardview);
        TextView innerTxt = findViewById(R.id.innerTxt);
        ImageView deliveryCheckIcon = findViewById(R.id.deliveryCheckIcon);

        if (model.getStatus().equalsIgnoreCase(CONSTANT.PAYMENT_COMPLETED) || model.getStatus().equalsIgnoreCase(CONSTANT.PAYMENT_PROCESSING)) {
            binding.toolbarNavigation.downloadPDf.setVisibility(View.VISIBLE);
        } else {
            binding.toolbarNavigation.downloadPDf.setVisibility(View.GONE);
        }
        if (model.getStatus().equalsIgnoreCase(CONSTANT.PAYMENT_PENDING)) {
            innerTxt.setText(CONSTANT.PAYMENT_PENDING_LABEL + " " + HELPER.convertDate(model.getOrderDate()));
            deliveryCheckIcon.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_pending_icon_white));
        }
        if (model.getStatus().equalsIgnoreCase(CONSTANT.PAYMENT_PROCESSING)) {
            innerTxt.setText(CONSTANT.PAYMENT_PROCESSING_LABEL + " " + HELPER.convertDate(model.getOrderDate()));
            deliveryCheckIcon.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_pending_icon_white));
        }
        if (model.getStatus().equalsIgnoreCase(CONSTANT.PAYMENT_ON_HOLD)) {
            innerTxt.setText(CONSTANT.PAYMENT_ON_HOLD_LABEL + " " + HELPER.convertDate(model.getOrderDate()));
            deliveryCheckIcon.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_pending_icon_white));
        }

        if (model.getStatus().equalsIgnoreCase(CONSTANT.PAYMENT_COMPLETED)) {
            HELPER.LOAD_HTML(innerTxt, "<font><b>" + CONSTANT.PAYMENT_COMPLETED_LABEL + "</b></font> " + HELPER.convertDate(model.getOrderDate()));
            deliveryCheckIcon.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_delivered_order));
        }


        if (model.getStatus().equalsIgnoreCase(CONSTANT.PAYMENT_CANCELLED)) {
            innerTxt.setText(CONSTANT.PAYMENT_CANCELLED_LABEL + " " + HELPER.convertDate(model.getOrderDate()));
            deliveryCheckIcon.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_cancel_black_24dp));
            deliveryCheckIcon.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        }

        if (model.getStatus().equalsIgnoreCase(CONSTANT.PAYMENT_REFUNDED)) {
            innerTxt.setText(CONSTANT.PAYMENT_REFUNDED_LABEL + " " + HELPER.convertDate(model.getOrderDate()));
            deliveryCheckIcon.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_cancel_black_24dp));
            deliveryCheckIcon.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        }

        if (model.getStatus().equalsIgnoreCase(CONSTANT.PAYMENT_FAILED)) {
            innerTxt.setText(CONSTANT.PAYMENT_FAILED_LABEL + " " + HELPER.convertDate(model.getOrderDate()));
            deliveryCheckIcon.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_cancel_black_24dp));
            deliveryCheckIcon.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        }

        if (model.getStatus().equalsIgnoreCase(CONSTANT.PAYMENT_CANCELLED_SALES_RETURN)) {
            innerTxt.setText(CONSTANT.PAYMENT_CANCELLED_SALES_RETURN_LABEL + " " + HELPER.convertDate(model.getOrderDate()));
            deliveryCheckIcon.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_cancel_black_24dp));
            deliveryCheckIcon.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        }

        TextView deliName = findViewById(R.id.deliName);
        TextView contactNo = findViewById(R.id.contactNo);
        if (model.getShippingAddress() != null) {
            deliName.setText(model.getShippingAddress().getFirstName() + " " + model.getShippingAddress().getLastName());

            contactNo.setText("No Filled");
            String shippingAddress = "";


            if (!model.getShippingAddress().getAddress1().isEmpty()) {
                shippingAddress = shippingAddress + model.getShippingAddress().getAddress1();
            }
            if (!model.getShippingAddress().getAddress2().isEmpty()) {
                shippingAddress = shippingAddress + ", " + model.getShippingAddress().getAddress2();
            }

            if (!model.getShippingAddress().getCity().isEmpty()) {
                shippingAddress = shippingAddress + ", " + model.getShippingAddress().getCity();
            }
            if (!model.getShippingAddress().getState().isEmpty()) {
                shippingAddress = shippingAddress + ", " + model.getShippingAddress().getState();
            }
            if (!model.getShippingAddress().getCountry().isEmpty()) {
                shippingAddress = shippingAddress + ", " + model.getShippingAddress().getCountry();
            }
            if (!model.getShippingAddress().getPostCode().isEmpty()) {
                shippingAddress = shippingAddress + " - " + model.getShippingAddress().getPostCode();
            }

            if (!shippingAddress.isEmpty()) {
                TextView deliveryAddress = findViewById(R.id.deliveryAddress);
                deliveryAddress.setText(shippingAddress);

            } else {
                CardView deliverySection = findViewById(R.id.deliverySection);
                deliverySection.setVisibility(View.GONE);
            }

        }


        TextView headingTitle1 = findViewById(R.id.headingTitle1);
        TextView headingTitle = findViewById(R.id.headingTitle);
        if (model.getProductsList().size() == 1) {
            headingTitle1.setText("Price Details (" + model.getProductsList().size() + " Item)");
            headingTitle.setText("Price Details (" + model.getProductsList().size() + " Item)");
        } else {
            headingTitle1.setText("Price Details (" + model.getProductsList().size() + " Items)");
            headingTitle.setText("Price Details (" + model.getProductsList().size() + " Items)");
        }


        TextView finalAmoutPrice1 = findViewById(R.id.finalAmoutPrice1);
        finalAmoutPrice1.setText(act.getString(R.string.Rs) + HELPER.format.format(Double.parseDouble(model.getTotal())));
        TextView totalPrice = findViewById(R.id.totalPrice);
        double actualPrice = 0.00;
        for (int i = 0; i < model.getProductsList().size(); i++) {
            actualPrice = actualPrice + Double.parseDouble(model.getProductsList().get(i).getSubTotal());
        }
        totalPrice.setText(act.getString(R.string.Rs) + HELPER.format.format(actualPrice));


        TextView finalAmoutPrice = findViewById(R.id.finalAmoutPrice);

        finalAmoutPrice.setText(act.getString(R.string.Rs) + HELPER.format.format(Double.parseDouble(model.getTotal())));

        TextView trackingId = findViewById(R.id.trackingId);
        TextView tracklink = findViewById(R.id.trackingLink);

        if (!model.getTrackingId().isEmpty()) {
            SpannableString ss = new SpannableString("Your order has been shipped. Your tracking number is "+model.getTrackingId());
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getTrackingLink()));
                    startActivity(browserIntent);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setFakeBoldText(true);
                    ds.setUnderlineText(false);
                }
            };
            ss.setSpan(clickableSpan, 52, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new StyleSpan(Typeface.BOLD), 52, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(Color.BLACK), 52, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            trackingId.setText(ss);
            trackingId.setMovementMethod(LinkMovementMethod.getInstance());
            trackingId.setHighlightColor(Color.TRANSPARENT);
            trackingId.setVisibility(View.VISIBLE);
        } else {
            trackingId.setVisibility(View.GONE);
        }
        if (!model.getTrackingLink().isEmpty()) {
            SpannableString ss = new SpannableString("Click here to : Track your Order");
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getTrackingLink()));
                    startActivity(browserIntent);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setFakeBoldText(true);
                }
            };
            ss.setSpan(clickableSpan, 15, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new StyleSpan(Typeface.BOLD), 15, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(Color.parseColor(prefManager.getThemeColor())), 15, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            tracklink.setText(ss);
            tracklink.setMovementMethod(LinkMovementMethod.getInstance());
            tracklink.setHighlightColor(Color.TRANSPARENT);
            tracklink.setVisibility(View.VISIBLE);
        } else {
            tracklink.setVisibility(View.GONE);
        }


        TextView shippingChargePrice = findViewById(R.id.shippingChargePrice);
        if (model.getShippingTotal() != null && !model.getShippingTotal().isEmpty()) {
            if (Double.parseDouble(model.getShippingTotal()) == 0) {
                shippingChargePrice.setText("FREE");
            } else {
                shippingChargePrice.setText(act.getString(R.string.Rs) + HELPER.format.format(Double.parseDouble(model.getShippingTotal())));
            }
        }

        RelativeLayout couponLayout = findViewById(R.id.couponLayout);
        if (model.getCouponCode() != null && !model.getCouponCode().isEmpty()) {
            TextView couponAmoutTxt = findViewById(R.id.couponAmoutTxt);
            couponAmoutTxt.setText("- " + act.getString(R.string.Rs) + HELPER.format.format(Double.parseDouble(model.getCouponDiscount())));
            couponLayout.setVisibility(View.VISIBLE);
        } else {
            couponLayout.setVisibility(View.GONE);
        }

        double finalAmount = Double.parseDouble(model.getTotal());
        double afterRoundOff = Double.parseDouble(HELPER.format.format(Math.round(Double.parseDouble(model.getTotal()))));
        double roundedOffValue = afterRoundOff - finalAmount;
        finalAmount = afterRoundOff;


        TextView roundedAmount = findViewById(R.id.roundedAmount);
        roundedAmount.setText(String.format("%.2f", roundedOffValue));
        if (roundedOffValue != 0 && !String.format("%.2f", roundedOffValue).contains("-")) {
            roundedAmount.setText("+" + String.format("%.2f", roundedOffValue));
        }
        if (roundedOffValue == 0) {
            roundedAmount.setText("0");
        }
        finalAmoutPrice1.setText(act.getString(R.string.Rs) + String.format("%.2f", finalAmount));
        finalAmoutPrice.setText(act.getString(R.string.Rs) + String.format("%.2f", finalAmount));

        RelativeLayout codLayout = findViewById(R.id.codLayout);
        if (model.getPaymentMethod().equalsIgnoreCase("cod") && model.getCodCharges()!=null && !model.getCodCharges().isEmpty() && !model.getCodCharges().equalsIgnoreCase("null")) {
            codLayout.setVisibility(View.VISIBLE);
            double codAmount = Double.parseDouble(model.getCodCharges());
            TextView codChargePrice = findViewById(R.id.codChargePrice);
            codChargePrice.setText(act.getString(R.string.Rs) + String.format("%.2f", codAmount));
            if (codAmount == 0) {
                codChargePrice.setText("FREE");
            }
        }

    }

    public void changeTheme() {
        CardView deliveredCardview = findViewById(R.id.deliveredCardview);
        deliveredCardview.setCardBackgroundColor(Color.parseColor(new PREF(act).getThemeColor()));
        LinearLayout priceSection = findViewById(R.id.priceSection);
        priceSection.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.toolbarNavigation.downloadPDf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPDF();

            }
        });
        DotsIndicator dotsIndicator = findViewById(R.id.dots_indicator);
        int code = ColorUtils.setAlphaComponent(Color.parseColor(prefManager.getThemeColor()), 100);
        dotsIndicator.setDotsColor(code);
        dotsIndicator.setSelectedDotColor(Color.parseColor(prefManager.getThemeColor()));
    }


    public void toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    private void setupTabIcons() {
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        WrapContentHeightViewPager wrapContentHeightViewPager = findViewById(R.id.viewPager);
        ProductDetailsTabAdapter adapter = new ProductDetailsTabAdapter(getSupportFragmentManager());
        qnAFragment.setThemColor();
        adapter.addFragment(descriptionFragment, "Description");
        adapter.addFragment(howToUseFragment, "How To Use");
        adapter.addFragment(ingredientsFragment, "Ingredients");
        adapter.addFragment(additionalInfoFragment, "Additional Information");
        adapter.addFragment(reviewsFragment, "Reviews");
        adapter.addFragment(qnAFragment, "Q&A");
        wrapContentHeightViewPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        wrapContentHeightViewPager.setAdapter(adapter);
        wrapContentHeightViewPager.setOffscreenPageLimit(10);
        tabLayout.setupWithViewPager(wrapContentHeightViewPager);
        ScrollView scrollView = findViewById(R.id.scrollView);
        wrapContentHeightViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                scrollView.smoothScrollTo(tabLayout.getLeft(), tabLayout.getTop() - 40);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setSelectedTabIndicatorColor(Color.parseColor(prefManager.getThemeColor()));
        tabLayout.setTabTextColors((Color.parseColor(prefManager.getThemeColor())), (Color.WHITE));

        tabLayout.setVisibility(View.GONE);
        wrapContentHeightViewPager.setVisibility(View.GONE);
    }

    public void downloadPDF() {
        //  new InvoiceDownloadHelper(act).downloadImage(model.getInvoiceLink());

        if (ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(act,
                    new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                    REQUESTED_STORAGE);
        } else {

            new DownloadFileFromURL().execute(model.getInvoiceLink());

        }

        //confirmationDialog();
    }

    //runtime storage permission
    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if ((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean targetSetting = false;
        if (requestCode == REQUESTED_STORAGE) {

            boolean readStorageGrant = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean writeStorageGrant = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            if (readStorageGrant && writeStorageGrant) {
                //permissionsLayoutBinding.checked2.setVisibility(View.VISIBLE);
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE) || shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                    showMessageOKCancel(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, REQUESTED_STORAGE);
                        }
                    });
                } else {
                    targetSetting = true;
                }
            }
        }
        if (targetSetting) {
            showMessageOKCancel((dialog, which) -> {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                act.startActivityForResult(intent, REQUEST_SETTINGS);
            });
        }
    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setMessage("You need to allow access to the permissions")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @SuppressLint("StaticFieldLeak")
    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        String filename = "";
        String outputFile;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            HELPER.showLoadingTran(act);
        }

        @Override
        protected String doInBackground(String... link) {
            int count;
            try {
                URL url = new URL(link[0]);
                HELPER.print("url",url.toString());
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                int contentLength = urlConnection.getContentLength();
                String headerField = urlConnection.getHeaderField("Content-Disposition");
                String[] headerSpit = headerField.split("filename=");
                filename = headerSpit[1].replace("filename=", "").replace("\"", "").trim();
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    String destURL = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + CONSTANT.FOLDER_NAME;
                    File desFile = new File(destURL);
                    if (!desFile.exists()) {
                        desFile.mkdir();
                    }
                    destURL = destURL + File.separator + filename;
                    outputFile = destURL;
                    OutputStream output = new FileOutputStream(destURL);
                    byte[] data = new byte[1024];
                    long total = 0;
                    while ((count = inputStream.read(data)) != -1) {
                        total += count;
                        publishProgress("" + (int) ((total * 100) / contentLength));
                        output.write(data, 0, count);
                    }
                    output.flush();
                    output.close();
                    inputStream.close();

                } else {
                    //below Android 11 save pdf
                    BufferedInputStream bis = new BufferedInputStream(inputStream);
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.MediaColumns.DISPLAY_NAME, filename);
                    String desDirectory = Environment.DIRECTORY_DOWNLOADS;
                    desDirectory = desDirectory + File.separator + CONSTANT.FOLDER_NAME;
                    File desFile = new File(desDirectory);
                    if (!desFile.exists()) {
                        desFile.mkdir();
                    }
                    outputFile = desDirectory + File.separator + filename;
                    values.put(MediaStore.MediaColumns.RELATIVE_PATH, desDirectory);
                    Uri uri = act.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                    if (uri != null) {
                        OutputStream outputStream = act.getContentResolver().openOutputStream(uri);
                        if (outputStream != null) {
                            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
                            byte[] bytes = new byte[1024];
                            while ((count = inputStream.read(bytes)) != -1) {
                                bos.write(bytes, 0, count);
                                bos.flush();
                            }
                            bos.close();
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            //Log.e("d", "s" + (progress[0]));
        }


        @Override
        protected void onPostExecute(String file_url) {
            HELPER.dismissLoadingTran();
            // Reading filepath from sdcard
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {

                String FilePath = Environment.getExternalStorageDirectory().toString() + "/" + filename;
                Toast.makeText(act, "Invoice downloaded successfully", Toast.LENGTH_SHORT).show();
                Log.e("pdf-stored", "" + FilePath);

                File file = new File(outputFile);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri apkURI = FileProvider.getUriForFile(act, act.getApplicationContext().getPackageName() + ".provider", file);
                intent.setDataAndType(apkURI, "application/pdf");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                act.startActivity(intent);
            } else {

                String FilePath = Environment.getExternalStorageDirectory().toString() + "/" + filename;
                Toast.makeText(act, "Invoice downloaded successfully", Toast.LENGTH_SHORT).show();

               Log.e("pdf-stored", "" + FilePath);
                File file = new File(Environment.getExternalStorageDirectory() + "/" + outputFile);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri apkURI = FileProvider.getUriForFile(act, act.getApplicationContext().getPackageName() + ".provider", file);
                intent.setDataAndType(apkURI, "application/pdf");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                act.startActivity(intent);
            }
        }

    }

}