package com.app.frimline.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.app.frimline.Common.FRIMLINE;
import com.app.frimline.Common.PREF;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.Observable;
import java.util.Observer;

public abstract class BaseFragment extends Fragment implements Observer {

    public FRIMLINE frimline;

    public PREF pref;
    public Activity act;

    public Gson gson;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = getActivity();
        gson = new Gson();

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


}
