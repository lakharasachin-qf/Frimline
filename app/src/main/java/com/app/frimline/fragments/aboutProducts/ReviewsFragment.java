package com.app.frimline.fragments.aboutProducts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.frimline.R;
import com.app.frimline.adapters.ProductReviewAdapter;
import com.app.frimline.databinding.FragmentReviewsBinding;
import com.app.frimline.models.OutCategoryModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ReviewsFragment extends Fragment {

    private FragmentReviewsBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reviews, container, false);
        ArrayList<OutCategoryModel> modelArrayList = new ArrayList<>();
        OutCategoryModel outCategoryModel = new OutCategoryModel();
        outCategoryModel.setName("");
        modelArrayList.add(outCategoryModel);
        modelArrayList.add(outCategoryModel);
        modelArrayList.add(outCategoryModel);
        modelArrayList.add(outCategoryModel);
        modelArrayList.add(outCategoryModel);
        modelArrayList.add(outCategoryModel);
        ProductReviewAdapter adaptertop = new ProductReviewAdapter(modelArrayList, getActivity());
        binding.reviewContainerRecycler.setNestedScrollingEnabled(false);
        binding.reviewContainerRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.reviewContainerRecycler.setAdapter(adaptertop);
        return binding.getRoot();
    }
}