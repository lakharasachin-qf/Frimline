package com.app.frimline.fragments.aboutProducts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.frimline.R;
import com.app.frimline.adapters.ProductQNAAdapter;
import com.app.frimline.databinding.FragmentQNABinding;
import com.app.frimline.models.OutCategoryModel;

import java.util.ArrayList;

public class QnAFragment extends Fragment {

    private FragmentQNABinding binding;
    private  boolean isThemColor=false;

    public void setThemColor(boolean themColor) {
        isThemColor = themColor;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_q_n_a, container, false);
        ArrayList<OutCategoryModel> modelArrayList = new ArrayList<>();
        OutCategoryModel outCategoryModel = new OutCategoryModel();
        outCategoryModel.setName("");
        modelArrayList.add(outCategoryModel);
        modelArrayList.add(outCategoryModel);
        modelArrayList.add(outCategoryModel);
        modelArrayList.add(outCategoryModel);
        modelArrayList.add(outCategoryModel);
        ProductQNAAdapter adaptertop = new ProductQNAAdapter(modelArrayList, getActivity());
        adaptertop.setThemeColor(true);
        binding.qnAContainerRecycler.setNestedScrollingEnabled(false);

        binding.qnAContainerRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.qnAContainerRecycler.setAdapter(adaptertop);
        return binding.getRoot();
    }


}