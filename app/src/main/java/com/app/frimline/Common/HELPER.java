package com.app.frimline.Common;
import android.app.Activity;
import android.content.Intent;
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
    public static void ROUTE_ANIM(Activity act, Class routeName){
        Intent i = new Intent(act, routeName);
        act.startActivity(i);
        act.overridePendingTransition(R.anim.act_left_out, R.anim.act_right_enter);
    }
    public static void ON_BACK_PRESS(Activity act){
        act.finish();
     //   act.overridePendingTransition(R.anim.act_left_out, R.anim.act_right_enter);
    }

}
