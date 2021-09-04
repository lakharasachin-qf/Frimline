package com.app.frimline.fragments;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.app.frimline.Common.ResponseHandler;
import com.app.frimline.R;
import com.app.frimline.adapters.ParentHomeAdapter;
import com.app.frimline.databinding.FragmentHomeBinding;
import com.app.frimline.models.CategoryRootFragments.CategorySingleModel;
import com.app.frimline.models.DataTransferModel;
import com.app.frimline.models.HomeModel;
import com.app.frimline.models.LAYOUT_TYPE;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class HomeFragment extends BaseFragment {
    ParentHomeAdapter parentHomeAdapter;

    private FragmentHomeBinding binding;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        ((ViewGroup) binding.getRoot().findViewById(R.id.containerLinear)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        HELPER.changeThemeHomeFragment(binding, pref.getCategoryColor());
        if (API_MODE) {
            startShimmer();
            loadHomeScreen();
        } else {
            binding.shimmerViewContainer.startShimmer();
            binding.containerRecycler.setVisibility(View.VISIBLE);
            binding.shimmerViewContainer.setVisibility(View.VISIBLE);
            launch();
            parentHomeAdapter = new ParentHomeAdapter(homeArray, getActivity());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
            binding.containerRecycler.setHasFixedSize(true);
            binding.containerRecycler.setNestedScrollingEnabled(false);
            binding.containerRecycler.setLayoutManager(mLayoutManager);
            binding.containerRecycler.setAdapter(parentHomeAdapter);
            if (homeArray == null || homeArray.size() == 0)
                loadIndexedApi();
        }
        binding.swipeContainer.setColorSchemeResources(R.color.orange, R.color.orange, R.color.orange);

        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CONSTANT.API_MODE) {
                    startShimmer();
                    loadHomeScreen();
                } else {
                    binding.swipeContainer.setRefreshing(false);
                }
            }
        });
        return binding.getRoot();
    }

    public void startShimmer() {
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();
        binding.swipeContainer.setVisibility(View.GONE);
    }

    public void stopShimmer() {
        binding.shimmerViewContainer.setVisibility(View.GONE);
        binding.shimmerViewContainer.stopShimmer();
        binding.swipeContainer.setVisibility(View.VISIBLE);
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
                binding.swipeContainer.setVisibility(View.VISIBLE);

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
                //getUpdate(dataFromApi.get(i));
                if (dataFromApi.get(i).getLayoutName().equalsIgnoreCase(BANNER)) {
                    loadBanner(dataFromApi.get(i).getLayoutIndex());
                }
                if (dataFromApi.get(i).getLayoutName().equalsIgnoreCase(PROMO_CODES)) {
                    loadPromoCodes(dataFromApi.get(i).getLayoutIndex());
                }
                if (dataFromApi.get(i).getLayoutName().equalsIgnoreCase(CATEGORY_PRODUCT)) {
                    loadProducts(dataFromApi.get(i).getLayoutIndex());
                }
                if (dataFromApi.get(i).getLayoutName().equalsIgnoreCase(CATEGORY)) {
                    loadCategory(dataFromApi.get(i).getLayoutIndex());
                }
                if (dataFromApi.get(i).getLayoutName().equalsIgnoreCase(TOP_RATTED)) {
                    loadTopRatted(dataFromApi.get(i).getLayoutIndex());
                }
                if (dataFromApi.get(i).getLayoutName().equalsIgnoreCase(ALERT_COVID)) {
                    loadAlertCovid(dataFromApi.get(i).getLayoutIndex());
                }
                if (dataFromApi.get(i).getLayoutName().equalsIgnoreCase(OFFERS)) {
                    loadOffers(dataFromApi.get(i).getLayoutIndex());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    ArrayList<HomeModel> homeArray = new ArrayList<>();

    public void loadBanner(int position) {
        int lastPos = homeArray.size();

        HomeModel homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.BANNER);
        homeModel.setLayoutIndex(position);
        homeArray.add(homeModel);


        Collections.sort(homeArray, new Comparator<HomeModel>() {
            @Override
            public int compare(HomeModel lhs, HomeModel rhs) {
                return lhs.getLayoutIndex() - rhs.getLayoutIndex();
            }
        });
        parentHomeAdapter.notifyItemRangeInserted(lastPos, 1);
        //parentHomeAdapter.notifyDataSetChanged();

    }

    public void loadProducts(int position) {
        int lastPos = homeArray.size();
        HomeModel homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.CATEGORY_PRODUCT);
        homeModel.setCategoryProduct(HELPER.setAdapterForProduct());
        homeModel.setLayoutIndex(position);
        homeArray.add(homeModel);
        Collections.sort(homeArray, new Comparator<HomeModel>() {
            @Override
            public int compare(HomeModel lhs, HomeModel rhs) {
                return lhs.getLayoutIndex() - rhs.getLayoutIndex();
            }
        });
        parentHomeAdapter.notifyItemRangeInserted(lastPos, 1);
        //parentHomeAdapter.notifyDataSetChanged();

    }

    public void loadAlertCovid(int position) {
        int lastPos = homeArray.size();
        HomeModel homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.ALERT_COVID);
        homeModel.setLayoutIndex(position);
        homeArray.add(homeModel);
        Collections.sort(homeArray, new Comparator<HomeModel>() {
            @Override
            public int compare(HomeModel lhs, HomeModel rhs) {
                return lhs.getLayoutIndex() - rhs.getLayoutIndex();
            }
        });
        parentHomeAdapter.notifyItemRangeInserted(lastPos, 1);
        //parentHomeAdapter.notifyDataSetChanged();
        Log.e("loadAlertCovid", new Gson().toJson(homeArray));
    }

    public void loadCategory(int position) {
        int lastPos = homeArray.size();
        HomeModel homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.CATEGORY);
        homeModel.setLayoutIndex(position);
        homeModel.setCategoryArrayList(HELPER.setAdapterForOurProduct());
        homeArray.add(homeModel);
        Collections.sort(homeArray, new Comparator<HomeModel>() {
            @Override
            public int compare(HomeModel lhs, HomeModel rhs) {
                return lhs.getLayoutIndex() - rhs.getLayoutIndex();
            }
        });
        parentHomeAdapter.notifyItemRangeInserted(lastPos, 1);

    }

    public void loadOffers(int position) {
        int lastPos = homeArray.size();
        HomeModel homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.OFFERS);
        homeModel.setLayoutIndex(position);
        homeModel.setTradingStoriesList(HELPER.setAdapterForTrendingProduct());
        homeArray.add(homeModel);
        Collections.sort(homeArray, new Comparator<HomeModel>() {
            @Override
            public int compare(HomeModel lhs, HomeModel rhs) {
                return lhs.getLayoutIndex() - rhs.getLayoutIndex();
            }
        });
        parentHomeAdapter.notifyItemRangeInserted(lastPos, 1);

    }

    public void loadPromoCodes(int position) {
        int lastPos = homeArray.size();
        HomeModel homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.PROMO_CODES);
        homeModel.setLayoutIndex(position);
        homeArray.add(homeModel);
        Collections.sort(homeArray, new Comparator<HomeModel>() {
            @Override
            public int compare(HomeModel lhs, HomeModel rhs) {
                return lhs.getLayoutIndex() - rhs.getLayoutIndex();
            }
        });
        parentHomeAdapter.notifyItemRangeInserted(lastPos, 1);

    }

    public void loadTopRatted(int position) {
        int lastPos = homeArray.size();
        HomeModel homeModel = new HomeModel();
        homeModel.setLayoutType(LAYOUT_TYPE.TOP_RATTED);
        homeModel.setLayoutIndex(position);
        homeModel.setApiProductModel(HELPER.setAdapterForTopRattedProduct());
        homeArray.add(homeModel);
        Collections.sort(homeArray, new Comparator<HomeModel>() {
            @Override
            public int compare(HomeModel lhs, HomeModel rhs) {
                return lhs.getLayoutIndex() - rhs.getLayoutIndex();
            }
        });
        parentHomeAdapter.notifyItemRangeInserted(lastPos, 1);
    }


    private ArrayList<HomeModel> rootModel;
    private boolean isLoading = false;

    private void loadHomeScreen() {
        if (!isLoading)
            isLoading = true;
        CategorySingleModel model = new Gson().fromJson(getActivity().getIntent().getStringExtra("model"), CategorySingleModel.class);
        if (act.getIntent().hasExtra("targetCategory")) {
            model = pref.getCurrentCategory();

        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.CATEGORY_HOME + model.getCategoryId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                stopShimmer();
                isLoading = false;
                binding.swipeContainer.setRefreshing(false);
                rootModel = ResponseHandler.handleResponseCategoryHomeFragments(response);
                if (rootModel.size() != 0) {
                    parentHomeAdapter = new ParentHomeAdapter(rootModel, getActivity());
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
                    binding.containerRecycler.setHasFixedSize(true);
                    binding.containerRecycler.setNestedScrollingEnabled(false);
                    binding.containerRecycler.setLayoutManager(mLayoutManager);
                    binding.containerRecycler.setAdapter(parentHomeAdapter);
                    binding.containerRecycler.setVisibility(View.VISIBLE);
                } else {
                    binding.containerRecycler.setVisibility(View.GONE);
                    binding.emptyData.setVisibility(View.VISIBLE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        binding.swipeContainer.setRefreshing(false);
                        error.printStackTrace();
                        isLoading = false;
                        stopShimmer();
                        binding.containerRecycler.setVisibility(View.GONE);
                        binding.emptyData.setVisibility(View.VISIBLE);
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

    @Override
    public void update(Observable observable, Object data) {
        super.update(observable, data);
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (parentHomeAdapter != null) {
                    if (frimline.getObserver().getValue() == ObserverActionID.HOME_ADDED_TO_CART) {
                        if (parentHomeAdapter != null) {
                            relaodData(frimline.getObserver().getModel(), true);
                            HELPER.changeCartCounter(act);
                        }
                    }
                    if (frimline.getObserver().getValue() == ObserverActionID.HOME_REMOVE_FROM_CART) {
                        if (parentHomeAdapter != null) {
                            relaodData(frimline.getObserver().getModel(), false);
                            HELPER.changeCartCounter(act);
                        }
                    }
                    if (frimline.getObserver().getValue() == ObserverActionID.CART_COUNTER_UPDATE) {
                        int refreshingPost = 0;
                        for (int i = 0; i < rootModel.size(); i++) {
                            if (rootModel.get(i).getLayoutType() == LAYOUT_TYPE.CATEGORY_PRODUCT) {
                                refreshingPost = i;
                                break;
                            }
                        }
                        parentHomeAdapter.notifyItemChanged(refreshingPost);
                    }
                }
            }
        });

    }

    public void relaodData(DataTransferModel dataTransferModel, boolean addOrNot) {
        String productId = dataTransferModel.getProductId();
        int productPosition = Integer.parseInt(dataTransferModel.getProductPosition()); // position from 3 layout produc t item
        int layoutType = Integer.parseInt(dataTransferModel.getLayoutType());//item layout 3 product or 2 product or 1 product
        int itemPosition = Integer.parseInt(dataTransferModel.getItemPosition()); // item layout position
        int adapterPosition = Integer.parseInt(dataTransferModel.getAdapterPosition()); // item layout position

        HomeModel model = rootModel.get(itemPosition);
        HomeModel dashBoardItemList = model.getCategoryProduct().get(adapterPosition);
        com.app.frimline.models.HomeFragements.ProductModel productModel = dashBoardItemList.getApiProductModel().get(productPosition);
        productModel.setAddedToCart(addOrNot);

        int refreshingPost = 0;
        for (int i = 0; i < rootModel.size(); i++) {
            if (rootModel.get(i).getLayoutType() == LAYOUT_TYPE.CATEGORY_PRODUCT) {
                refreshingPost = i;
                break;
            }
        }

        parentHomeAdapter.notifyItemChanged(refreshingPost);

    }
}