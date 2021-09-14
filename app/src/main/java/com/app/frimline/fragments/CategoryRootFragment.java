package com.app.frimline.fragments;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.frimline.Common.APIs;
import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.MySingleton;
import com.app.frimline.Common.PREF;
import com.app.frimline.Common.ResponseHandler;
import com.app.frimline.R;
import com.app.frimline.adapters.CatBannerAdapter;
import com.app.frimline.adapters.CategoryAdapter;
import com.app.frimline.adapters.TodaysTomorrowAdapter;
import com.app.frimline.databinding.DialogOfferBinding;
import com.app.frimline.databinding.FragmentCategoryRootBinding;
import com.app.frimline.models.CategoryRootFragments.CategoryRootModel;
import com.app.frimline.models.CategoryRootFragments.TodaysModel;
import com.app.frimline.models.LAYOUT_TYPE;
import com.app.frimline.screens.CategoryLandingActivity;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class CategoryRootFragment extends BaseFragment {
    private FragmentCategoryRootBinding binding;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_root, parent, false);
        ((ViewGroup) binding.getRoot().findViewById(R.id.containerLinear)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        HELPER.changeThemeCategoryRootFragment(binding, pref.getThemeColor());

        if (CONSTANT.API_MODE) {
            binding.layout1.setVisibility(View.GONE);
            binding.layout2.setVisibility(View.GONE);
            binding.layout3.setVisibility(View.GONE);
            binding.layout4.setVisibility(View.GONE);
            startShimmer();
            getTodayTomorrow();
        } else {
            binding.layout1.setVisibility(View.VISIBLE);
            binding.layout2.setVisibility(View.VISIBLE);
            binding.layout3.setVisibility(View.VISIBLE);
            binding.layout4.setVisibility(View.VISIBLE);

            binding.shimmerViewContainer.setVisibility(View.GONE);
            binding.categoryProductRecycler.setVisibility(View.VISIBLE);
            binding.scrollView.setVisibility(View.VISIBLE);
            binding.dummyContainer.setVisibility(View.VISIBLE);

            List<String> outCategoryModels = new ArrayList<>();
            outCategoryModels.add("");
            outCategoryModels.add("");
            outCategoryModels.add("");
            outCategoryModels.add("");
            CatBannerAdapter sliderAdapter = new CatBannerAdapter(outCategoryModels, getActivity());
            binding.viewPager.setAdapter(sliderAdapter);
            binding.dot.setViewPager(binding.viewPager);
            binding.dot.setSelectedDotColor(Color.parseColor(new PREF(getActivity()).getThemeColor()));

        }

        binding.cat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (API_MODE) {
                    pref.setConfiguration(pref.getThemeColor(), "#12C0DD");

                } else {
                    pref.setConfiguration("#EF7F1A", "#12C0DD");

                }
                Intent intent = new Intent(getActivity(), CategoryLandingActivity.class);
                startActivity(intent);
            }
        });
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.NoDataFound.setVisibility(View.GONE);
                startShimmer();
                getTodayTomorrow();
            }
        });
        binding.cat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (API_MODE) {
                    pref.setConfiguration(pref.getThemeColor(), "#12C0DD");
                } else {
                    pref.setConfiguration("#EF7F1A", "#12C0DD");
                    //pref.setConfiguration("#EF7F1A","#81B533");

                }
                Intent intent = new Intent(getActivity(), CategoryLandingActivity.class);
                startActivity(intent);
            }
        });
        binding.cat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (API_MODE) {
                    pref.setConfiguration(pref.getThemeColor(), "#12C0DD");
                } else {
                    pref.setConfiguration("#EF7F1A", "#12C0DD");
                    //pref.setConfiguration("#EF7F1A","#E8AE21");
                }
                Intent intent = new Intent(getActivity(), CategoryLandingActivity.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    public void startShimmer() {
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();
        binding.categoryProductRecycler.setVisibility(View.GONE);
        binding.scrollView.setVisibility(View.GONE);
    }

    public void stopShimmer() {
        binding.shimmerViewContainer.setVisibility(View.GONE);
        binding.shimmerViewContainer.stopShimmer();
        binding.scrollView.setVisibility(View.VISIBLE);
        binding.categoryProductRecycler.setVisibility(View.VISIBLE);
    }


    private CategoryRootModel rootModel;
    private boolean isLoading = false;

    private void getTodayTomorrow() {

        if (!isLoading)
            isLoading = true;
        binding.NoDataFound.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.TODAY_TOMORROW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("Response", response);
                stopShimmer();
                isLoading = false;
                binding.NoDataFound.setVisibility(View.GONE);
                rootModel = ResponseHandler.handleResponseCategoryRootFragment(response);
                loadData(rootModel);
                if (!pref.displayOFFER())
                    getOFFER();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        isLoading = false;
                        binding.NoDataFound.setVisibility(View.VISIBLE);
                        binding.shimmerViewContainer.setVisibility(View.GONE);
                        binding.shimmerViewContainer.stopShimmer();

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


    public void loadData(CategoryRootModel rootModel) {
        if (CONSTANT.API_MODE) {
            binding.layout1.setVisibility(View.GONE);
            binding.layout2.setVisibility(View.GONE);
            binding.layout3.setVisibility(View.GONE);
            binding.layout4.setVisibility(View.GONE);

            binding.todaysRecycler.setVisibility(View.VISIBLE);
            if (!rootModel.getMessages().isEmpty()) {
                TodaysTomorrowAdapter adapter = new TodaysTomorrowAdapter(prepareData(), act);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
                binding.todaysRecycler.setNestedScrollingEnabled(false);
                binding.todaysRecycler.setLayoutManager(mLayoutManager);
                binding.todaysRecycler.setAdapter(adapter);


                CategoryAdapter categoryAdapter = new CategoryAdapter(rootModel.getCategoryList(), act);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(act, RecyclerView.HORIZONTAL, false);
                binding.categoryRecycler.setNestedScrollingEnabled(false);
                binding.categoryRecycler.setLayoutManager(layoutManager);
                binding.categoryRecycler.setAdapter(categoryAdapter);
                binding.categoryRecycler.setVisibility(View.VISIBLE);
                binding.dummyContainer.setVisibility(View.GONE);
            } else {
                //show error message
            }
            loadBanner();
        }
    }

    public void loadBanner() {
        CatBannerAdapter sliderAdapter = new CatBannerAdapter(rootModel.getBannerList(), getActivity());
        binding.viewPager.setAdapter(sliderAdapter);
        binding.dot.setViewPager(binding.viewPager);
        binding.dot.setSelectedDotColor(Color.parseColor(new PREF(act).getThemeColor()));
        Timer timer;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                binding.viewPager.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.viewPager.setCurrentItem((binding.viewPager.getCurrentItem() + 1) % sliderAdapter.getCount(), true);
                    }
                });
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, 7000, 7000);
    }

    public ArrayList<TodaysModel> prepareData() {
        String message = rootModel.getMessages();

        String[] msgList = message.split("\\.");
        ArrayList<TodaysModel> list = new ArrayList<>();
        int i = 0;
        for (String str : msgList) {
            TodaysModel model = new TodaysModel();

            if (i == 0) {
                model.setLayoutType(LAYOUT_TYPE.LAYOUT_LEFT_BLOG);
            } else if (i == 1) {
                model.setLayoutType(LAYOUT_TYPE.LAYOUT_RIGHT_BLOG);
            } else {
                if ((i % 2) == 0) {
                    model.setLayoutType(LAYOUT_TYPE.LAYOUT_LEFT_BLOG);
                } else {
                    model.setLayoutType(LAYOUT_TYPE.LAYOUT_RIGHT_BLOG);
                }
            }
            str = str.trim();

            model.setMessage(str.trim() + ".");
            list.add(model);
            i++;
        }

        return list;
    }

    public void triggerPopUp(String response) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showOFFER(response);
            }
        }, 1000);

    }


    private void getOFFER() {
        if (isLoading)
            return;

        isLoading = true;

        pref.setOFFER(true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.POP_OFFER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                isLoading = false;
                triggerPopUp(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        isLoading = false;
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
    private DialogOfferBinding reqBinding;

    public void showOFFER(String response) {

        if (alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();

        reqBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_offer, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(reqBinding.getRoot());
        alertDialog = builder.create();
        alertDialog.setContentView(reqBinding.getRoot());

        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        reqBinding.closeView.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        reqBinding.closeLayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
        reqBinding.closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        JSONObject responseObj = ResponseHandler.createJsonObject(response);
        if (responseObj != null) {
            if (ResponseHandler.getString(responseObj, "status").equalsIgnoreCase("200")) {
                try {
                    if (responseObj.get("data") instanceof JSONObject) {
                        JSONObject data = ResponseHandler.getJSONObject(responseObj, "data");
                        if (data.get("options") instanceof JSONObject) {
                            Glide.with(act).load(ResponseHandler.getString(ResponseHandler.getJSONObject(data, "options"), "sgpb-image-url")).placeholder(R.drawable.ic_square_place_holder).error(R.drawable.ic_square_place_holder).into(reqBinding.offerImage);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!act.isFinishing() && !act.isDestroyed())
            alertDialog.show();
    }
}