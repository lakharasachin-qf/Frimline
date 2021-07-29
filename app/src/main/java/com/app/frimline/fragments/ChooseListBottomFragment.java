package com.app.frimline.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.Common.HELPER;
import com.app.frimline.R;
import com.app.frimline.adapters.CountryChooseAdapter;
import com.app.frimline.databinding.FragmentListBottomListBinding;
import com.app.frimline.models.CountryModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class ChooseListBottomFragment extends BottomSheetDialogFragment {
    ArrayList<CountryModel> listModels;
    private Activity act;
    private View view;
    private CountryChooseAdapter adpt;
    private FragmentListBottomListBinding binding;
    private int calledFlag;


    public ChooseListBottomFragment() {

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            HELPER.setWhiteNavigationBar(dialog, getActivity());
        }
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_bottom_list, container, false);
        view = binding.getRoot();
        act = getActivity();
        listModels=new ArrayList<>();

        CountryModel countryModel = new CountryModel();
        countryModel.setName("India");
        listModels.add(countryModel);

        countryModel = new CountryModel();
        countryModel.setName("USA");
        listModels.add(countryModel);
        ;
        binding.titleText.setText("Choose Country");
        if (listModels != null) {
            Log.e("SSSS", String.valueOf(listModels.size()));
            adpt = new CountryChooseAdapter(listModels, act, calledFlag);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act);
            binding.recyclerList.setLayoutManager(mLayoutManager);
            binding.recyclerList.setItemAnimator(new DefaultItemAnimator());
            binding.recyclerList.setAdapter(adpt);
        }


        binding.recyclerList.requestFocus();

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }



}
