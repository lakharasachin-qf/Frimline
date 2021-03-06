package com.app.frimline.screens;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.SEND_SMS;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.FRIMLINE;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.ObserverActionID;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.adapters.LoginTabAdapter;
import com.app.frimline.databinding.ActivityLoginBinding;
import com.app.frimline.databinding.DialogDiscardImageBinding;
import com.app.frimline.fragments.LoginVEmailFragment;
import com.app.frimline.fragments.LoginVMobileFragment;
import com.app.frimline.views.WrapContentHeightViewPager;
import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import com.google.android.material.tabs.TabLayout;

import java.util.Observable;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private int indicatorWidth;
    boolean pendingBackPress = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_login);
        changeTheme();
        setupTabIcons();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        binding.backPress.setOnClickListener(v -> onBackPressed());

        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) binding.backPress.getLayoutParams();
        layoutParams.topMargin = HELPER.getStatusBarHeight(act);
        binding.backPress.setLayoutParams(layoutParams);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        loginWidth();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean targetSetting = false;
        if (requestCode == CONSTANT.REQUEST_CODE_READ_SMS) {
            if (grantResults.length > 0){
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loginFragment.callApi(true);
                }else if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                    if (ActivityCompat.shouldShowRequestPermissionRationale(act, RECEIVE_SMS)) {
                        loginFragment.callApi(true);
                    }else{
                        prefManager.AskOTP(false);
                    }
                }
            }else{

                loginFragment.callApi(true);
            }
        }



    }

    DialogDiscardImageBinding discardImageBinding;
    public void askDialogForPermission(String title, String msg,int code) {
        discardImageBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_discard_image, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(discardImageBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(discardImageBinding.getRoot());

        discardImageBinding.titleTxt.setText(title);
        HELPER.LOAD_HTML(discardImageBinding.subTitle, msg);
        discardImageBinding.yesTxt.setText("Allow");
        discardImageBinding.noTxt.setText("Cancel");
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
                if (code == 0){
                    ActivityCompat.requestPermissions(act, new String[]{RECEIVE_SMS}, CONSTANT.REQUEST_CODE_READ_SMS);
                }else {
                    ActivityCompat.requestPermissions(act, new String[]{RECEIVE_SMS}, CONSTANT.REQUEST_CODE_READ_SMS);
                }
            }
        });
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    public void loginWidth() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int fragmentWidth = (width / 6) + 20;

        if (HELPER.isTablet(act)) {
            fragmentWidth = (width / 6) + 40;
            RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams) binding.logo.getLayoutParams();
            layoutParams.topMargin = fragmentWidth;
            binding.logo.setLayoutParams(layoutParams);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        if (pendingBackPress) {
            act.onBackPressed();
        }
    }
    LoginVMobileFragment loginFragment;
    LoginVEmailFragment vEmailFragment;
    private void setupTabIcons() {
          loginFragment = new LoginVMobileFragment();
          vEmailFragment = new LoginVEmailFragment();
        WrapContentHeightViewPager wrapContentHeightViewPager = findViewById(R.id.viewPager);
        wrapContentHeightViewPager.setOffscreenPageLimit(3);
        LoginTabAdapter adapter = new LoginTabAdapter(getSupportFragmentManager());
        adapter.addFragment(loginFragment, "Sign In");
        adapter.addFragment(vEmailFragment, "Sign In");

        wrapContentHeightViewPager.setAdapter(adapter);
        binding.tab.setupWithViewPager(wrapContentHeightViewPager);
        wrapContentHeightViewPager.setNestedScrollingEnabled(false);
        /* Determine indicator width at runtime */
        binding.tab.post(() -> {
            indicatorWidth = binding.tab.getWidth() / binding.tab.getTabCount();
            FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) binding.indicator.getLayoutParams();
            indicatorParams.width = indicatorWidth;
            binding.indicator.setLayoutParams(indicatorParams);
            /* binding.helper.getLayoutParams().height=binding.indicator.getLayoutParams().height; */
        });

        wrapContentHeightViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int i, float positionOffset, int positionOffsetPx) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) binding.indicator.getLayoutParams();
                float translationOffset = (positionOffset + i) * indicatorWidth;
                params.leftMargin = (int) translationOffset;
                binding.indicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        binding.tab.getTabAt(0).setIcon(ContextCompat.getDrawable(act, R.drawable.ic_tab_mobile_indicator));
        binding.tab.getTabAt(1).setIcon(ContextCompat.getDrawable(act, R.drawable.ic_tab_indicator_email));

        binding.tab.getTabAt(0).getIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
        binding.tab.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.colorToolbarHeader), PorterDuff.Mode.SRC_IN);

        // setupTabIcons();
        binding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {

                    case 0:
                        binding.tab.getTabAt(0).getIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
                        binding.tab.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.colorToolbarHeader), PorterDuff.Mode.SRC_IN);

                        break;
                    case 1:
                        binding.tab.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.colorToolbarHeader), PorterDuff.Mode.SRC_IN);
                        binding.tab.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
                        break;
                }
                binding.tab.invalidate();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void changeTheme() {
        binding.indicator.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.backgroundLayar2.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.signupTxtTXT.setTextColor((Color.parseColor(new PREF(act).getThemeColor())));
        ImageView logo = act.findViewById(R.id.logo);
        VectorChildFinder vector = new VectorChildFinder(act, R.drawable.ic_logo_green, logo);
        VectorDrawableCompat.VFullPath path1 = vector.findPathByName("background");
        path1.setFillColor(Color.parseColor(new PREF(act).getThemeColor()));
        logo.invalidate();

        binding.label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.signupTxtTXT.performClick();

            }
        });
        binding.signupTxtTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSomeActivityForResult();
            }
        });

    }


    public void openSomeActivityForResult() {
        HELPER.SIMPLE_ROUTE(act, SignupActivity.class);
    }

    @Override
    public void update(Observable observable, Object data) {
        super.update(observable, data);
        if (frimline.getObserver().getValue() == ObserverActionID.LOGIN) {
            pendingBackPress = true;
        }
    }
}