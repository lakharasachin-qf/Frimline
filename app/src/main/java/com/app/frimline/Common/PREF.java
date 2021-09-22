package com.app.frimline.Common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.app.frimline.models.CategoryRootFragments.CategorySingleModel;
import com.app.frimline.models.HomeFragements.CouponCodeModel;
import com.app.frimline.models.ProfileModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class PREF {
    private static final String PREF_NAME = "frimline";
    private static final String USER_TOKEN = "user_token";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public PREF(Context context) {
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }

    public void Logout() {
        String themeColor = getThemeColor();
        String catColor = getCategoryColor();
        String rememberList = getRememberedList();
        editor.clear();
        editor.commit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, false);
        editor.putString("emails", rememberList);
        setConfiguration(themeColor, catColor);
        editor.commit();
        editor.apply();
    }

    public String getEMAIL_Id() {
        return pref.getString("email_id", null);
    }

    public void setEMAIL_Id(String parameters) {
        pref.edit().putString("email_id", parameters).apply();
    }


    public void setConfiguration(String themeColor, String categoryColor) {
        pref.edit().putString("themeColor", themeColor).apply();
        pref.edit().putString("categoryColor", categoryColor).apply();
    }

    public String getThemeColor() {
        return pref.getString("themeColor", "#81B533");
    }

    public String getCategoryColor() {
        return pref.getString("categoryColor", "#EF7F1B");
    }

    public boolean isLogin() {
        return pref.getBoolean("isLogin", false);
    }

    public void setLogin(boolean isLogin) {
        pref.edit().putBoolean("isLogin", isLogin).apply();
    }

    public void setUserToken(String token) {
        pref.edit().putString("token", token).apply();
    }

    public String getToken() {
        return pref.getString("token", "");
    }

    public ProfileModel getUser() {
        return new Gson().fromJson(pref.getString("user", null), ProfileModel.class);
    }

    public void setUser(ProfileModel token) {
        pref.edit().putString("user", new Gson().toJson(token)).apply();
    }

    public boolean isAskOTP() {
        return pref.getBoolean("otp", true);
    }

    public void AskOTP(boolean isLogin) {
        pref.edit().putBoolean("otp", isLogin).apply();
    }


    public CategorySingleModel getCurrentCategory() {
        return new Gson().fromJson(pref.getString("currentCat", null), CategorySingleModel.class);
    }

    public void setCurrentCategory(CategorySingleModel token) {
        pref.edit().putString("currentCat", new Gson().toJson(token)).apply();
    }

    public String getPassword(String email) {
        TypeToken<HashMap<String, String>> typeToken = new TypeToken<HashMap<String, String>>() {
        };
        HashMap<String, String> rememberMeList = new Gson().fromJson(pref.getString("emails", null), typeToken.getType());
        if (rememberMeList != null)
            return rememberMeList.get(email);
        else
            return null;

    }

    public String getRememberedList() {
        return pref.getString("emails", null);
    }

    public void addRememberMe(String email, String password) {

        TypeToken<HashMap<String, String>> typeToken = new TypeToken<HashMap<String, String>>() {
        };
        HashMap<String, String> rememberMeList = new Gson().fromJson(pref.getString("emails", null), typeToken.getType());
        boolean isAlreadyExist = false;
        if (rememberMeList != null) {
            for (String key : rememberMeList.keySet()) {
                if (key.equals(email)) {
                    isAlreadyExist = true;
                    break;
                }
            }
        } else {
            rememberMeList = new HashMap<>();
        }

        // if (!isAlreadyExist) {
        rememberMeList.put(email, password);
        // }

        pref.edit().putString("emails", new Gson().toJson(rememberMeList)).apply();
    }

    public String getCouponCode() {
        return pref.getString("couponCode", "");
    }

    public void setCouponCode(String couponCode) {
        pref.edit().putString("couponCode", couponCode).apply();
    }


    public boolean displayOFFER() {
        return pref.getBoolean("offer", false);
    }

    public void setOFFER(boolean isLogin) {
        pref.edit().putBoolean("offer", isLogin).apply();
    }


}
