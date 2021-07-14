package com.app.frimline.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.frimline.Common.HELPER;
import com.app.frimline.R;
import com.app.frimline.screens.BlogDetailsActivity;
import com.google.android.material.chip.Chip;

public class BlogsFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_blogs, container, false);
        RelativeLayout relativeLayout=root.findViewById(R.id.layout);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(getActivity(), BlogDetailsActivity.class);
            }
        });

        Chip exploreMore=root.findViewById(R.id.exploreMore);
        exploreMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(getActivity(),BlogDetailsActivity.class);
            }
        });

        return root;
    }
}