package com.app.frimline.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.frimline.Common.FRIMLINE;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.MySingleton;
import com.app.frimline.R;
import com.app.frimline.adapters.ApplicationController;
import com.app.frimline.adapters.ParentHomeAdapter;
import com.app.frimline.databinding.FragmentHomeBinding;
import com.app.frimline.models.HomeModel;
import com.app.frimline.models.LAYOUT_TYPE;
import com.app.frimline.models.OutCategoryModel;
import com.app.frimline.models.ProductModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends BaseFragment {
    ParentHomeAdapter parentHomeAdapter;

    private FragmentHomeBinding binding;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        HELPER.changeThemeHomeFragment(binding, pref.getCategoryColor());

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            binding.titleTxt.setText(Html.fromHtml("<b>Due to <font color='#FF0707'>COVID-19,</font></b>", Html.FROM_HTML_MODE_COMPACT));
//        } else {
//            binding.titleTxt.setText(Html.fromHtml("<b>Due to <font color='#FF0707'>COVID-19,</font></b>"));
//        }

        binding.shimmerViewContainer.startShimmer();
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
//        setAdapterForProduct();
//        setAdapterForOurProduct();
//        setAdapterForTrendingProduct();
//        setAdapterForTopRattedProduct();
        launch();
        parentHomeAdapter = new ParentHomeAdapter(homeArray, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
        binding.containerRecycler.setHasFixedSize(true);
        binding.containerRecycler.setLayoutManager(mLayoutManager);
        binding.containerRecycler.setAdapter(parentHomeAdapter);
        loadIndexedApi();
        return binding.getRoot();
    }

    Handler handler;
    Runnable r;

    public void launch() {
        handler = new Handler();
        if (r != null)
            handler.removeCallbacks(r);

        r = new Runnable() {
            public void run() {

                binding.shimmerViewContainer.stopShimmer();
                binding.shimmerViewContainer.setVisibility(View.GONE);

            }
        };

        handler.postDelayed(r, 3000);


    }

    private static final String BANNER = "banner";
    private static final String CATEGORY_PRODUCT = "categoryProduct";
    private static final String OFFERS = "Offers";
    private static final String PROMO_CODES = "promoCodes";
    private static final String TOP_RATTED = "topRatted";
    private static final String ALERT_COVID = "alertCovid";
    private static final String CATEGORY = "category";

    public void loadIndexedApi() {

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            JSONObject subObj = new JSONObject();
            subObj.put("layout", BANNER);
            subObj.put("url", "http://queryfinders.com/brandmania/public/api/getApplicationVersion");
            subObj.put("position", "0");
            jsonArray.put(subObj);

            subObj = new JSONObject();
            subObj.put("layout", CATEGORY_PRODUCT);
            subObj.put("position", "1");
            subObj.put("url", "http://queryfinders.com/brandmania/public/api/getImageCategory?page=1");
            jsonArray.put(subObj);

            subObj = new JSONObject();
            subObj.put("layout", ALERT_COVID);
            subObj.put("position", "2");
            subObj.put("url", "http://queryfinders.com/brandmania/public/api/getApplicationVersion");
            jsonArray.put(subObj);

            subObj = new JSONObject();
            subObj.put("layout", CATEGORY);
            subObj.put("position", "3");
            subObj.put("url", "http://queryfinders.com/brandmania/public/api/getImageCategory?page=1");
            jsonArray.put(subObj);

            subObj = new JSONObject();
            subObj.put("layout", OFFERS);
            subObj.put("position", "4");
            subObj.put("url", "http://queryfinders.com/brandmania/public/api/getApplicationVersion");
            jsonArray.put(subObj);

            subObj = new JSONObject();
            subObj.put("layout", TOP_RATTED);
            subObj.put("position", "5");
            subObj.put("url", "http://queryfinders.com/brandmania/public/api/getImageCategory?page=1");
            jsonArray.put(subObj);

            subObj = new JSONObject();
            subObj.put("layout", PROMO_CODES);
            subObj.put("position", "6");
            subObj.put("url", "http://queryfinders.com/brandmania/public/api/getApplicationVersion");
            jsonArray.put(subObj);






            jsonObject.put("data", jsonArray);


            ArrayList<HomeModel> dataFromApi = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                HomeModel demo = new HomeModel();
                demo.setApiUrl(jsonArray.getJSONObject(i).getString("url"));
                demo.setLayoutName(jsonArray.getJSONObject(i).getString("layout"));
                demo.setLayoutIndex(Integer.parseInt(jsonArray.getJSONObject(i).getString("position")));
                dataFromApi.add(demo);
            }

            Collections.sort(dataFromApi, new Comparator<HomeModel>() {
                @Override
                public int compare(HomeModel lhs, HomeModel rhs) {
                    return lhs.getLayoutIndex() - rhs.getLayoutIndex();
                }
            });

            Log.e("NEW ARRAY", new Gson().toJson(dataFromApi));

            for (int i = 0; i < dataFromApi.size(); i++) {
                getUpdate(dataFromApi.get(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    ArrayList<HomeModel> homeArray = new ArrayList<>();


    public void loadBanner() {
        int lastPos = homeArray.size();
        HomeModel homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.BANNER);
        homeArray.add(homeModel);
        parentHomeAdapter.notifyItemRangeInserted(lastPos, 1);
        //parentHomeAdapter.notifyDataSetChanged();
        Log.e("loadBanner", String.valueOf(homeArray.size()));

    }

    public void loadProducts() {
        int lastPos = homeArray.size();
        HomeModel homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.CATEGORY_PRODUCT);
        homeModel.setCategoryProduct(setAdapterForProduct());
        homeArray.add(homeModel);
        parentHomeAdapter.notifyItemRangeInserted(lastPos, 1);
        //parentHomeAdapter.notifyDataSetChanged();
        Log.e("loadProducts", String.valueOf(homeArray.size()));
    }

    public void loadAlertCovid() {
        int lastPos = homeArray.size();
        HomeModel homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.ALERT_COVID);
        homeArray.add(homeModel);
        parentHomeAdapter.notifyItemRangeInserted(lastPos, 1);
        //parentHomeAdapter.notifyDataSetChanged();
        Log.e("loadAlertCovid", String.valueOf(homeArray.size()));
    }

    public void loadCategory() {
        int lastPos = homeArray.size();
        HomeModel homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.CATEGORY);
        homeModel.setProductList(setAdapterForTrendingProduct());
        homeArray.add(homeModel);
        parentHomeAdapter.notifyItemRangeInserted(lastPos, 1);
        //parentHomeAdapter.notifyDataSetChanged();
        Log.e("loadCategory", String.valueOf(homeArray.size()));
    }

    public void loadOffers() {
        int lastPos = homeArray.size();
        HomeModel homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.OFFERS);
        homeModel.setProductList(setAdapterForTrendingProduct());
        homeArray.add(homeModel);
        parentHomeAdapter.notifyItemRangeInserted(lastPos, 1);
        //parentHomeAdapter.notifyDataSetChanged();
        Log.e("loadOffers", String.valueOf(homeArray.size()));
    }

    public void loadPromoCodes() {
        int lastPos = homeArray.size();
        HomeModel homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.PROMO_CODES);
        homeArray.add(homeModel);
        parentHomeAdapter.notifyItemRangeInserted(lastPos, 1);
        //parentHomeAdapter.notifyDataSetChanged();
        Log.e("loadPromoCodes", String.valueOf(homeArray.size()));
    }

    public void loadTopRatted() {
        int lastPos = homeArray.size();
        HomeModel homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.TOP_RATTED);
        homeModel.setProductList(setAdapterForTopRattedProduct());
        homeArray.add(homeModel);
        //parentHomeAdapter.notifyDataSetChanged();
        parentHomeAdapter.notifyItemRangeInserted(lastPos, 1);
        Log.e("loadTopRatted", String.valueOf(homeArray.size()));
    }

    private void getUpdate(HomeModel homeModel) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, homeModel.getApiUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Log.e("homeModel", homeModel.getLayoutIndex() + " " + homeModel.getLayoutName());
                if (homeModel.getLayoutName().equalsIgnoreCase(BANNER)) {
                    loadBanner();
                }
                if (homeModel.getLayoutName().equalsIgnoreCase(PROMO_CODES)) {
                    loadPromoCodes();
                }
                if (homeModel.getLayoutName().equalsIgnoreCase(CATEGORY_PRODUCT)) {
                    loadProducts();
                }
                if (homeModel.getLayoutName().equalsIgnoreCase(CATEGORY)) {
                    loadCategory();
                }
                if (homeModel.getLayoutName().equalsIgnoreCase(TOP_RATTED)) {
                    loadTopRatted();
                }
                if (homeModel.getLayoutName().equalsIgnoreCase(ALERT_COVID)) {
                    loadAlertCovid();
                }
                if (homeModel.getLayoutName().equalsIgnoreCase(OFFERS)) {
                    loadOffers();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (homeModel.getLayoutName().equalsIgnoreCase(BANNER)) {
                            loadBanner();
                        }
                        if (homeModel.getLayoutName().equalsIgnoreCase(PROMO_CODES)) {
                            loadPromoCodes();
                        }
                        if (homeModel.getLayoutName().equalsIgnoreCase(CATEGORY_PRODUCT)) {
                            loadProducts();
                        }
                        if (homeModel.getLayoutName().equalsIgnoreCase(CATEGORY)) {
                            loadCategory();
                        }
                        if (homeModel.getLayoutName().equalsIgnoreCase(TOP_RATTED)) {
                            loadTopRatted();
                        }
                        if (homeModel.getLayoutName().equalsIgnoreCase(ALERT_COVID)) {
                            loadAlertCovid();
                        }
                        if (homeModel.getLayoutName().equalsIgnoreCase(OFFERS)) {
                            loadOffers();
                        }
                    }
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/x-www-form-urlencoded");//application/json
                params.put("Content-Type", "application/x-www-form-urlencoded");
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

    private void triggerApiCall() {

//        StringRequest stringRequest = new StringRequest(Request.Method.GET, "url", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        }) {
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                return super.getHeaders();
//            }
//
//            @Nullable
//            @org.jetbrains.annotations.Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                return super.getParams();
//            }
//        };
//        ApplicationController.getInstance().addToRequestQueue(stringRequest);

    }


    //
