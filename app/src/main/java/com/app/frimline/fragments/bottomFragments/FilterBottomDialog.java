package com.app.frimline.fragments.bottomFragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.R;
import com.app.frimline.adapters.SortingAdapter;
import com.app.frimline.databinding.FragmentFilterDialogBinding;
import com.app.frimline.fragments.BaseFragment;
import com.app.frimline.models.ListModel;

import java.util.ArrayList;

public class FilterBottomDialog extends BaseFragment {
    ArrayList<ListModel> listModels;
    private Activity act;
    private FragmentFilterDialogBinding binding;
    private int calledFlag;


    public FilterBottomDialog() {

    }


    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter_dialog, parent, false);
        View view = binding.getRoot();
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

        binding.titleText.setText("Filter");
        if (listModels != null) {
            SortingAdapter adpt = new SortingAdapter(listModels, act, calledFlag);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act);
            binding.recyclerList.setLayoutManager(mLayoutManager);
            binding.recyclerList.setItemAnimator(new DefaultItemAnimator());
            binding.recyclerList.setAdapter(adpt);
        }

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
