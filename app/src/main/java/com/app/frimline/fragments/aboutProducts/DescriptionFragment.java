package com.app.frimline.fragments.aboutProducts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.frimline.R;
import com.app.frimline.databinding.FragmentCenterBinding;

public class DescriptionFragment extends Fragment {

    private FragmentCenterBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_description,container,false);

        return binding.getRoot();
    }
}