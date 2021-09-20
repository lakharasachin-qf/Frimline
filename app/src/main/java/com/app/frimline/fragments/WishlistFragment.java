package com.app.frimline.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
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
import com.app.frimline.databinding.DialogDiscardImageBinding;
import com.app.frimline.databinding.FragmentWishlistBinding;
import com.app.frimline.models.HomeFragements.ProductModel;
import com.app.frimline.models.OrderModel;
import com.app.frimline.models.roomModels.WishlistEntity;
import com.app.frimline.screens.LoginActivity;
import com.app.frimline.screens.ProductDetailActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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
                wishlist();
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
                    wishlist();
                } else {
                    binding.swipeContainer.setRefreshing(false);
                }
            }
        });


        return binding.getRoot();
    }

    WishlistEntity selectedEntity;
    int selectedPosition = -1;
    WishlistAdapter.actionInterface anInterface = new WishlistAdapter.actionInterface() {
        @Override
        public void onDelete(int position, WishlistEntity entity) {
            selectedEntity = entity;
            selectedPosition = position;
            dialogDisplay("Wishlist", "Are you sure to remove product from wishlist?", "confirm");
        }

        @Override
        public void onView(int position, WishlistEntity entity) {
            selectedEntity = entity;
            selectedPosition = position;
            loadProductDetails(false);
        }

        @Override
        public void addToCart(int position, WishlistEntity entity) {
            //cartRoomDatabase.productEntityDao().insert(HELPER.convertToCartObject(productList.get(0)));
            selectedEntity = entity;
            selectedPosition = position;
            loadProductDetails(true);
        }

    };

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

    ArrayList<WishlistEntity> arrayList;
    WishlistAdapter wishlistAdapter;
    private void wishlist() {

        if (!isLoading)
            isLoading = true;
        startShimmer();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.WISHLIST, response -> {
            isLoading = false;
            binding.swipeContainer.setRefreshing(false);
            stopShimmer();

            Log.e("RESSS", response);

            arrayList = ResponseHandler.parseWishlist(response);

            if (arrayList.size() != 0) {
                binding.NoDataFound.setVisibility(View.GONE);
                binding.swipeContainer.setVisibility(View.VISIBLE);
                binding.orderHistoryRecycler.setVisibility(View.VISIBLE);
                binding.swipeContainer.setVisibility(View.VISIBLE);
                db.wishlistEntityDao().deleteWishlist();
                for (int i=0;i<arrayList.size();i++){
                    db.wishlistEntityDao().insert(arrayList.get(i));
                }
                wishlistAdapter = new WishlistAdapter(arrayList, getActivity());
                wishlistAdapter.setAnInterface(anInterface);
                binding.orderHistoryRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                binding.orderHistoryRecycler.setAdapter(wishlistAdapter);
                binding.orderHistoryRecycler.setHasFixedSize(true);
            } else {
                binding.button.setVisibility(View.GONE);
                binding.errorText.setText("No Product added to wishlist.");
                binding.NoDataFound.setVisibility(View.VISIBLE);
                binding.orderHistoryRecycler.setVisibility(View.GONE);
                binding.swipeContainer.setVisibility(View.VISIBLE);
            }

        },
                error -> {
                    error.printStackTrace();
                    binding.button.setVisibility(View.GONE);
                    binding.errorText.setText("No Product added to wishlist.");
                    binding.NoDataFound.setVisibility(View.VISIBLE);
                    binding.orderHistoryRecycler.setVisibility(View.GONE);
                    binding.swipeContainer.setVisibility(View.VISIBLE);
                    stopShimmer();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
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

    private void remove(WishlistEntity model) {
        if (isLoading)
            return;
        isLoading = true;
        HELPER.showLoadingTran(act);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.REMOVE_WISHLIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoading = false;
                HELPER.dismissLoadingTran();
                JSONObject repo = ResponseHandler.createJsonObject(response);
                if (repo != null)
                    dialogDisplay("Wishlist", ResponseHandler.getString(repo, "msg"), "success");

                if (selectedPosition != -1) {
                    db.wishlistEntityDao().deleteWishlistItem(selectedEntity.getProductId());
                    arrayList.remove(selectedEntity);
                    wishlistAdapter.notifyItemRemoved(selectedPosition);
                }
                if (arrayList.size()==0){
                    binding.button.setVisibility(View.GONE);
                    binding.errorText.setText("No Product added to wishlist.");
                    binding.NoDataFound.setVisibility(View.VISIBLE);
                    binding.orderHistoryRecycler.setVisibility(View.GONE);
                    binding.swipeContainer.setVisibility(View.VISIBLE);
                }

            }


        },
                error -> {
                    isLoading = false;
                    error.printStackTrace();
                    HELPER.dismissLoadingTran();
                    NetworkResponse response = error.networkResponse;
                    if (response != null && response.statusCode == 400) {
                        try {
                            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            JSONObject jsonObject = new JSONObject(jsonString);
                            dialogDisplay("Error", ResponseHandler.getString(jsonObject, "msg"), "error");
                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                        }


                    }
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
                Map<String, String> params = new HashMap<>();
                params.put("wishlist_id", model.getID());
                Log.e("PARAM", params.toString());
                return params;
            }
        };
        MySingleton.getInstance(act).addToRequestQueue(stringRequest);
    }


    @Override
    public void update(Observable observable, Object data) {
        super.update(observable, data);
        if (frimline.getObserver().getValue() == ObserverActionID.WISHLIST_REFRESH) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    wishlist();
                }
            });
        }
    }

    DialogDiscardImageBinding discardImageBinding;

    public void dialogDisplay(String title, String msg, String flag) {
        discardImageBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_discard_image, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(discardImageBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(discardImageBinding.getRoot());
        discardImageBinding.titleTxt.setText(title);
        discardImageBinding.subTitle.setText(msg);
        discardImageBinding.yesTxt.setText("Ok");
        //discardImageBinding.noTxt.setVisibility(View.GONE);
        discardImageBinding.noTxt.setOnClickListener(v -> {
            alertDialog.dismiss();

        });
        discardImageBinding.yesTxt.setOnClickListener(v -> {
            alertDialog.dismiss();
            if (flag.equalsIgnoreCase("confirm")) {
                remove(selectedEntity);
            }
        });
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }



    private void loadProductDetails(boolean isAddToCart) {

        if (!isLoading)
            isLoading = true;

        HELPER.showLoadingTran(act);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.PRODUCT_DETAILS + selectedEntity.getProductId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoading = false;
                HELPER.dismissLoadingTran();
                JSONObject object = ResponseHandler.createJsonObject(response);
                ProductModel productModel = ResponseHandler.getProductDetails(object);
                Log.e("print", gson.toJson(productModel));
                if (productModel != null) {
                    if (isAddToCart){
                        productModel.setQty(selectedEntity.getQuantity());
                        Toast.makeText(act, "Added to cart", Toast.LENGTH_SHORT).show();
                        db.productEntityDao().insert(HELPER.convertToCartObject(productModel));
                        HELPER.changeCartCounter(act);
                    }else {
                        Intent i = new Intent(act, ProductDetailActivity.class);
                        i.putExtra("fragment", "wishlist");
                        i.putExtra("qty", selectedEntity.getQuantity());
                        i.putExtra("productPosition", "0");
                        i.putExtra("layoutType", String.valueOf(-1));
                        i.putExtra("itemPosition", String.valueOf(-1));
                        i.putExtra("adapterPosition", String.valueOf(-1));
                        i.putExtra("model", new Gson().toJson(productModel));
                        i.putExtra("addToCartID", String.valueOf(-1));
                        i.putExtra("removeCartID", String.valueOf(-1));
                        act.startActivity(i);
                        act.overridePendingTransition(R.anim.right_enter_second, R.anim.left_out_second);
                    }
                }
            }


        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        HELPER.dismissLoadingTran();
                        isLoading = false;
                    }
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = getHeader();

                return getHeader();
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        MySingleton.getInstance(act).addToRequestQueue(stringRequest);
    }
}