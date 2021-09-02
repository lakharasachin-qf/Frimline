package com.app.frimline.screens;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderHistoryViewActivity extends BaseActivity {
    BottomSheetBehavior sheetBehavior;
    LinearLayout layoutBottomSheet;
    DialogDiscardImageBinding discardImageBinding;
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
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        headingTitle.setVisibility(View.VISIBLE);
                        bottomMinState.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        headingTitle.setVisibility(View.GONE);
                        bottomMinState.setVisibility(View.VISIBLE);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        headingTitle.setVisibility(View.VISIBLE);
                        bottomMinState.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
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
        HELPER.LOAD_HTML(orderId, "Order Id : " + model.getOrderKey());


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

        TextView couponAmoutTxt = findViewById(R.id.couponAmoutTxt);
        couponAmoutTxt.setText(act.getString(R.string.Rs) + HELPER.format.format(Double.parseDouble(model.getDiscountTotal())));

        TextView finalAmoutPrice = findViewById(R.id.finalAmoutPrice);
        finalAmoutPrice.setText(act.getString(R.string.Rs) + HELPER.format.format(Double.parseDouble(model.getTotal())));

        TextView trackingId = findViewById(R.id.trackingId);
        TextView tracklink = findViewById(R.id.trackingLink);
        Log.e("TrackingId", model.getTrackingId() + "-");
        Log.e("TrackingLink", model.getTrackingLink() + "-");
        if (!model.getTrackingId().isEmpty()) {
            trackingId.setText(model.getTrackingId());

        } else {
            trackingId.setVisibility(View.GONE);
        }
        if (!model.getTrackingLink().isEmpty()) {
            tracklink.setText(model.getTrackingLink());
        } else {
            tracklink.setVisibility(View.GONE);
        }

    }

    public void changeTheme() {
        CardView deliveredCardview = findViewById(R.id.deliveredCardview);
        deliveredCardview.setCardBackgroundColor(Color.parseColor(new PREF(act).getThemeColor()));
        LinearLayout priceSection = findViewById(R.id.priceSection);
        priceSection.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
//        ImageView deliveryCheckIcon = findViewById(R.id.deliveryCheckIcon);
//        deliveryCheckIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(prefManager.getThemeColor())));
//        deliveryCheckIcon.setBackgroundTintList(ColorStateList.valueOf((Color.WHITE)));
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

    public void confirmationDialog() {
        discardImageBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_discard_image, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(discardImageBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(discardImageBinding.getRoot());

        discardImageBinding.titleTxt.setText("Report Download");
        discardImageBinding.subTitle.setText("Order report downloaded.");

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
        qnAFragment.setThemColor(true);
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
            Toast.makeText(act, "permission", Toast.LENGTH_SHORT).show();
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
                    showMessageOKCancel("You need to allow access to the permissions", new DialogInterface.OnClickListener() {
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
            showMessageOKCancel("You need to allow access to the permissions", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, REQUEST_SETTINGS);
                }
            });
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public class Downloading extends AsyncTask<String, Integer, String> {

        @Override
        public void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... url) {
            File mydir = new File(Environment.getExternalStorageDirectory() + "/11zon");
            if (!mydir.exists()) {
                mydir.mkdirs();
            }

            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(url[0]);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);

            SimpleDateFormat dateFormat = new SimpleDateFormat("mmddyyyyhhmmss");
            String date = dateFormat.format(new Date());

            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle("Downloading")
                    .setDestinationInExternalPublicDir("/11zon", date + ".jpg");

            manager.enqueue(request);
            return mydir.getAbsolutePath() + File.separator + date + ".jpg";
        }

        @Override
        public void onPostExecute(String s) {
            super.onPostExecute(s);

            Toast.makeText(getApplicationContext(), "Image Saved", Toast.LENGTH_SHORT).show();
        }
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        String filename = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            HELPER.showLoadingTran(act);
            // showDialog(progress_bar_type);
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();
                String depo = conection.getHeaderField("Content-Disposition");
                String[] depoSplit = depo.split("filename=");
                filename = depoSplit[1].replace("filename=", "").replace("\"", "").trim();
                Log.e("", "fileName" + filename);

                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream
                OutputStream output = new FileOutputStream("/sdcard/" + filename);

                byte[] data = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            Log.e("d", "s" + (progress[0]));
        }


        @Override
        protected void onPostExecute(String file_url) {
            HELPER.dismissLoadingTran();
            // Reading filepath from sdcard
            String FilePath = Environment.getExternalStorageDirectory().toString() + "/" + filename;
            Toast.makeText(act, "Invoice downloaded successfully", Toast.LENGTH_SHORT).show();
            Log.v("FilePath", "" + FilePath);
        }

    }
}