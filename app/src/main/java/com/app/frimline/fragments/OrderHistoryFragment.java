package com.app.frimline.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.app.frimline.common.APIs;
import com.app.frimline.common.CONSTANT;
import com.app.frimline.common.HELPER;
import com.app.frimline.common.MySingleton;
import com.app.frimline.common.ObserverActionID;
import com.app.frimline.common.PREF;
import com.app.frimline.common.ResponseHandler;
import com.app.frimline.R;
import com.app.frimline.adapters.OrderHistoryAdapter;
import com.app.frimline.databinding.FragmentOrderHistoryBinding;
import com.app.frimline.models.OrderModel;
import com.app.frimline.screens.LoginActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class OrderHistoryFragment extends BaseFragment {

    private FragmentOrderHistoryBinding binding;


    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_history, parent, false);

        if (CONSTANT.API_MODE) {
            binding.shimmerViewContainer.setVisibility(View.VISIBLE);
            binding.swipeContainer.setVisibility(View.GONE);
            if (pref.isLogin()) {
                loadProfile();
            } else {
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.orderHistoryRecycler.setVisibility(View.GONE);
                binding.swipeContainer.setVisibility(View.VISIBLE);
                binding.NoDataFound.setVisibility(View.VISIBLE);
                binding.errorText.setText(R.string.you_are_not_signed_in);

            }
        } else {
            binding.NoDataFound.setVisibility(View.GONE);
            binding.shimmerViewContainer.setVisibility(View.GONE);
            binding.swipeContainer.setVisibility(View.VISIBLE);
            binding.orderHistoryRecycler.setVisibility(View.VISIBLE);
            stopShimmer();
            setAdapterForOurProduct();
        }

        binding.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));

        binding.button.setOnClickListener(v -> HELPER.SIMPLE_ROUTE(getActivity(), LoginActivity.class));
        binding.swipeContainer.setColorSchemeResources(R.color.orange, R.color.orange, R.color.orange);

        binding.swipeContainer.setOnRefreshListener(() -> {
            if (CONSTANT.API_MODE) {
                startShimmer();
                loadProfile();
            } else {
                binding.swipeContainer.setRefreshing(false);
            }
        });
        return binding.getRoot();
    }


    public void setAdapterForOurProduct() {
        ArrayList<OrderModel> modelArrayList = new ArrayList<>();
        modelArrayList.add(new OrderModel());
        modelArrayList.add(new OrderModel());
        modelArrayList.add(new OrderModel());
        modelArrayList.add(new OrderModel());
        modelArrayList.add(new OrderModel());
        modelArrayList.add(new OrderModel());
        OrderHistoryAdapter productAdapter = new OrderHistoryAdapter(modelArrayList, getActivity());
        binding.orderHistoryRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.orderHistoryRecycler.setAdapter(productAdapter);
    }

    public void startShimmer() {
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();
        binding.swipeContainer.setVisibility(View.GONE);
        binding.NoDataFound.setVisibility(View.GONE);
    }

    public void stopShimmer() {
        binding.shimmerViewContainer.setVisibility(View.GONE);
        binding.shimmerViewContainer.stopShimmer();

    }

    private boolean isLoading = false;

    ArrayList<OrderModel> arrayList;

    private void loadProfile() {

        if (!isLoading)
            isLoading = true;
        startShimmer();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.ORDER_HISTORY, response -> {
            isLoading = false;
            binding.swipeContainer.setRefreshing(false);
            HELPER.print("Response", response);
            stopShimmer();
            arrayList = ResponseHandler.parseOrderHistory(response);

            if (arrayList.size() != 0) {
                binding.NoDataFound.setVisibility(View.GONE);
                binding.swipeContainer.setVisibility(View.VISIBLE);
                binding.orderHistoryRecycler.setVisibility(View.VISIBLE);
                binding.swipeContainer.setVisibility(View.VISIBLE);
                OrderHistoryAdapter productAdapter = new OrderHistoryAdapter(arrayList, getActivity());
                binding.orderHistoryRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                binding.orderHistoryRecycler.setAdapter(productAdapter);
                binding.orderHistoryRecycler.setHasFixedSize(true);
            } else {
                binding.button.setVisibility(View.GONE);
                binding.errorText.setText(R.string.no_order_found_yet);
                binding.NoDataFound.setVisibility(View.VISIBLE);
                binding.orderHistoryRecycler.setVisibility(View.GONE);
                binding.swipeContainer.setVisibility(View.VISIBLE);
            }


        },
                error -> {
                    error.printStackTrace();

                    binding.swipeContainer.setVisibility(View.VISIBLE);
                    binding.swipeContainer.setRefreshing(false);
                    stopShimmer();
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() {
                return getHeader();
            }

            @Override
            protected Map<String, String> getParams() {
                return new HashMap<>();
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    @Override
    public void update(Observable observable, Object data) {
        super.update(observable, data);
        if (frimline.getObserver().getValue() == ObserverActionID.LOGIN) {
            act.runOnUiThread(this::loadProfile);
        }
        if (frimline.getObserver().getValue() == ObserverActionID.LOGOUT) {
            act.runOnUiThread(() -> {
                if (!new PREF(act).isLogin()) {
                    binding.shimmerViewContainer.setVisibility(View.GONE);
                    binding.orderHistoryRecycler.setVisibility(View.GONE);
                    binding.swipeContainer.setVisibility(View.VISIBLE);
                    binding.NoDataFound.setVisibility(View.VISIBLE);
                    binding.errorText.setText(R.string.you_are_not_signed_in);
                }
            });
        }
    }
}