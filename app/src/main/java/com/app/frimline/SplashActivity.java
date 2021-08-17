package com.app.frimline;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import com.app.frimline.databaseHelper.CartRoomDatabase;
import com.app.frimline.databinding.ActivitySplashBinding;
import com.app.frimline.screens.CategoryRootActivity;

public class SplashActivity extends BaseActivity {
    private AnimatorSet animatorSet1;
    private ActivitySplashBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_splash);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        final ObjectAnimator scaleAnimatiorXX = ObjectAnimator.ofFloat(binding.logo, "scaleX", 0, 1f);
        ObjectAnimator scaleAnimatiorYX = ObjectAnimator.ofFloat(binding.logo, "scaleY", 0, 1f);
        animatorSet1 = new AnimatorSet();
        animatorSet1.playTogether(scaleAnimatiorXX, scaleAnimatiorYX);
        animatorSet1.setDuration(3000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(act, CategoryRootActivity.class);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                finish();
            }
        }, 1000);

        CartRoomDatabase cartRoomDatabase = CartRoomDatabase.getAppDatabase(this);
        Log.e("List", gson.toJson(cartRoomDatabase.productEntityDao().getAll()));
//        NfcManager manager = (NfcManager) getSystemService(Context.NFC_SERVICE);
//        NfcAdapter adapter = manager.getDefaultAdapter();
//        if (adapter != null && adapter.isEnabled()) {
//            Log.e("NFC", "YES, but enable");
//            //Yes NFC available
//        } else if (adapter != null && !adapter.isEnabled()) {
//            Log.e("NFC", "YES, but not enable");
//            //NFC is not enabled.Need to enable by the user.
//        } else {
//            //NFC is not supported
//            Log.e("NFC", "Not Support");
//        }
    }


}