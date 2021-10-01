package com.app.frimline.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.app.frimline.Common.PREF;
import com.app.frimline.R;

public class RightFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_right, container, false);
        RelativeLayout cartBackgroundLayer = view.findViewById(R.id.cartBackgroundLayar);
        cartBackgroundLayer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(getActivity()).getThemeColor())));
        return view;
    }
}