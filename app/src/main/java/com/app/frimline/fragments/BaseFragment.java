package com.app.frimline.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.FRIMLINE;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.databaseHelper.CartRoomDatabase;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public abstract class BaseFragment extends Fragment implements Observer {

    public FRIMLINE frimline;
    public PREF pref;
    public Activity act;
    public Gson gson;
    public boolean PROTOTYPE_MODE = CONSTANT.PROTOTYPING_MODE;
    public boolean API_MODE = CONSTANT.API_MODE;
    public CartRoomDatabase db;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = getActivity();
        gson = new Gson();
        db = CartRoomDatabase.getAppDatabase(act);
        frimline = (FRIMLINE) act.getApplication();
        frimline.getObserver().addObserver(this);
        pref = new PREF(act);

    }

    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanseState) {
        return provideFragmentView(inflater, parent, savedInstanseState);
    }

    public abstract View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState);

    @Override
    public void update(Observable observable, Object data) {

    }

    public HashMap<String, String> getHeader() {
        HashMap<String, String> map = new HashMap<>();
        pref = new PREF(act);
        if (pref.isLogin())
            map.put("Authorization", "Bearer " + pref.getToken());

        //map.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczpcL1wvZnJpbWxpbmUucXVlcnlmaW5kZXJzLmNvbSIsImlhdCI6MTYyODYxNTEwMSwibmJmIjoxNjI4NjE1MTAxLCJleHAiOjE2MjkyMTk5MDEsImRhdGEiOnsidXNlciI6eyJpZCI6IjY2In19fQ.YpZq2d8kP3PLR5UXfEWFa4uqiNL7wQoaHENHjNuJ-98");
        HELPER.print("header",map.toString());
        return map;
    }

}
