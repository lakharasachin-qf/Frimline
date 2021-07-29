package com.app.frimline.Common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.app.frimline.R;
import com.app.frimline.databinding.FragmentCategoryRootBinding;
import com.app.frimline.databinding.FragmentHomeBinding;
import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class HELPER {

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
    }


    //Home Fragment
    public static void changeThemeHomeFragment(FragmentHomeBinding binding, String primaryColor) {
        // binding.headingUnderlineView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(primaryColor)));
    }


    //hide status bar in android 11 or API 30
    public static void hideStatusBarAPI30(Activity act) {
        if (Build.VERSION.SDK_INT >= 30) {
            Log.e("Android ", "11");
            act.getWindow().getDecorView().getWindowInsetsController().hide(android.view.WindowInsets.Type.statusBars());

        }
    }

    public static void showStatusBarAPI30(Activity act) {
        if (Build.VERSION.SDK_INT >= 30) {
            Log.e("Android ", "11");
            act.getWindow().getDecorView().getWindowInsetsController().hide(android.view.WindowInsets.Type.statusBars());
            // getWindow().setDecorFitsSystemWindows(false);
//            ViewCompat.setOnApplyWindowInsetsListener(binding.rootBackgroun, new OnApplyWindowInsetsListener() {
//                @Override
//                public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
//                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
//                    layoutParams.topMargin =   insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
//                    layoutParams.bottomMargin =   insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
//                    v.setLayoutParams(layoutParams);
//                    return null;
//                }
//            });
        }
    }


    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
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
//        editText.setOnTouchListener(new View.OnTouchListener() {
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public boolean onTouch(@SuppressLint("ClickableViewAccessibility") View v, MotionEvent event) {
//                // TODO Auto-generated method stub
//
//                editText.getParent().requestDisallowInterceptTouchEvent(true);
//                switch (event.getAction() & MotionEvent.ACTION_MASK) {
//                    case MotionEvent.ACTION_UP:
//                        editText.getParent().requestDisallowInterceptTouchEvent(false);
//                        break;
//
//                }
//                return false;
//            }
//        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    int scrollTo = ((View) v.getParent().getParent()).getBottom() + v.getBottom();

                    scrollView.smoothScrollTo(0, scrollTo);
                    // scrollView.scrollTo(editText.getLeft(), editText.getBottom());
                }
            }
        });
    }

    public static void FOCUS_HELPER(NestedScrollView scrollView, TextInputEditText editText, TextInputLayout nameEdtLayout) {
//        editText.setOnTouchListener(new View.OnTouchListener() {
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public boolean onTouch(@SuppressLint("ClickableViewAccessibility") View v, MotionEvent event) {
//                // TODO Auto-generated method stub
//
//                editText.getParent().requestDisallowInterceptTouchEvent(true);
//                switch (event.getAction() & MotionEvent.ACTION_MASK) {
//                    case MotionEvent.ACTION_UP:
//                        editText.getParent().requestDisallowInterceptTouchEvent(false);
//                        break;
//
//                }
//                return false;
//            }
//        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    int scrollTo = ((View) v.getParent().getParent()).getTop() + v.getTop();
                    scrollView.smoothScrollTo(0, scrollTo);
                    //scrollView.scro(editText.getLeft(), editText.getBottom());
                    //scrollView.smoothScrollTo(editText.getLeft(), editText.getTop());
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
//                if (!s.toString().startsWith("+91")) {
//                    editText.setText("+91 " + s);
//                    Selection.setSelection(editText.getText(), editText.getText().length());
//                }
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


    public static Dialog dialog;

    public static void showLoadingTran(Activity act) {

        if (dialog != null && dialog.isShowing())
            return;

        dialog = new Dialog(act);
        dialog.getWindow().setBackgroundDrawableResource(
                R.color.colorProgressBackground);
        dialog.setContentView(R.layout.progress_bar_layout);
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
}
