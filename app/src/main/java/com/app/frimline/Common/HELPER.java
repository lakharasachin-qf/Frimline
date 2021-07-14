package com.app.frimline.Common;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.app.frimline.R;

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
}
