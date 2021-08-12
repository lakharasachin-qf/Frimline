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
import com.app.frimline.adapters.ProductQNAAdapter;
import com.app.frimline.databinding.DialogAskQuestionBinding;
import com.app.frimline.databinding.FragmentQNABinding;
import com.app.frimline.fragments.BaseFragment;
import com.app.frimline.models.HomeFragements.ProductModel;
import com.app.frimline.models.QAModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QnAFragment extends BaseFragment {

    private FragmentQNABinding binding;
    private boolean isThemColor = false;

    private boolean applyThemeColor = false;
    private String defaultColor = "#EF7F1A";

    public void setThemColor(boolean themColor) {
        isThemColor = themColor;
    }

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_q_n_a, parent, false);
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
            ArrayList<QAModel> modelArrayList = new ArrayList<>();
            QAModel outCategoryModel = new QAModel();
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
        }
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddReviewDialog();
            }
        });
        binding.askNowTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddReviewDialog();
            }
        });
        changeTheme();
        return binding.getRoot();
    }

    public void changeTheme() {
        binding.screenLoader.setProgressTintList(ColorStateList.valueOf(Color.parseColor(defaultColor)));
        binding.screenLoader.getIndeterminateDrawable().setColorFilter(Color.parseColor(defaultColor), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.tryAgainBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(defaultColor)));
        binding.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(defaultColor)));
        HELPER.LOAD_HTML(binding.askNowTxt,"Do you have any question? <bold><fonts color='"+defaultColor+"'><u>Ask Now!</u></fonts></bold>");
    }


    private boolean isLoading = false;
    private QAModel reviewRootModel;
    ProductModel model;

    private void loadReview() {

        if (!isLoading)
            isLoading = true;

        binding.errorContainer.setVisibility(View.GONE);
        binding.dataDisplayContainer.setVisibility(View.GONE);


        binding.screenLoader.setVisibility(View.VISIBLE);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.PRODUCT_QA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("QA", response);
                binding.screenLoader.setVisibility(View.GONE);

                reviewRootModel = ResponseHandler.handleQAFragment(response);
                if (reviewRootModel.getBlogList() != null && reviewRootModel.getBlogList().size() != 0) {
                    ProductQNAAdapter adaptertop = new ProductQNAAdapter(reviewRootModel.getBlogList(), getActivity());
                    binding.qnAContainerRecycler.setNestedScrollingEnabled(false);
                    binding.qnAContainerRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    binding.qnAContainerRecycler.setAdapter(adaptertop);
                    binding.dataDisplayContainer.setVisibility(View.VISIBLE);
                } else {
                    binding.qaContainer.setVisibility(View.VISIBLE);
                    binding.dataDisplayContainer.setVisibility(View.GONE);
                    binding.button.setText("Ask Now!");

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

                        binding.dataDisplayContainer.setVisibility(View.GONE);
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
    private DialogAskQuestionBinding reqBinding;

    public void showAddReviewDialog() {
        if (alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();

        reqBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_ask_question, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(reqBinding.getRoot());
        reqBinding.dialogCloseImg.setImageTintList(ColorStateList.valueOf(Color.parseColor(defaultColor)));
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
                alertDialog.dismiss();
            }
        });


    }
}