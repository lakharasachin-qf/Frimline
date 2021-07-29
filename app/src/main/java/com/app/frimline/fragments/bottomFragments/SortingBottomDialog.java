package com.app.frimline.fragments.bottomFragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.Common.FRIMLINE;
import com.app.frimline.Common.ObserverActionID;
import com.app.frimline.R;
import com.app.frimline.adapters.SortingAdapter;
import com.app.frimline.databinding.FragmentSortingDialogBinding;
import com.app.frimline.fragments.BaseFragment;
import com.app.frimline.models.ListModel;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SortingBottomDialog extends BaseFragment {
    ArrayList<ListModel> listModels;
    private SortingAdapter adpt;
    private Activity act;
    private View view;

    private FragmentSortingDialogBinding binding;
    private int calledFlag;
    private SortingBottomDialog  bottomDialog =this;

    public SortingBottomDialog() {

    }


    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sorting_dialog, parent, false);
        view = binding.getRoot();
        act = getActivity();
        fillSortingData();
        return view;
    }

    public void fillSortingData() {
        listModels = new ArrayList<>();

        ListModel countryModel = new ListModel();
        countryModel.setName("Sort by popularity");
        listModels.add(countryModel);
        countryModel = new ListModel();
        countryModel.setName("Sort by average rating");
        listModels.add(countryModel);
        countryModel = new ListModel();
        countryModel.setName("Sort by latest");
        listModels.add(countryModel);
        countryModel = new ListModel();
        countryModel.setName("Sort by price : Low to High");
        listModels.add(countryModel);
        countryModel = new ListModel();
        countryModel.setName("Sort by price : High to Low");
        listModels.add(countryModel);

        binding.titleText.setText("Sort By");
        if (listModels != null) {
            adpt = new SortingAdapter(listModels, act, calledFlag);
            SortingAdapter.setOnCheckedRadioListener radioListener = new SortingAdapter.setOnCheckedRadioListener() {
                @Override
                public void onOptionSelect(ListModel listModel, int position) {

                }
            };
            adpt.setRadioListener(radioListener);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act);
            binding.recyclerList.setLayoutManager(mLayoutManager);
            binding.recyclerList.setItemAnimator(new DefaultItemAnimator());
            binding.recyclerList.setAdapter(adpt);
        }

//        binding.closeView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.CLOSE_SORT_FILTER_VIEW);
//            }
//        });

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
