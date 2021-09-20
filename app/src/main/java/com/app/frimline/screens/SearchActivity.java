package com.app.frimline.screens;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.frimline.BaseActivity;
import com.app.frimline.Common.APIs;
import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.MySingleton;
import com.app.frimline.Common.ObserverActionID;
import com.app.frimline.Common.PREF;
import com.app.frimline.Common.ResponseHandler;
import com.app.frimline.R;
import com.app.frimline.adapters.SearchAdapter;
import com.app.frimline.adapters.SortingAdapter;
import com.app.frimline.databinding.ActivitySearchBinding;
import com.app.frimline.models.CategoryRootFragments.CategorySingleModel;
import com.app.frimline.models.DataTransferModel;
import com.app.frimline.models.HomeFragements.ProductModel;
import com.app.frimline.models.HomeModel;
import com.app.frimline.models.LAYOUT_TYPE;
import com.app.frimline.models.ListModel;
import com.app.frimline.models.SearchModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class SearchActivity extends BaseActivity {
    ArrayList<ListModel> listModels;
    SearchAdapter searchAdapter;
    private ActivitySearchBinding binding;
    private ArrayList<SearchModel> searchResult;
    private ListModel sortingOptionSelection;
    private CategorySingleModel selectedCategory;
    private String categoryId;
    private String jsonStr;
    private SortingAdapter adpt;
    private boolean isLoading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_search);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        binding.sortAction.setVisibility(View.GONE);
        search();
        if (savedInstanceState == null) {
            binding.rootBackground.setVisibility(View.INVISIBLE);

            final ViewTreeObserver viewTreeObserver = binding.rootBackground.getViewTreeObserver();

            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        circularRevealActivity();
                        binding.rootBackground.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                });
            }

        }

        changeStatusBarColor(ContextCompat.getColor(act, R.color.colorScreenBackground));
        binding.nameEdtLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.nameEdt.requestFocus();

            }
        });
        binding.clearAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (CONSTANT.API_MODE) {
            binding.nameEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        closeKeyboard();
                        search();
                        return true;
                    }
                    return false;
                }
            });

        } else {

            ArrayList<ProductModel> innerDataList = new ArrayList<>();
            ProductModel outCategoryModel = new ProductModel();
            outCategoryModel.setName("");
            innerDataList.add(outCategoryModel);
            outCategoryModel = new ProductModel();
            outCategoryModel.setName("");
            innerDataList.add(outCategoryModel);
            outCategoryModel = new ProductModel();
            outCategoryModel.setName("");
            innerDataList.add(outCategoryModel);


            ArrayList<HomeModel> topProdcut = new ArrayList<>();
            HomeModel homeModelTop = new HomeModel();
            homeModelTop.setName("");
            topProdcut.add(homeModelTop);
            homeModelTop = new HomeModel();
            homeModelTop.setName("");
            topProdcut.add(homeModelTop);
            homeModelTop = new HomeModel();
            homeModelTop.setName("");
            topProdcut.add(homeModelTop);

            ArrayList<CategorySingleModel> categoryList = new ArrayList<>();
            CategorySingleModel categorySingleModel = new CategorySingleModel();
            categoryList.add(categorySingleModel);
            categorySingleModel = new CategorySingleModel();
            categoryList.add(categorySingleModel);
            categorySingleModel = new CategorySingleModel();
            categoryList.add(categorySingleModel);

            ArrayList<SearchModel> arrayList = new ArrayList<>();
            SearchModel homeModel = new SearchModel();
            homeModel.setTopProduct(topProdcut);
            homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_TOP_PRODUCT);
            arrayList.add(homeModel);
            homeModel = new SearchModel();
            homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_FILTER_CHIP);
            homeModel.setCategoryList(categoryList);
            arrayList.add(homeModel);
            homeModel = new SearchModel();
            homeModel.setLayoutType(LAYOUT_TYPE.LAYOUT_HOT_PRODUCT);
            homeModel.setHotProduct(innerDataList);
            arrayList.add(homeModel);
            SearchAdapter shopFilterAdapter = new SearchAdapter(arrayList, act);
            shopFilterAdapter.setCategoryFilter(selectedCategory);
            binding.containerRecycler.setLayoutManager(new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false));
            binding.containerRecycler.setNestedScrollingEnabled(false);
            binding.containerRecycler.setAdapter(shopFilterAdapter);

        }

        fillSortingData();
        binding.sortAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideToLeftFilter(binding.filterFrameLayout);
            }
        });
        binding.filterFrameLayout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        binding.transparentOverlay.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        binding.transparentOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.filterFrameLayout.getVisibility() == View.VISIBLE) {
                    slideTorightFilter(binding.filterFrameLayout);
                }

            }
        });

        changeTheme();
    }

    public void startShimmer() {
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();
        binding.containerRecycler.setVisibility(View.GONE);
        binding.transparentOverlay.setVisibility(View.GONE);
        binding.filterFrameLayout.setVisibility(View.GONE);
        binding.emptyData.setVisibility(View.GONE);

    }

    public void stopShimmer() {
        binding.shimmerViewContainer.setVisibility(View.GONE);
        binding.shimmerViewContainer.stopShimmer();
        binding.transparentOverlay.setVisibility(View.GONE);
        binding.emptyData.setVisibility(View.GONE);
        binding.filterFrameLayout.setVisibility(View.GONE);
    }

    public void changeTheme() {
        binding.closeView.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.clearAction.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.searchIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.sortAction.setChipIconTint(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
        binding.sortAction.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor(new PREF(act).getThemeColor())));
    }

    private void circularRevealActivity() {
        int cx = binding.rootBackground.getRight() - getDips(44);
        int cy = binding.rootBackground.getTop() - getDips(44);

        float finalRadius = Math.max(binding.rootBackground.getWidth(), binding.rootBackground.getHeight());

        Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                binding.rootBackground,
                cx,
                cy,
                0,
                finalRadius);

        circularReveal.setDuration(500);
        binding.rootBackground.setVisibility(View.VISIBLE);
        circularReveal.start();

    }

    private int getDips(int dps) {
        Resources resources = getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dps,
                resources.getDisplayMetrics());
    }

    public void slideTorightFilter(View view) {
        Animation RightSwipe = AnimationUtils.loadAnimation(act, R.anim.slide_right_out);
        view.startAnimation(RightSwipe);
        binding.transparentOverlay.setVisibility(View.GONE);
        binding.filterFrameLayout.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        binding.containerRecycler.setEnabled(true);

    }

    public void slideToLeftFilter(View view) {
        Animation RightSwipe = AnimationUtils.loadAnimation(act, R.anim.slide_from_right);
        view.startAnimation(RightSwipe);
        view.setVisibility(View.VISIBLE);
        binding.transparentOverlay.setVisibility(View.VISIBLE);
        binding.filterFrameLayout.setVisibility(View.VISIBLE);
        binding.containerRecycler.setEnabled(false);

    }

    @Override
    public void onBackPressed() {
        if (binding.transparentOverlay.getVisibility() == View.VISIBLE) {
            if (binding.filterFrameLayout.getVisibility() == View.VISIBLE) {
                slideTorightFilter(binding.filterFrameLayout);
            }
            return;
        }
        int cx = binding.rootBackground.getWidth() - getDips(44);
        int cy = binding.rootBackground.getTop() - getDips(44);

        float finalRadius = Math.max(binding.rootBackground.getWidth(), binding.rootBackground.getHeight());
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(binding.rootBackground, cx, cy, finalRadius, 0);

        circularReveal.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                binding.rootBackground.setVisibility(View.INVISIBLE);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        circularReveal.setDuration(300);
        circularReveal.start();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void fillSortingData() {

        listModels = new ArrayList<>();
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

            binding.titleText.setText("Sort By");
            if (listModels != null) {
                adpt = new SortingAdapter(listModels, act, 1);
                SortingAdapter.setOnCheckedRadioListener radioListener = new SortingAdapter.setOnCheckedRadioListener() {
                    @Override
                    public void onOptionSelect(ListModel listModel, int position) {
                        sortingOptionSelection = listModel;
                        search();
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
            binding.titleText.setText("Sort By");
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
                slideTorightFilter(binding.filterFrameLayout);
                if (CONSTANT.API_MODE) {

                }
            }
        });


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int fragmentWidth = (width / 2);


        binding.filterFrameLayout.getLayoutParams().width = fragmentWidth;

        binding.filterFrameLayout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, @SuppressLint("ClickableViewAccessibility") MotionEvent event) {
                return false;
            }
        });

    }

    private void search() {
        if (!isLoading)
            isLoading = true;
        startShimmer();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.SEARCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("result", response);
                isLoading = false;
                stopShimmer();

                searchResult = ResponseHandler.parseSearch(response);
                Log.e("selectedCategory", gson.toJson(selectedCategory));
                Log.e("jsonStr", gson.toJson(jsonStr));
                if (searchResult != null && searchResult.size() != 0) {
                    binding.containerRecycler.setVisibility(View.VISIBLE);
                    searchAdapter = new SearchAdapter(searchResult, act);
                    searchAdapter.setCategoryFilter(selectedCategory);
                    binding.containerRecycler.setLayoutManager(new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false));
                    binding.containerRecycler.setNestedScrollingEnabled(false);
                    binding.containerRecycler.setAdapter(searchAdapter);
                    binding.sortAction.setVisibility(View.VISIBLE);
                } else {
                    binding.emptyData.setVisibility(View.VISIBLE);
                    binding.emptyData.setText("No Search found");
                    binding.containerRecycler.setVisibility(View.GONE);
                 //   binding.sortAction.setVisibility(View.GONE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        isLoading = false;
                        binding.emptyData.setVisibility(View.VISIBLE);
                        binding.containerRecycler.setVisibility(View.GONE);
                      //  binding.sortAction.setVisibility(View.GONE);
//                        NetworkResponse response = error.networkResponse;
//                        if (response.statusCode == 400 && response.data!=null) {
//                            try {
//
//                                String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//                                JSONObject jsonObject = new JSONObject(jsonString);
//
//                            } catch (UnsupportedEncodingException | JSONException e) {
//                                e.printStackTrace();
//                            }
//                            Log.e("Error", gson.toJson(response.headers));
//                            Log.e("allHeaders", gson.toJson(response.allHeaders));
//                        }

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
                if (sortingOptionSelection != null) {
                    params.put("orderby", sortingOptionSelection.getOrderBy());
                    params.put("order", sortingOptionSelection.getOrder());
                }
                if (selectedCategory != null)
                    params.put("category", selectedCategory.getCategoryId());
                if (!binding.nameEdt.getText().toString().trim().isEmpty())
                    params.put("search", binding.nameEdt.getText().toString());

                Log.e("PostedParam", params.toString());
                return params;
            }
        };

        MySingleton.getInstance(act).addToRequestQueue(stringRequest);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();

        if (view != null) {

            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        if (searchAdapter != null) {
            searchAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        super.update(observable, data);
        if (frimline.getObserver().getValue() == ObserverActionID.SEARCH_ADDED_TO_CART) {
            if (searchAdapter != null) {
                relaodData(frimline.getObserver().getModel(), true);

            }
        }
        if (frimline.getObserver().getValue() == ObserverActionID.SEARCH_REMOVE_FROM_CART) {
            if (searchAdapter != null) {
                relaodData(frimline.getObserver().getModel(), false);
            }
        }

        if (frimline.getObserver().getValue() == ObserverActionID.SEARCH_HOT_ADDED_TO_CART) {
            if (searchAdapter != null) {
                reloadHot(frimline.getObserver().getModel(), false);
            }
        }
        if (frimline.getObserver().getValue() == ObserverActionID.SEARCH_HOT_REMOVED_FROM_CART) {
            if (searchAdapter != null) {
                reloadHot(frimline.getObserver().getModel(), false);
            }
        }
        if (frimline.getObserver().getValue() == ObserverActionID.APPLY_SORT_SELECTION) {
            if (frimline.getObserver().getData() != null && !frimline.getObserver().getData().isEmpty()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        slideTorightFilter(binding.filterFrameLayout);
                        sortingOptionSelection = gson.fromJson(frimline.getObserver().getData(), ListModel.class);
                        search();
                    }
                });

            }
        }
        if (frimline.getObserver().getValue() == ObserverActionID.CATEGORY_FILTER) {
            if (frimline.getObserver().getData() != null && !frimline.getObserver().getData().isEmpty()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        selectedCategory = gson.fromJson(frimline.getObserver().getData(), CategorySingleModel.class);
                        jsonStr = frimline.getObserver().getData();
                        categoryId = selectedCategory.getCategoryId();
                        Log.e("selectedCategory", gson.toJson(selectedCategory));
                        Log.e("jsonStr", gson.toJson(jsonStr));
                        search();
                    }
                });

            }
        }
        if (frimline.getObserver().getValue() == ObserverActionID.CATEGORY_FILTER_REMOVE) {
            if (frimline.getObserver().getData() != null && !frimline.getObserver().getData().isEmpty()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        selectedCategory = null;
                        search();
                    }
                });

            }
        }
    }

    public void reloadHot(DataTransferModel dataTransferModel, boolean addOrNot) {
        String productId = dataTransferModel.getProductId();
        int productPosition = Integer.parseInt(dataTransferModel.getProductPosition()); // position from 3 layout produc t item
        int layoutType = Integer.parseInt(dataTransferModel.getLayoutType());//item layout 3 product or 2 product or 1 product
        int itemPosition = Integer.parseInt(dataTransferModel.getItemPosition()); // item layout position
        int adapterPosition = Integer.parseInt(dataTransferModel.getAdapterPosition()); // item layout position

        if (searchResult.size() > 2) {
            SearchModel model = searchResult.get(2);
            ProductModel dashBoardItemList = model.getHotProduct().get(adapterPosition);
            dashBoardItemList.setAddedToCart(addOrNot);
            searchAdapter.notifyItemChanged(2);
        }
    }

    public void relaodData(DataTransferModel dataTransferModel, boolean addOrNot) {
        String productId = dataTransferModel.getProductId();
        int productPosition = Integer.parseInt(dataTransferModel.getProductPosition()); // position from 3 layout produc t item
        int layoutType = Integer.parseInt(dataTransferModel.getLayoutType());//item layout 3 product or 2 product or 1 product
        int itemPosition = Integer.parseInt(dataTransferModel.getItemPosition()); // item layout position
        int adapterPosition = Integer.parseInt(dataTransferModel.getAdapterPosition()); // item layout position

        SearchModel model = searchResult.get(0);
        HomeModel dashBoardItemList = model.getTopProduct().get(adapterPosition);
        com.app.frimline.models.HomeFragements.ProductModel productModel = dashBoardItemList.getApiProductModel().get(productPosition);
        productModel.setAddedToCart(addOrNot);

        searchAdapter.notifyItemChanged(0);

    }
}