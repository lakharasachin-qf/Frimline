package com.app.frimline.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.app.frimline.CategoryLandingActivity;
import com.app.frimline.CategoryRootActivity;
import com.app.frimline.R;


public class CategoryRootFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_category_root, container, false);
        LinearLayout linearLayout = view.findViewById(R.id.cat1);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryLandingActivity.class);
                startActivity(intent);
            }
        });
        return  view;
    }
}