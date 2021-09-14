package com.app.frimline.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.frimline.Common.APIs;
import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.MySingleton;
import com.app.frimline.Common.ObserverActionID;
import com.app.frimline.Common.PREF;
import com.app.frimline.Common.ResponseHandler;
import com.app.frimline.R;
import com.app.frimline.adapters.OrderHistoryAdapter;
import com.app.frimline.adapters.WishlistAdapter;
import com.app.frimline.databinding.FragmentWishlistBinding;
import com.app.frimline.models.OrderModel;
import com.app.frimline.screens.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class WishlistFragment extends BaseFragment {

    private FragmentWishlistBinding binding;


    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wishlist, parent, false);

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
                binding.errorText.setText("You are not Signed In.");
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

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(getActivity(), LoginActivity.class);
            }
        });
        binding.swipeContainer.setColorSchemeResources(R.color.orange, R.color.orange, R.color.orange);

        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CONSTANT.API_MODE) {
                    startShimmer();
                    loadProfile();
                } else {
                    binding.swipeContainer.setRefreshing(false);
                }
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.ORDER_HISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoading = false;
                binding.swipeContainer.setRefreshing(false);
                stopShimmer();
                JSONArray object = null;
                try {
                    object = new JSONArray(response);
                    arrayList = ResponseHandler.parseOrderHistory(response);

                    if (arrayList.size() != 0) {
                        binding.NoDataFound.setVisibility(View.GONE);
                        binding.swipeContainer.setVisibility(View.VISIBLE);
                        binding.orderHistoryRecycler.setVisibility(View.VISIBLE);
                        binding.swipeContainer.setVisibility(View.VISIBLE);
                        WishlistAdapter productAdapter = new WishlistAdapter(arrayList, getActivity());
                        binding.orderHistoryRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        binding.orderHistoryRecycler.setAdapter(productAdapter);
                        binding.orderHistoryRecycler.setHasFixedSize(true);
                    } else {
                        binding.button.setVisibility(View.GONE);
                        binding.errorText.setText("No Product found yet.");
                        binding.NoDataFound.setVisibility(View.VISIBLE);
                        binding.orderHistoryRecycler.setVisibility(View.GONE);
                        binding.swipeContainer.setVisibility(View.VISIBLE);
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

                        binding.swipeContainer.setVisibility(View.VISIBLE);
                        binding.swipeContainer.setRefreshing(false);
                        stopShimmer();
                    }
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
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

    @Override
    public void update(Observable observable, Object data) {
        super.update(observable, data);
        if (frimline.getObserver().getValue() == ObserverActionID.LOGIN) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadProfile();
                }
            });
        }
        if (frimline.getObserver().getValue() == ObserverActionID.LOGOUT) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!new PREF(act).isLogin()) {
                        binding.shimmerViewContainer.setVisibility(View.GONE);
                        binding.orderHistoryRecycler.setVisibility(View.GONE);
                        binding.swipeContainer.setVisibility(View.VISIBLE);
                        binding.NoDataFound.setVisibility(View.VISIBLE);
                        binding.errorText.setText("You are not Signed In.");
                    }
                }
            });
        }
    }
}