package com.app.frimline.Common;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.VectorDrawable;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.app.frimline.R;
import com.app.frimline.databinding.FragmentCategoryRootBinding;
import com.app.frimline.databinding.FragmentHomeBinding;
import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;

public class HELPER {

    /** INTENT-EVENT*/
    public static void SIMPLE_ROUTE(Activity act, Class routeName){
        Intent i = new Intent(act, routeName);
        act.startActivity(i);
        act.overridePendingTransition(R.anim.right_enter_second, R.anim.left_out_second);
    }
    public static void ROUTE(Activity act, Class routeName){
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


    //for Edittext Color Effect
    public static void SET_STYLE(Activity act,EditText editText, LinearLayout view) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    view.setBackground(ContextCompat.getDrawable(act, R.drawable.shape_editext_background_active));
                } else {
                    view.setBackground(ContextCompat.getDrawable(act, R.drawable.shape_editext_background));
                }
            }
        });
    }


    //CategoryRoot
    public static void changeTheme(Activity act,String primaryColor){
        ImageView logo=act.findViewById(R.id.logo);
        VectorChildFinder vector = new VectorChildFinder(act, R.drawable.ic_logo_green, logo);
        VectorDrawableCompat.VFullPath path1 = vector.findPathByName("background");
        path1.setFillColor(Color.parseColor(primaryColor));
        logo.invalidate();
    }
    //Category Root Fragment
    public static void changeThemeCategoryRootFragment(FragmentCategoryRootBinding binding, String primaryColor){
        binding.headingUnderlineView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(primaryColor)));
    }


    //Home Fragment
    public static void changeThemeHomeFragment(FragmentHomeBinding binding, String primaryColor){
       // binding.headingUnderlineView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(primaryColor)));
    }
}
