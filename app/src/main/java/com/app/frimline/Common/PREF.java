package com.app.frimline.Common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.app.frimline.models.ProfileModel;
import com.google.gson.Gson;

public class PREF {
    private static final String PREF_NAME = "frimline";
    private static final String USER_TOKEN = "user_token";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    @SuppressLint("CommitPrefEdits")
    public PREF(Context context) {
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }

    public void Logout() {
        String themeColor = getThemeColor();
        String catColor = getCategoryColor();
        editor.clear();
        editor.commit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, false);
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


    public void setLogin(boolean isLogin) {
        pref.edit().putBoolean("isLogin", isLogin).apply();
    }

    public boolean isLogin() {
        return pref.getBoolean("isLogin", false);
    }

    public void setUserToken(String token) {
        pref.edit().putString("token", token).apply();
    }

    public String getToken() {
        return pref.getString("token", "");
    }

    public void setUser(ProfileModel token) {
        pref.edit().putString("user", new Gson().toJson(token)).apply();
    }

    public ProfileModel getUser() {
        return new Gson().fromJson(pref.getString("user", null), ProfileModel.class);
    }


}
