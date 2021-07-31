package com.app.frimline.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.frimline.Common.FRIMLINE;
import com.app.frimline.Common.ObserverActionID;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.adapters.ListAdapter;
import com.app.frimline.adapters.ShopAdapter;
import com.app.frimline.adapters.SortingAdapter;
import com.app.frimline.databinding.FragmentShopBinding;
import com.app.frimline.fragments.bottomFragments.FilterBottomDialog;
import com.app.frimline.fragments.bottomFragments.SortingBottomDialog;
import com.app.frimline.models.HomeModel;
import com.app.frimline.models.LAYOUT_TYPE;
import com.app.frimline.models.ListModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


public class ShopFragment extends BaseFragment {
    private FragmentShopBinding binding;
    private FilterBottomDialog filterBottomDialog;
    private SortingBottomDialog sortingBottomDialog;


    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop, parent, false);
        // ((ViewGroup) binding.getRoot().findViewById(R.id.containerLinear)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

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
        fillSortingData();
        setupFilter();
        return binding.getRoot();
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

    Handler handler;
    Runnable r;

    public void launch() {
        handler = new Handler();
        if (r != null)
            handler.removeCallbacks(r);

        r = new Runnable() {
            public void run() {
//
//                binding.shimmerViewContainer.stopShimmer();
//                binding.shimmerViewContainer.setVisibility(View.GONE);

            }
        };

        handler.postDelayed(r, 2000);


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
//        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
//        filterBottomDialog = new FilterBottomDialog();
//        fragmentTransaction.replace(R.id.frameLayout, filterBottomDialog);
//        fragmentTransaction.commit();


//        sortingBottomDialog = new SortingBottomDialog();
//        fragmentTransaction = getChildFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.filterFrameLayout, sortingBottomDialog);
//        fragmentTransaction.commit();

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

    @Override
    public void update(Observable observable, Object data) {
        super.update(observable, data);
        if (frimline.getObserver().getValue() == ObserverActionID.APPLY_SORT_SELECTION) {
            if (frimline.getObserver().getData() != null && !frimline.getObserver().getData().isEmpty()) {

            }
            if (binding.filterFrameLayout.getVisibility() == View.VISIBLE) {
                slideTorightFilter(binding.filterFrameLayout);
            }
            if (binding.frameLayout.getVisibility() == View.VISIBLE) {
                slideToright(binding.frameLayout);
            }
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
    }

    ArrayList<ListModel> listModels;
    private SortingAdapter adpt;

    public void fillSortingData() {

        listModels = new ArrayList<>();

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

        binding.titleText.setText("Sort By");
        binding.titleFilterText.setText("Filter");
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
        minPriceList = rootPriceList;
        maxPriceList = rootPriceList;

        adapter = new ListAdapter(getActivity(), R.layout.item_string_filter_price_layout, minPriceList);

        binding.minPrice.setThreshold(0);
        binding.minPrice.setAdapter(adapter);

        adapter2 = new ListAdapter(getActivity(), R.layout.item_string_filter_price_layout, maxPriceList);
        binding.maxPrice.setThreshold(0);
        binding.maxPrice.setAdapter(adapter2);
//        binding.minPrice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                maxPriceList = (List<String>) rootPriceList.subList(position + 1, rootPriceList.size());
//                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), R.layout.item_string_filter_price_layout, maxPriceList);
//                binding.maxPrice.setThreshold(0);
//                binding.maxPrice.setAdapter(adapter2);
//                adapter2.setNotifyOnChange(true);
//            }
//        });
//        binding.minPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                maxPriceList = (List<String>) rootPriceList.subList(position + 1, rootPriceList.size());
//                binding.maxPrice.clearListSelection();
//                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), R.layout.item_string_filter_price_layout, maxPriceList);
//                binding.maxPrice.setThreshold(0);
//                binding.maxPrice.setAdapter(adapter2);
//                adapter2.setNotifyOnChange(true);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });

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


}