//
    public ArrayList<OutCategoryModel> setAdapterForOurProduct() {
        ArrayList<OutCategoryModel> modelArrayList = new ArrayList<>();
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
//        OurProductAdapter productAdapter = new OurProductAdapter(modelArrayList, getActivity());
//        binding.ourProductSection.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        binding.ourProductSection.setAdapter(productAdapter);
        return modelArrayList;
    }


    public ArrayList<OutCategoryModel> setAdapterForTrendingProduct() {
        ArrayList<OutCategoryModel> modelArrayList = new ArrayList<>();
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
//        TrendingProductAdapter productAdapter = new TrendingProductAdapter(modelArrayList, getActivity());
//        binding.trendingSectionRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        binding.trendingSectionRecycler.setAdapter(productAdapter);
        return modelArrayList;
    }

    public ArrayList<OutCategoryModel> setAdapterForTopRattedProduct() {
        ArrayList<OutCategoryModel> modelArrayList = new ArrayList<>();
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
        modelArrayList.add(new OutCategoryModel());
//        TopRattedProductAdapter productAdapter = new TopRattedProductAdapter(modelArrayList, getActivity());
//        binding.topRattingProductRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        binding.topRattingProductRecycler.setAdapter(productAdapter);
        return modelArrayList;
    }
//

//
    public ArrayList<HomeModel> setAdapterForProduct() {
        ArrayList<HomeModel> productArray3= new ArrayList<>();
        ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
        ProductModel productModel = new ProductModel();
        productModelArrayList.add(productModel);
        productModelArrayList.add(productModel);
        productModelArrayList.add(productModel);

        HomeModel homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_THREE_PRODUCT);
        homeModel.setProductModels(productModelArrayList);

        productArray3.add(homeModel);
        productArray3.add(homeModel);
        productArray3.add(homeModel);


//        MultiViewAdapterForHomeFragment productAdapter = new MultiViewAdapterForHomeFragment(productArray, getActivity());
//        binding.categoryProductRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        binding.categoryProductRecycler.setHasFixedSize(true);
//        SnapHelper startSnapHelper = new PagerSnapHelper();
//        binding.categoryProductRecycler.setOnFlingListener(null);
//        startSnapHelper.attachToRecyclerView(binding.categoryProductRecycler);
//        binding.categoryProductRecycler.setAdapter(productAdapter);

        return  productArray3;
    }
}