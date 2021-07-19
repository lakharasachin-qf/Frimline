package com.app.frimline.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.databinding.FragmentCenterBinding;

public class CenterFragment extends Fragment {

    private FragmentCenterBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_center,container,false);
          //  binding.layout1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(getActivity()).getCategoryColor())));
            //binding.icon.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(getActivity()).getCategoryColor())));
           // binding.text.setTextColor((Color.parseColor(new PREF(getActivity()).getCategoryColor())));
        return binding.getRoot();
    }
}