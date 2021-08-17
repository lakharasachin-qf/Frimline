package com.app.frimline.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.frimline.Common.APIs;
import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.FRIMLINE;
import com.app.frimline.Common.MySingleton;
import com.app.frimline.Common.ObserverActionID;
import com.app.frimline.Common.PREF;
import com.app.frimline.Common.ResponseHandler;
import com.app.frimline.R;
import com.app.frimline.adapters.ListAdapter;
import com.app.frimline.adapters.ShopAdapter;
import com.app.frimline.adapters.SortingAdapter;
import com.app.frimline.databinding.FragmentShopBinding;
import com.app.frimline.models.CategoryRootFragments.CategorySingleModel;
import com.app.frimline.models.DataTransferModel;
import com.app.frimline.models.HomeFragements.ProductModel;
import com.app.frimline.models.HomeModel;
import com.app.frimline.models.LAYOUT_TYPE;
import com.app.frimline.models.ListModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;


public class ShopFragment extends BaseFragment {
    private FragmentShopBinding binding;
    private ShopAdapter shopAdapter;
    private ArrayList<HomeModel> rootModel;
    private int minPricePosition = 0;
    private int maxPricePosition = 0;
    private ListModel sortingOptionSelection;
    private CategorySingleModel selectedCategory;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop, parent, false);

        if (CONSTANT.API_MODE) {
            startShimmer();
            loadShopData();

        } else {
            binding.containerRoot.setVisibility(View.VISIBLE);
            binding.shimmerViewContainer.setVisibility(View.GONE);

            ArrayList<HomeModel> arrayList = new ArrayList<>();
            HomeModel homeModel = new HomeModel();
            homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_TOP_PRODUCT);
            arrayList.add(homeModel);
            homeModel = new HomeModel();
            homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_FILTER_CHIP);
            arrayList.add(homeModel);
            homeModel = new HomeModel();
            homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_HOT_PRODUCT);
            arrayList.add(homeModel);

            ShopAdapter shopAdapter = new ShopAdapter(arrayList, getActivity());
            binding.containerRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            binding.containerRecycler.setNestedScrollingEnabled(false);
            binding.containerRecycler.setAdapter(shopAdapter);

        }

        fillSortingData();
        setupFilter();

        return binding.getRoot();
    }

    public void startShimmer() {
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();
        binding.containerRoot.setVisibility(View.GONE);
        binding.emptyData.setVisibility(View.GONE);
    }

    public void stopShimmer() {
        binding.shimmerViewContainer.setVisibility(View.GONE);
        binding.shimmerViewContainer.stopShimmer();
        binding.containerRoot.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changeTheme();
    }

    public void changeTheme() {

        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(getActivity()).getThemeColor())));
        binding.filterChip.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor(new PREF(getActivity()).getThemeColor())));
        binding.sortFilterAction.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor(new PREF(getActivity()).getThemeColor())));
        binding.filterChip.setChipIconTint(ColorStateList.valueOf(Color.parseColor(new PREF(getActivity()).getThemeColor())));
        binding.sortFilterAction.setChipIconTint(ColorStateList.valueOf(Color.parseColor(new PREF(getActivity()).getThemeColor())));
    }


    public void setupFilter() {
        binding.sortFilterAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideToLeftFilter(binding.filterFrameLayout);
            }
        });
        binding.filterChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideToLeft(binding.frameLayout);
            }
        });


        binding.transparentOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.filterFrameLayout.getVisibility() == View.VISIBLE) {
                    slideTorightFilter(binding.filterFrameLayout);
                }
                if (binding.frameLayout.getVisibility() == View.VISIBLE) {
                    slideToright(binding.frameLayout);
                }
            }
        });

    }

    public void slideToLeft(View view) {
        Animation RightSwipe = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_from_right);
        view.startAnimation(RightSwipe);
        view.setVisibility(View.VISIBLE);
        binding.transparentOverlay.setVisibility(View.VISIBLE);
        binding.frameLayout.setVisibility(View.VISIBLE);
        binding.container.setEnabled(false);
    }

    public void slideToLeftFilter(View view) {
        Animation RightSwipe = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_from_right);
        view.startAnimation(RightSwipe);
        view.setVisibility(View.VISIBLE);
        binding.transparentOverlay.setVisibility(View.VISIBLE);
        binding.filterFrameLayout.setVisibility(View.VISIBLE);
        binding.container.setEnabled(false);
    }


    public void slideToright(View view) {
        Animation RightSwipe = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_right_out);
        view.startAnimation(RightSwipe);
        binding.transparentOverlay.setVisibility(View.GONE);
        binding.frameLayout.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        binding.container.setEnabled(true);
    }

    public void slideTorightFilter(View view) {
        Animation RightSwipe = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_right_out);
        view.startAnimation(RightSwipe);
        binding.transparentOverlay.setVisibility(View.GONE);
        binding.filterFrameLayout.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        binding.container.setEnabled(true);
    }


    ArrayList<ListModel> listModels;
    private SortingAdapter adpt;

    public void fillSortingData() {

        binding.titleText.setText("Sort By");
        binding.titleFilterText.setText("Filter");
        listModels = new ArrayList<>();
        if (CONSTANT.API_MODE) {
            ListModel popularityModel = new ListModel();
            popularityModel.setName("Sort by popularity");
            popularityModel.setOrderBy("popularity");
            popularityModel.setOrder(ListModel.ASC);
            listModels.add(popularityModel);

            ListModel ratingModel = new ListModel();
            ratingModel.setName("Sort by average rating");
            ratingModel.setOrderBy("rating");
            ratingModel.setOrder(ListModel.ASC);
            listModels.add(ratingModel);

            ListModel dateModel = new ListModel();
            dateModel.setName("Sort by latest");
            dateModel.setOrderBy("date");
            dateModel.setOrder(ListModel.ASC);
            listModels.add(dateModel);

            ListModel priceModelLowToHigh = new ListModel();
            priceModelLowToHigh.setName("Sort by price : Low to High");
            priceModelLowToHigh.setOrderBy("price");
            priceModelLowToHigh.setOrder(ListModel.ASC);
            listModels.add(priceModelLowToHigh);

            ListModel priceModelHighToLow = new ListModel();
            priceModelHighToLow.setName("Sort by price : High to Low");
            priceModelHighToLow.setOrderBy("price");
            priceModelHighToLow.setOrder(ListModel.DESC);
            listModels.add(priceModelHighToLow);

            if (listModels != null) {
                adpt = new SortingAdapter(listModels, act, 1);
                SortingAdapter.setOnCheckedRadioListener radioListener = new SortingAdapter.setOnCheckedRadioListener() {
                    @Override
                    public void onOptionSelect(ListModel listModel, int position) {
                        sortingOptionSelection = listModel;
                        loadShopData();
                    }
                };
                adpt.setRadioListener(radioListener);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act);
                binding.recyclerList.setLayoutManager(mLayoutManager);
                binding.recyclerList.setItemAnimator(new DefaultItemAnimator());
                binding.recyclerList.setAdapter(adpt);


            }
        } else {
            ListModel countryModel = new ListModel();
            countryModel.setName("Sort by popularity");
            listModels.add(countryModel);
            countryModel = new ListModel();
            countryModel.setName("Sort by average rating");
            listModels.add(countryModel);
            countryModel = new ListModel();
            countryModel.setName("Sort by latest");
            listModels.add(countryModel);
            countryModel = new ListModel();
            countryModel.setName("Sort by price : Low to High");
            listModels.add(countryModel);
            countryModel = new ListModel();
            countryModel.setName("Sort by price : High to Low");
            listModels.add(countryModel);

            if (listModels != null) {
                adpt = new SortingAdapter(listModels, act, 1);
                SortingAdapter.setOnCheckedRadioListener radioListener = new SortingAdapter.setOnCheckedRadioListener() {
                    @Override
                    public void onOptionSelect(ListModel listModel, int position) {

                    }
                };
                adpt.setRadioListener(radioListener);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act);
                binding.recyclerList.setLayoutManager(mLayoutManager);
                binding.recyclerList.setItemAnimator(new DefaultItemAnimator());
                binding.recyclerList.setAdapter(adpt);


            }
        }

        binding.closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.CLOSE_SORT_FILTER_VIEW);
            }
        });
        binding.closeFilterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.CLOSE_SORT_FILTER_VIEW);
            }
        });


        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int fragmentWidth = (width / 2);


        binding.frameLayout.getLayoutParams().width = fragmentWidth;
        binding.filterFrameLayout.getLayoutParams().width = fragmentWidth;


        setAdapaters();


    }

    List<String> rootPriceList = new ArrayList<>();
    List<String> minPriceList = new ArrayList<>();
    List<String> maxPriceList = new ArrayList<>();
    ListAdapter adapter2;
    ListAdapter adapter;

    @SuppressLint("ClickableViewAccessibility")
    private void setAdapaters() {
        rootPriceList = new ArrayList<>();
        minPriceList = new ArrayList<>();
        maxPriceList = new ArrayList<>();

        binding.minPrice.clearListSelection();
        binding.maxPrice.clearListSelection();

        binding.maxPrice.setText("");
        binding.minPrice.setText("");

        rootPriceList.clear();
        rootPriceList.add("100");
        rootPriceList.add("500");
        rootPriceList.add("1000");
        rootPriceList.add("1500");
        rootPriceList.add("2000");
        rootPriceList.add("2500");
        rootPriceList.add("3000");

        minPriceList = new ArrayList<>(rootPriceList);
        maxPriceList = new ArrayList<>(rootPriceList);

        adapter = new ListAdapter(getActivity(), R.layout.item_string_filter_price_layout, minPriceList);

        binding.minPrice.setThreshold(0);
        binding.minPrice.setAdapter(adapter);

        adapter2 = new ListAdapter(getActivity(), R.layout.item_string_filter_price_layout, maxPriceList);
        binding.maxPrice.setThreshold(0);
        binding.maxPrice.setAdapter(adapter2);


        binding.minPrice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                minPricePosition = position;
                adapter2.updateReceiptsList(rootPriceList.subList(position, rootPriceList.size()));
            }
        });

        binding.maxPrice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                maxPricePosition = position;
                FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.PRICE_FILTER);
            }
        });

        binding.filterFrameLayout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        binding.frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }

    private boolean isLoading = false;

    private void loadShopData() {

        if (!isLoading)
            isLoading = true;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.SHOP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);

                stopShimmer();
                isLoading = false;
                rootModel = ResponseHandler.handleShopFragmentData(response);
                shopAdapter = new ShopAdapter(rootModel, getActivity());

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
                binding.containerRecycler.setHasFixedSize(true);
                binding.containerRecycler.setNestedScrollingEnabled(false);
                binding.containerRecycler.setLayoutManager(mLayoutManager);
                binding.containerRecycler.setAdapter(shopAdapter);

                if (rootModel.size() == 0) {
                    binding.containerRoot.setVisibility(View.GONE);
                    binding.shimmerViewContainer.setVisibility(View.GONE);
                    binding.emptyData.setVisibility(View.GONE);
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
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("min_price", "");
                params.put("max_price", "");
                if (sortingOptionSelection != null) {
                    params.put("orderby", sortingOptionSelection.getOrderBy());
                    params.put("order", sortingOptionSelection.getOrder());
                }

                if (selectedCategory != null)
                    params.put("category", selectedCategory.getCategoryId());

                Log.e("Param", params.toString());
                return params;
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    @Override
    public void update(Observable observable, Object data) {
        super.update(observable, data);
        if (frimline.getObserver().getValue() == ObserverActionID.APPLY_SORT_SELECTION) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (frimline.getObserver().getData() != null && !frimline.getObserver().getData().isEmpty()) {
                        sortingOptionSelection = gson.fromJson(frimline.getObserver().getData(), ListModel.class);
                        startShimmer();
                        loadShopData();
                    }
                    if (binding.filterFrameLayout.getVisibility() == View.VISIBLE) {
                        slideTorightFilter(binding.filterFrameLayout);
                    }
                    if (binding.frameLayout.getVisibility() == View.VISIBLE) {
                        slideToright(binding.frameLayout);
                    }
                }
            });
        } else if (frimline.getObserver().getValue() == ObserverActionID.CLOSE_SORT_FILTER_VIEW) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (binding.filterFrameLayout.getVisibility() == View.VISIBLE) {
                        slideTorightFilter(binding.filterFrameLayout);
                    }
                    if (binding.frameLayout.getVisibility() == View.VISIBLE) {
                        slideToright(binding.frameLayout);
                    }
                }
            });
        }
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (frimline.getObserver().getValue() == ObserverActionID.SHOP_ADDED_TO_CART) {
                    if (shopAdapter != null) {
                        relaodData(frimline.getObserver().getModel(), true);
                    }
                }
                if (frimline.getObserver().getValue() == ObserverActionID.SHOP_REMOVE_FROM_CART) {
                    if (shopAdapter != null) {
                        relaodData(frimline.getObserver().getModel(), false);
                    }
                }
                if (frimline.getObserver().getValue() == ObserverActionID.PRICE_FILTER) {
                    if (binding.filterFrameLayout.getVisibility() == View.VISIBLE) {
                        slideTorightFilter(binding.filterFrameLayout);
                    }
                    if (binding.frameLayout.getVisibility() == View.VISIBLE) {
                        slideToright(binding.frameLayout);
                    }
                    startShimmer();
                    loadShopData();
                }

                if (frimline.getObserver().getValue() == ObserverActionID.CATEGORY_FILTER) {
                    if (frimline.getObserver().getData() != null && !frimline.getObserver().getData().isEmpty()) {
                        selectedCategory = gson.fromJson(frimline.getObserver().getData(), CategorySingleModel.class);
                        startShimmer();
                        loadShopData();
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
        ProductModel dashBoardItemList = model.getApiProductModel().get(adapterPosition);
        dashBoardItemList.setAddedToCart(addOrNot);
        int refreshingPost = 0;
        for (int i = 0; i < rootModel.size(); i++) {
            if (rootModel.get(i).getLayoutType() == layoutType) {
                refreshingPost = i;
                break;
            }
        }

        shopAdapter.notifyItemChanged(itemPosition);

    }

}