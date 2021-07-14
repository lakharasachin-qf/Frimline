package com.app.frimline.screens;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.app.frimline.BaseActivity;

import com.app.frimline.Common.HELPER;
import com.app.frimline.R;

import com.app.frimline.databinding.ActivityOrderHistoryViewBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class OrderHistoryViewActivity extends BaseActivity {
    BottomSheetBehavior sheetBehavior;
    LinearLayout layoutBottomSheet;
    private ActivityOrderHistoryViewBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_order_history_view);
        setTheme(R.style.Theme_Frimline_GREENSHADE);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorGreen));
        }
        binding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.ON_BACK_PRESS(act);
            }
        });
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
    }


    public void toggleBottomSheet() {

        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);


        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


        }
    }
}