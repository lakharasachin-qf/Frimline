package com.app.frimline.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.adapters.CountryChooseAdapter;
import com.app.frimline.adapters.StateChooseAdapter;
import com.app.frimline.databinding.FragmentListBottomListBinding;
import com.app.frimline.models.CountryModel;
import com.app.frimline.models.StateModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class ChooseListBottomFragment extends BottomSheetDialogFragment {
    ArrayList<CountryModel> listModels;
    private View view;
    private CountryChooseAdapter adpt;
    private StateChooseAdapter stateChooseAdapter;
    private final int calledFlag;

    private final ArrayList<CountryModel> rootList;
    private final ArrayList<CountryModel> dataList;
    private final String title;
    private final ArrayList<StateModel> stateRootList;
    private final ArrayList<StateModel> stateDataList;

    public ChooseListBottomFragment(String title, ArrayList<CountryModel> countryList, int calledFlag, ArrayList<StateModel> stateModels) {
        rootList = new ArrayList<>(countryList);
        this.title = title;
        dataList = new ArrayList<>(countryList);

        stateRootList = new ArrayList<>(stateModels);
        stateDataList = new ArrayList<>(stateModels);
        this.calledFlag = calledFlag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            HELPER.setWhiteNavigationBar(dialog, getActivity());
        }
        this.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogThemeNoFloating);
        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
            setupFullHeight(bottomSheetDialog);
        });
        //BottomSheetBehavior.from(bottomSheetInternal).state = BottomSheetBehavior.STATE_EXPANDED
        return dialog;
    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        com.app.frimline.databinding.FragmentListBottomListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_bottom_list, container, false);
        view = binding.getRoot();
        Activity act = getActivity();
        binding.titleText.setText(title);
        binding.closeFilterView.setOnClickListener(v -> ChooseListBottomFragment.this.dismiss());
        if (CONSTANT.API_MODE) {
            Log.e("SSSS", String.valueOf(dataList.size()));
            if (calledFlag == CONSTANT.COUNTRY) {
                adpt = new CountryChooseAdapter(dataList, act, calledFlag);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act);
                binding.recyclerList.setLayoutManager(mLayoutManager);
                binding.recyclerList.setItemAnimator(new DefaultItemAnimator());
                binding.recyclerList.setAdapter(adpt);
                binding.recyclerList.setNestedScrollingEnabled(false);
                binding.searchEdt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        filterCountry(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            } else {
                stateChooseAdapter = new StateChooseAdapter(stateDataList, act, calledFlag);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act);
                binding.recyclerList.setLayoutManager(mLayoutManager);
                binding.recyclerList.setItemAnimator(new DefaultItemAnimator());
                binding.recyclerList.setNestedScrollingEnabled(false);
                binding.recyclerList.setAdapter(stateChooseAdapter);
                binding.searchEdt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        filter(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        } else {
            listModels = new ArrayList<>();
            CountryModel countryModel = new CountryModel();
            countryModel.setName("India");
            listModels.add(countryModel);

            countryModel = new CountryModel();
            countryModel.setName("Iran");
            listModels.add(countryModel);


            if (listModels != null) {
                Log.e("SSSS", String.valueOf(listModels.size()));
                adpt = new CountryChooseAdapter(listModels, act, calledFlag);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act);
                binding.recyclerList.setLayoutManager(mLayoutManager);
                binding.recyclerList.setItemAnimator(new DefaultItemAnimator());
                binding.recyclerList.setAdapter(adpt);
                binding.recyclerList.setNestedScrollingEnabled(false);
            }
        }

        binding.recyclerList.requestFocus();
        binding.closeFilterView.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.searchEdtLayout.setBoxStrokeColor(Color.parseColor(new PREF(act).getThemeColor()));
        return view;
    }

    void filterCountry(String text) {
        ArrayList<CountryModel> temp = new ArrayList<>();
        for (CountryModel d : rootList) {
            if (d.getName().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview
        adpt.updateList(temp);
    }

    void filter(String text) {
        ArrayList<StateModel> temp = new ArrayList<>();
        for (StateModel d : stateRootList) {

            if (d.getStateName().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview
        stateChooseAdapter.updateList(temp);
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
