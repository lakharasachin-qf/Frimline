package com.app.frimline.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.app.frimline.Common.APIs;
import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.FRIMLINE;
import com.app.frimline.Common.HELPER;
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
    private ListModel sortingOptionSelection;
    private CategorySingleModel selectedCategory;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop, parent, false);

        if (CONSTANT.API_MODE) {
            startShimmer();
            loadShopData();

        } else {
            binding.swipeContainer.setVisibility(View.VISIBLE);
            binding.containerRecycler.setVisibility(View.VISIBLE);
            binding.emptyData.setVisibility(View.GONE);
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

            ShopAdapter shopAdapter = new ShopAdapter(arrayList, getActivity(), selectedCategory);
            binding.containerRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            binding.containerRecycler.setNestedScrollingEnabled(false);
            binding.containerRecycler.setAdapter(shopAdapter);

        }
        binding.swipeContainer.setColorSchemeResources(R.color.orange, R.color.orange, R.color.orange);

        binding.swipeContainer.setOnRefreshListener(() -> {
            if (CONSTANT.API_MODE) {
                startShimmer();
                loadShopData();
            } else {
                binding.swipeContainer.setRefreshing(false);
            }
        });
        fillSortingData();
        setupFilter();

        return binding.getRoot();
    }

    public void startShimmer() {
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();
        binding.swipeContainer.setVisibility(View.GONE);
        binding.filterChip.setVisibility(View.GONE);
        binding.sortFilterAction.setVisibility(View.GONE);
        binding.emptyData.setVisibility(View.GONE);
    }

    public void stopShimmer() {
        binding.filterChip.setVisibility(View.VISIBLE);
        binding.sortFilterAction.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.setVisibility(View.GONE);
        binding.shimmerViewContainer.stopShimmer();
        binding.swipeContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changeTheme();
    }

    public void changeTheme() {

        binding.button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.closeView.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.closeFilterView.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.backgroundLayar.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.filterChip.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.sortFilterAction.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.filterChip.setChipIconTint(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.sortFilterAction.setChipIconTint(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
    }


    public void setupFilter() {
        binding.sortFilterAction.setOnClickListener(v -> slideToLeftFilter(binding.filterFrameLayout));
        binding.filterChip.setOnClickListener(v -> slideToLeft(binding.frameLayout));


        binding.transparentOverlay.setOnClickListener(v -> {
            if (binding.filterFrameLayout.getVisibility() == View.VISIBLE) {
                slideRightFilter(binding.filterFrameLayout);
            }
            if (binding.frameLayout.getVisibility() == View.VISIBLE) {
                slideRight(binding.frameLayout);
            }
        });

    }

    public void slideToLeft(View view) {
        Animation RightSwipe = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_from_right);
        view.startAnimation(RightSwipe);
        view.setVisibility(View.VISIBLE);
        binding.transparentOverlay.setVisibility(View.VISIBLE);
        binding.frameLayout.setVisibility(View.VISIBLE);
        binding.swipeContainer.setEnabled(false);
    }

    public void slideToLeftFilter(View view) {
        Animation RightSwipe = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_from_right);
        view.startAnimation(RightSwipe);
        view.setVisibility(View.VISIBLE);
        binding.transparentOverlay.setVisibility(View.VISIBLE);
        binding.filterFrameLayout.setVisibility(View.VISIBLE);
        binding.swipeContainer.setEnabled(false);
    }


    public void slideRight(View view) {
        Animation RightSwipe = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_right_out);
        view.startAnimation(RightSwipe);
        binding.transparentOverlay.setVisibility(View.GONE);
        binding.frameLayout.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        binding.swipeContainer.setEnabled(true);
    }

    public void slideRightFilter(View view) {
        Animation RightSwipe = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_right_out);
        view.startAnimation(RightSwipe);
        binding.transparentOverlay.setVisibility(View.GONE);
        binding.filterFrameLayout.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        binding.swipeContainer.setEnabled(true);
    }


    ArrayList<ListModel> listModels;

    public void fillSortingData() {

        binding.titleText.setText(R.string.sort_by);
        binding.titleFilterText.setText(R.string.filter);
        listModels = new ArrayList<>();
        SortingAdapter adapt;
        if (CONSTANT.API_MODE) {
            ListModel popularityModel = new ListModel();
            popularityModel.setName("Sort by popularity");
            popularityModel.setOrderBy("popularity");
            popularityModel.setOrder(ListModel.DESC);
            listModels.add(popularityModel);

            ListModel ratingModel = new ListModel();
            ratingModel.setName("Sort by average rating");
            ratingModel.setOrderBy("rating");
            ratingModel.setOrder(ListModel.DESC);
            listModels.add(ratingModel);

            ListModel dateModel = new ListModel();
            dateModel.setName("Sort by latest");
            dateModel.setOrderBy("date");
            dateModel.setOrder(ListModel.DESC);
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
                adapt = new SortingAdapter(listModels, act, 1);
                SortingAdapter.setOnCheckedRadioListener radioListener = (listModel, position) -> {
                    sortingOptionSelection = listModel;
                    loadShopData();
                };
                adapt.setRadioListener(radioListener);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act);
                binding.recyclerList.setLayoutManager(mLayoutManager);
                binding.recyclerList.setItemAnimator(new DefaultItemAnimator());
                binding.recyclerList.setAdapter(adapt);


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
                adapt = new SortingAdapter(listModels, act, 1);
                SortingAdapter.setOnCheckedRadioListener radioListener = (listModel, position) -> {

                };
                adapt.setRadioListener(radioListener);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act);
                binding.recyclerList.setLayoutManager(mLayoutManager);
                binding.recyclerList.setItemAnimator(new DefaultItemAnimator());
                binding.recyclerList.setAdapter(adapt);


            }
        }

        binding.closeView.setOnClickListener(v -> FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.CLOSE_SORT_FILTER_VIEW));
        binding.closeFilterView.setOnClickListener(v -> FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.CLOSE_SORT_FILTER_VIEW));


        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int fragmentWidth = (width / 2);


        binding.frameLayout.getLayoutParams().width = fragmentWidth;
        binding.filterFrameLayout.getLayoutParams().width = fragmentWidth;


        setAdapter();


    }

    List<String> rootPriceList = new ArrayList<>();
    List<String> minPriceList = new ArrayList<>();
    List<String> maxPriceList = new ArrayList<>();
    ListAdapter adapter2;
    ListAdapter adapter;

    @SuppressLint("ClickableViewAccessibility")
    private void setAdapter() {
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


        binding.minPrice.setOnItemClickListener((parent, view, position, id) -> adapter2.updateReceiptsList(rootPriceList.subList(position, rootPriceList.size())));

        binding.button.setOnClickListener(v -> FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.PRICE_FILTER));

        binding.maxPrice.setOnItemClickListener((parent, view, position, id) -> {
        });

        binding.filterFrameLayout.setOnTouchListener((v, event) -> true);

        binding.frameLayout.setOnTouchListener((v, event) -> true);

    }

    private boolean isLoading = false;

    private void loadShopData() {

        if (!isLoading)
            isLoading = true;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.SHOP, response -> {

            binding.swipeContainer.setRefreshing(false);
            stopShimmer();
            isLoading = false;
            rootModel = ResponseHandler.handleShopFragmentData(response);
            shopAdapter = new ShopAdapter(rootModel, getActivity(), selectedCategory);
            shopAdapter.setCategoryFilter(selectedCategory);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
            binding.containerRecycler.setHasFixedSize(true);
            binding.containerRecycler.setNestedScrollingEnabled(false);
            binding.containerRecycler.setLayoutManager(mLayoutManager);
            binding.containerRecycler.setAdapter(shopAdapter);
            binding.containerRecycler.setVisibility(View.VISIBLE);
            binding.emptyData.setVisibility(View.GONE);
            binding.swipeContainer.setVisibility(View.VISIBLE);
            if (rootModel.size() == 0) {
                binding.containerRecycler.setVisibility(View.GONE);
                binding.swipeContainer.setVisibility(View.VISIBLE);
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.emptyData.setVisibility(View.VISIBLE);
                binding.emptyData.setText(R.string.no_product_found);
            }

        },
                error -> {
                    error.printStackTrace();
                    isLoading = false;
                    binding.swipeContainer.setVisibility(View.VISIBLE);
                    binding.emptyData.setVisibility(View.VISIBLE);
                    binding.containerRecycler.setVisibility(View.GONE);
                    binding.swipeContainer.setVisibility(View.VISIBLE);
                    binding.shimmerViewContainer.setVisibility(View.GONE);
                    stopShimmer();
                    binding.swipeContainer.setRefreshing(false);
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() {
                return new HashMap<>();
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("min_price", binding.minPrice.getText().toString());
                params.put("max_price", binding.maxPrice.getText().toString());
                if (sortingOptionSelection != null) {
                    params.put("orderby", sortingOptionSelection.getOrderBy());
                    params.put("order", sortingOptionSelection.getOrder());
                }

                if (selectedCategory != null)
                    params.put("category", selectedCategory.getCategoryId());

                return params;
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    @Override
    public void update(Observable observable, Object data) {
        super.update(observable, data);
        if (frimline.getObserver().getValue() == ObserverActionID.APPLY_SORT_SELECTION) {
            act.runOnUiThread(() -> {
                if (frimline.getObserver().getData() != null && !frimline.getObserver().getData().isEmpty()) {
                    sortingOptionSelection = gson.fromJson(frimline.getObserver().getData(), ListModel.class);
                    startShimmer();
                    loadShopData();
                }
                if (binding.filterFrameLayout.getVisibility() == View.VISIBLE) {
                    slideRightFilter(binding.filterFrameLayout);
                }
                if (binding.frameLayout.getVisibility() == View.VISIBLE) {
                    slideRight(binding.frameLayout);
                }
            });
        } else if (frimline.getObserver().getValue() == ObserverActionID.CLOSE_SORT_FILTER_VIEW) {
            act.runOnUiThread(() -> {
                if (binding.filterFrameLayout.getVisibility() == View.VISIBLE) {
                    slideRightFilter(binding.filterFrameLayout);
                }
                if (binding.frameLayout.getVisibility() == View.VISIBLE) {
                    slideRight(binding.frameLayout);
                }
            });
        }
        act.runOnUiThread(() -> {

            if (frimline.getObserver().getValue() == ObserverActionID.SHOP_ADDED_TO_CART) {
                if (shopAdapter != null) {
                    //shopAdapter.notifyDataSetChanged();
                    reloadData(frimline.getObserver().getModel(), true);
                }
            }
            if (frimline.getObserver().getValue() == ObserverActionID.SHOP_REMOVE_FROM_CART) {
                if (shopAdapter != null) {
                    //shopAdapter.notifyDataSetChanged();
                    reloadData(frimline.getObserver().getModel(), false);
                }
            }
            if (frimline.getObserver().getValue() == ObserverActionID.PRICE_FILTER) {
                if (binding.filterFrameLayout.getVisibility() == View.VISIBLE) {
                    slideRightFilter(binding.filterFrameLayout);
                }
                if (binding.frameLayout.getVisibility() == View.VISIBLE) {
                    slideRight(binding.frameLayout);
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


            if (frimline.getObserver().getValue() == ObserverActionID.CATEGORY_FILTER_REMOVE) {
                if (frimline.getObserver().getData() != null && !frimline.getObserver().getData().isEmpty()) {
                    selectedCategory = null;
                    startShimmer();
                    loadShopData();
                }
            }

            if (frimline.getObserver().getValue() == ObserverActionID.GLOBAL_CART_REFRESH) {
                if (shopAdapter != null) {
                    shopAdapter.notifyItemChanged(0);
                }
            }


        });

    }

    public void reloadData(DataTransferModel dataTransferModel, boolean addOrNot) {
        // position from 3 layout   t item
        int layoutType = Integer.parseInt(dataTransferModel.getLayoutType());//item layout 3 product or 2 product or 1 product
        int itemPosition = Integer.parseInt(dataTransferModel.getItemPosition()); // item layout position
        int adapterPosition = Integer.parseInt(dataTransferModel.getAdapterPosition()); // item layout position

        HomeModel model = rootModel.get(itemPosition);
        ProductModel dashBoardItemList = model.getApiProductModel().get(adapterPosition);
        dashBoardItemList.setAddedToCart(addOrNot);
        for (int i = 0; i < rootModel.size(); i++) {
            if (rootModel.get(i).getLayoutType() == layoutType) {
                break;
            }
        }

        shopAdapter.notifyItemChanged(itemPosition);

    }

}