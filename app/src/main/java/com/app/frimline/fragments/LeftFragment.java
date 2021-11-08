package com.app.frimline.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.app.frimline.common.PREF;
import com.app.frimline.R;


public class LeftFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_left, container, false);
        ProgressBar progressBar1 = view.findViewById(R.id.progressBar1);
        progressBar1.getIndeterminateDrawable().setColorFilter(Color.parseColor(new PREF(getActivity()).getThemeColor()), android.graphics.PorterDuff.Mode.MULTIPLY);
        return view;
    }
}