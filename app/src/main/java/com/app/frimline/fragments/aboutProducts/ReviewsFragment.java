package com.app.frimline.fragments.aboutProducts;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
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
import com.app.frimline.adapters.ProductReviewAdapter;
import com.app.frimline.databinding.DialogAddReviewBinding;
import com.app.frimline.databinding.FragmentReviewsBinding;
import com.app.frimline.fragments.BaseFragment;
import com.app.frimline.models.CategoryRootFragments.ReviewRootModel;
import com.app.frimline.models.HomeFragements.ProductModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReviewsFragment extends BaseFragment {

    private FragmentReviewsBinding binding;
    private String defaultColor = "#EF7F1A";
    private boolean applyThemeColor = false;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle
            savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reviews, parent, false);
        binding.screenLoader.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(act, R.color.orange)));
        binding.screenLoader.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(act, R.color.orange), android.graphics.PorterDuff.Mode.MULTIPLY);
        if (act.getIntent().hasExtra("themeColor"))
            applyThemeColor = true;
        if (applyThemeColor) {
            defaultColor = pref.getThemeColor();
        } else {
            defaultColor = pref.getCategoryColor();
        }

        if (CONSTANT.API_MODE) {
            loadReview();
        } else {
            ArrayList<ReviewRootModel.Review> modelArrayList = new ArrayList<>();
            ReviewRootModel.Review outCategoryModel = new ReviewRootModel().new Review();
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
            binding.dataDisplayContainer.setVisibility(View.VISIBLE);
        }

        binding.addReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show pop up
                showAddReviewDialog();
            }
        });
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddReviewDialog();
            }
        });
        return binding.getRoot();
    }


    private boolean isLoading = false;
    private ReviewRootModel reviewRootModel;

    private void loadReview() {

        if (!isLoading)
            isLoading = true;

        binding.errorContainer.setVisibility(View.GONE);
        binding.dataDisplayContainer.setVisibility(View.GONE);
        binding.reviewContainer.setVisibility(View.GONE);

        binding.screenLoader.setVisibility(View.VISIBLE);
        ProductModel model = new Gson().fromJson(getActivity().getIntent().getStringExtra("model"), ProductModel.class);
        Log.e("Review-URL", APIs.PRODUCT_REVIEW + "9659");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.PRODUCT_REVIEW + model.getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("REVIEWS", response);
                binding.screenLoader.setVisibility(View.GONE);

                reviewRootModel = ResponseHandler.handleReviewList(response);
                if (reviewRootModel.getReviewsList() != null && reviewRootModel.getReviewsList().size() != 0) {
                    binding.reviewContainerRecycler.setVisibility(View.VISIBLE);
                    binding.reviewContainer.setVisibility(View.GONE);

                    ProductReviewAdapter adaptertop = new ProductReviewAdapter(reviewRootModel.getReviewsList(), getActivity());
                    binding.reviewContainerRecycler.setNestedScrollingEnabled(false);
                    binding.reviewContainerRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    binding.reviewContainerRecycler.setAdapter(adaptertop);
                    binding.dataDisplayContainer.setVisibility(View.VISIBLE);
                } else {
                    binding.reviewContainer.setVisibility(View.VISIBLE);
                    binding.dataDisplayContainer.setVisibility(View.GONE);
                    binding.button.setText("Add Review");
                    binding.loginHintLabel.setVisibility(View.GONE);
                    HELPER.LOAD_HTML(binding.secondaryLabel, "Be the first to <b><font color='" + defaultColor + "'>Review</font></b><br>Dente91 n-HAP Toothpaste Pack of 2");

                }

                binding.errorContainer.setVisibility(View.GONE);
                isLoading = false;
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        isLoading = false;

                        binding.errorContainer.setVisibility(View.VISIBLE);
                        binding.tryAgainBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadReview();
                            }
                        });
                    }
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }


    private AlertDialog alertDialog;
    private DialogAddReviewBinding reqBinding;

    public void showAddReviewDialog() {
        if (alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();

        reqBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_add_review, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(reqBinding.getRoot());

        alertDialog = builder.create();
        alertDialog.setContentView(reqBinding.getRoot());
        reqBinding.dialogCloseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        reqBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }
}