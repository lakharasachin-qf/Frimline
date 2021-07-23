package com.app.frimline.fragments.aboutProducts;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.frimline.R;
import com.app.frimline.databinding.FragmentCenterBinding;
import com.app.frimline.databinding.FragmentReviewsBinding;

public class ReviewsFragment extends Fragment {

    private FragmentReviewsBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_reviews,container,false);

        return binding.getRoot();
    }
}