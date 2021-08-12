package com.app.frimline.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.frimline.Common.APIs;
import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.MySingleton;
import com.app.frimline.Common.ResponseHandler;
import com.app.frimline.R;
import com.app.frimline.adapters.OrderHistoryAdapter;
import com.app.frimline.databinding.FragmentOrderHistoryBinding;
import com.app.frimline.models.OrderModel;
import com.app.frimline.models.OutCategoryModel;
import com.app.frimline.screens.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderHistoryFragment extends BaseFragment {

    private FragmentOrderHistoryBinding binding;


    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_history, parent, false);

        if (CONSTANT.API_MODE) {
            binding.shimmerViewContainer.setVisibility(View.VISIBLE);
            binding.orderHistoryRecycler.setVisibility(View.GONE);
            if (pref.isLogin()) {
                loadProfile();
            } else {
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.orderHistoryRecycler.setVisibility(View.GONE);
                binding.NoDataFound.setVisibility(View.VISIBLE);
                binding.errorText.setText("You are not Signed In.");

            }
        } else {
            setAdapterForOurProduct();
        }

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(getActivity(), LoginActivity.class);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProfile();
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
        binding.orderHistoryRecycler.setVisibility(View.GONE);
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.ORDER_HISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoading = false;
                Log.e("Response", response);
                stopShimmer();
                JSONArray object = null;
                try {
                    object = new JSONArray(response);
                    arrayList = ResponseHandler.parseOrderHistory(response);

                    if (arrayList.size() != 0) {
                        binding.NoDataFound.setVisibility(View.GONE);
                        binding.orderHistoryRecycler.setVisibility(View.VISIBLE);
                        OrderHistoryAdapter productAdapter  = new OrderHistoryAdapter(arrayList, getActivity());
                        binding.orderHistoryRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        binding.orderHistoryRecycler.setAdapter(productAdapter);
                    } else {
                        binding.button.setVisibility(View.GONE);
                        binding.errorText.setText("No order found yet.");
                        binding.NoDataFound.setVisibility(View.VISIBLE);
                        binding.orderHistoryRecycler.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        isLoading = false;
                        stopShimmer();
                    }
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = getHeader();
                Log.e("HEADER", getHeader().toString());
                return getHeader();
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}