package com.app.frimline.fragments.aboutProducts;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.app.frimline.databinding.DialogDiscardImageBinding;
import com.app.frimline.databinding.FragmentReviewsBinding;
import com.app.frimline.fragments.BaseFragment;
import com.app.frimline.models.CategoryRootFragments.ReviewRootModel;
import com.app.frimline.models.HomeFragements.ProductModel;
import com.app.frimline.screens.LoginActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReviewsFragment extends BaseFragment {

    ProductModel model;
    private FragmentReviewsBinding binding;
    private String defaultColor = "#EF7F1A";
    private boolean applyThemeColor = false;
    private boolean isLoading = false;
    private ReviewRootModel reviewRootModel;
    private AlertDialog alertDialog;
    private DialogAddReviewBinding reqBinding;

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
            model = new Gson().fromJson(getActivity().getIntent().getStringExtra("model"), ProductModel.class);
            if (model != null) {
                loadReview();
            }
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
                binding.button.performClick();
            }
        });
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.isLogin()) {
                    showAddReviewDialog();
                }else{
                    confirmationDialog("Add Review","You must be logged in to post a review.",011);
                }
            }
        });
        changeTheme();
        return binding.getRoot();
    }

    public void changeTheme() {
        binding.screenLoader.setProgressTintList(ColorStateList.valueOf(Color.parseColor(defaultColor)));
        binding.screenLoader.getIndeterminateDrawable().setColorFilter(Color.parseColor(defaultColor), android.graphics.PorterDuff.Mode.MULTIPLY);

        binding.addReviewsIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(defaultColor)));
        binding.tryAgainBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(defaultColor)));
        binding.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(defaultColor)));

    }

    private void loadReview() {

        if (!isLoading)
            isLoading = true;

        binding.errorContainer.setVisibility(View.GONE);
        binding.dataDisplayContainer.setVisibility(View.GONE);
        binding.reviewContainer.setVisibility(View.GONE);

        binding.screenLoader.setVisibility(View.VISIBLE);

        Log.e("Review-URL", APIs.PRODUCT_REVIEW + "9659");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.PRODUCT_REVIEW + model.getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("REVIEWS", response);
                binding.screenLoader.setVisibility(View.GONE);

                reviewRootModel = ResponseHandler.handleReviewList(response);
                if (reviewRootModel.getReviewsList() != null && reviewRootModel.getReviewsList().size() != 0) {
                    binding.reviewContainerRecycler.setVisibility(View.VISIBLE);
                    ProductReviewAdapter adaptertop = new ProductReviewAdapter(reviewRootModel.getReviewsList(), getActivity());
                    binding.reviewContainerRecycler.setNestedScrollingEnabled(false);
                    binding.reviewContainerRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    binding.reviewContainerRecycler.setAdapter(adaptertop);
                    binding.dataDisplayContainer.setVisibility(View.VISIBLE);
                    binding.reviewContainer.setVisibility(View.GONE);
                    binding.errorContainer.setVisibility(View.GONE);
                } else {
                    binding.reviewContainer.setVisibility(View.VISIBLE);
                    binding.dataDisplayContainer.setVisibility(View.GONE);
                    binding.button.setText("Add Review");
                    binding.loginHintLabel.setVisibility(View.GONE);
                    HELPER.LOAD_HTML(binding.secondaryLabel, "Be the first to <b><fonts color='" + defaultColor + "'>Review</fonts></b><br>Dente91 n-HAP Toothpaste Pack of 2");

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

    public void showAddReviewDialog() {
        if (alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();

        reqBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_add_review, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(reqBinding.getRoot());
        reqBinding.dialogCloseImg.setImageTintList(ColorStateList.valueOf(Color.parseColor(defaultColor)));
        reqBinding.companyEdtLayout.setBoxStrokeColor(Color.parseColor(defaultColor));
        reqBinding.icon.setImageTintList(ColorStateList.valueOf(Color.parseColor(defaultColor)));
        reqBinding.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(defaultColor)));
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

                if (CONSTANT.API_MODE) {
                    addReview();
                } else {
                    alertDialog.dismiss();

                }
            }
        });


    }


    private void addReview() {

        if (!isLoading)
            isLoading = true;

        HELPER.showLoadingTran(act);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.ADD_REVIEW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("add-review", response);
                HELPER.dismissLoadingTran();
                isLoading = false;
                alertDialog.dismiss();
                loadReview();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        isLoading = false;
                        HELPER.dismissLoadingTran();
                        alertDialog.dismiss();
                    }
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return getHeader();
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", model.getId());
                params.put("review", reqBinding.reviewEdt.getText().toString());

                params.put("rating", String.valueOf(reqBinding.rate.getRating()));
                return params;
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public void setProductModel(ProductModel productModel) {
        this.model = productModel;
        if (this.model != null) {
            loadReview();
        }
    }


    DialogDiscardImageBinding discardImageBinding;
    public void confirmationDialog(String title, String msg, int action) {
        discardImageBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_discard_image, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(discardImageBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(discardImageBinding.getRoot());
        discardImageBinding.titleTxt.setText(title);
        discardImageBinding.subTitle.setText(msg);

        if (action == 011){
            discardImageBinding.yesTxt.setText("Sign In");
            discardImageBinding.noTxt.setText("Cancel");
            discardImageBinding.noTxt.setVisibility(View.VISIBLE);
        }
        discardImageBinding.noTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });
        discardImageBinding.yesTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (action == 011){
                    HELPER.SIMPLE_ROUTE(getActivity(), LoginActivity.class);
                }
            }
        });
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
}