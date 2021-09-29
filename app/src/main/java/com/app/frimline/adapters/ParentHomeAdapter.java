package com.app.frimline.adapters;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.databinding.ItemHomeAlertCovidSectionLayoutBinding;
import com.app.frimline.databinding.ItemHomeBannerSectionLayoutBinding;
import com.app.frimline.databinding.ItemHomeCategorySectionLayoutBinding;
import com.app.frimline.databinding.ItemHomeOffersSectionLayoutBinding;
import com.app.frimline.databinding.ItemHomeProductSectionLayoutBinding;
import com.app.frimline.databinding.ItemHomePromoOffersSectionLayoutBinding;
import com.app.frimline.databinding.ItemHomeTopRattingSectionLayoutBinding;
import com.app.frimline.models.HomeFragements.BannerModel;
import com.app.frimline.models.HomeModel;
import com.app.frimline.models.LAYOUT_TYPE;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ParentHomeAdapter extends RecyclerView.Adapter {
    Activity activity;
    private ArrayList<HomeModel> dashBoardItemList = new ArrayList<>();

    public ParentHomeAdapter(ArrayList<HomeModel> dashBoardItemList, Activity activity) {

        this.dashBoardItemList = dashBoardItemList;
        this.activity = activity;
        Gson gson = new Gson();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case LAYOUT_TYPE
                    .BANNER:
                ItemHomeBannerSectionLayoutBinding layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_home_banner_section_layout, viewGroup, false);
                return new Banner(layoutBinding);
            case LAYOUT_TYPE
                    .CATEGORY_PRODUCT:
                ItemHomeProductSectionLayoutBinding twoLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_home_product_section_layout, viewGroup, false);
                return new CategoryProduct(twoLayoutBinding);
            case LAYOUT_TYPE
                    .CATEGORY:
                ItemHomeCategorySectionLayoutBinding binding3 = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_home_category_section_layout, viewGroup, false);
                return new Category(binding3);

            case LAYOUT_TYPE
                    .ALERT_COVID:
                ItemHomeAlertCovidSectionLayoutBinding binding4 = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_home_alert_covid_section_layout, viewGroup, false);
                return new AlertCovid(binding4);

            case LAYOUT_TYPE
                    .PROMO_CODES:
                ItemHomePromoOffersSectionLayoutBinding binding5 = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_home_promo_offers_section_layout, viewGroup, false);
                return new PromoCodes(binding5);

            case LAYOUT_TYPE
                    .TOP_RATTED:
                ItemHomeTopRattingSectionLayoutBinding binding6 = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_home_top_ratting_section_layout, viewGroup, false);
                return new TopRatted(binding6);

            case LAYOUT_TYPE
                    .OFFERS:
                ItemHomeOffersSectionLayoutBinding binding7 = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_home_offers_section_layout, viewGroup, false);
                return new Offers(binding7);

        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (dashBoardItemList.get(position).getLayoutType()) {
            case 0:
                return LAYOUT_TYPE.BANNER;
            case 1:
                return LAYOUT_TYPE.CATEGORY_PRODUCT;
            case 2:
                return LAYOUT_TYPE.ALERT_COVID;
            case 3:
                return LAYOUT_TYPE.CATEGORY;
            case 4:
                return LAYOUT_TYPE.TOP_RATTED;
            case 5:
                return LAYOUT_TYPE.OFFERS;
            case 6:
                return LAYOUT_TYPE.PROMO_CODES;
            default:
                return -1;
        }

    }

    @Override
    public int getItemCount() {
        return dashBoardItemList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final HomeModel model = dashBoardItemList.get(position);

        if (model != null) {
            switch (model.getLayoutType()) {
                case LAYOUT_TYPE.BANNER:
                    if (CONSTANT.API_MODE) {
                        Timer timer;

                        HomeBannerAdapter sliderAdapter = new HomeBannerAdapter(model.getBannerList(), activity);
                        ((Banner) holder).binding.viewPager.setAdapter(sliderAdapter);

                        ((Banner) holder).binding.dot.setViewPager(((Banner) holder).binding.viewPager);
                        ((Banner) holder).binding.dot.setSelectedDotColor(Color.parseColor(new PREF(activity).getCategoryColor()));
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                ((Banner) holder).binding.viewPager.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((Banner) holder).binding.viewPager.setCurrentItem((((Banner) holder).binding.viewPager.getCurrentItem() + 1) % sliderAdapter.getCount(), true);
                                    }
                                });
                            }
                        };

                        timer = new Timer();
                        timer.schedule(timerTask, 7000, 7000);
                    } else {
                        List<BannerModel> outCategoryModels = new ArrayList<>();
                        outCategoryModels.add(new BannerModel());
                        outCategoryModels.add(new BannerModel());
                        HomeBannerAdapter sliderAdapter = new HomeBannerAdapter(outCategoryModels, activity);
                        ((Banner) holder).binding.viewPager.setAdapter(sliderAdapter);
                        ((Banner) holder).binding.dot.setViewPager(((Banner) holder).binding.viewPager);
                        ((Banner) holder).binding.dot.setSelectedDotColor(Color.parseColor(new PREF(activity).getCategoryColor()));
                    }
                    break;
                case LAYOUT_TYPE.CATEGORY_PRODUCT:
                    MultiViewAdapterForHomeFragment productAdapter = new MultiViewAdapterForHomeFragment(model.getCategoryProduct(), activity);
                    productAdapter.setPosition(position);
                    ((CategoryProduct) holder).binding.categoryProductRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                    ((CategoryProduct) holder).binding.categoryProductRecycler.setHasFixedSize(true);
                    SnapHelper startSnapHelper = new PagerSnapHelper();
                    ((CategoryProduct) holder).binding.categoryProductRecycler.setOnFlingListener(null);
                    startSnapHelper.attachToRecyclerView(((CategoryProduct) holder).binding.categoryProductRecycler);
                    ((CategoryProduct) holder).binding.categoryProductRecycler.setAdapter(productAdapter);
                    break;
                case LAYOUT_TYPE.CATEGORY:
                    OurProductAdapter productAdapte3r = new OurProductAdapter(model.getCategoryArrayList(), activity);
                    ((Category) holder).binding.ourProductSection.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                    ((Category) holder).binding.ourProductSection.setAdapter(productAdapte3r);
                    break;

                case LAYOUT_TYPE.OFFERS:
                    TrendingProductAdapter productAdapter2 = new TrendingProductAdapter(model.getTradingStoriesList(), activity);
                    ((Offers) holder).binding.trendingSectionRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                    ((Offers) holder).binding.trendingSectionRecycler.setAdapter(productAdapter2);
                    break;
                case LAYOUT_TYPE.TOP_RATTED:

                    TopRattedProductAdapter adaptertop = new TopRattedProductAdapter(model.getApiProductModel(), activity);
                    ((TopRatted) holder).binding.topRattingProductRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                    ((TopRatted) holder).binding.topRattingProductRecycler.setAdapter(adaptertop);
                    break;

                case LAYOUT_TYPE.ALERT_COVID:
                    if (CONSTANT.API_MODE) {
                        ((AlertCovid) holder).binding.content.setVisibility(View.GONE);
                        Glide.with(activity).load(model.getCouponCodeBannerList().get(0).getUrl())
                                .placeholder(R.drawable.ic_banner_place_holder)
                                .error(R.drawable.ic_banner_place_holder)
                                .into(((AlertCovid) holder).binding.bannerImage);
                    }
                    break;
                case LAYOUT_TYPE.PROMO_CODES:
                    if (CONSTANT.API_MODE) {
                        ((PromoCodes) holder).binding.content.setVisibility(View.GONE);
                        Glide.with(activity).load(model.getCouponCodeBannerList().get(0).getUrl())
                                .placeholder(R.drawable.ic_banner_place_holder).error(R.drawable.ic_banner_place_holder).into(((PromoCodes) holder).binding.bannerImage);
                    }
                    break;
            }
        }
    }


    public static class Banner extends RecyclerView.ViewHolder {
        ItemHomeBannerSectionLayoutBinding binding;

        public Banner(ItemHomeBannerSectionLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }


    public static class CategoryProduct extends RecyclerView.ViewHolder {
        ItemHomeProductSectionLayoutBinding binding;

        public CategoryProduct(ItemHomeProductSectionLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    public static class AlertCovid extends RecyclerView.ViewHolder {
        ItemHomeAlertCovidSectionLayoutBinding binding;

        public AlertCovid(ItemHomeAlertCovidSectionLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    public class TopRatted extends RecyclerView.ViewHolder {
        ItemHomeTopRattingSectionLayoutBinding binding;

        public TopRatted(ItemHomeTopRattingSectionLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            changeColor();
        }

        public void changeColor() {
            PREF pref = new PREF(activity);
            binding.topRaatedviewUnderline.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        }
    }

    public class Offers extends RecyclerView.ViewHolder {
        ItemHomeOffersSectionLayoutBinding binding;

        public Offers(ItemHomeOffersSectionLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            changeColor();
        }

        public void changeColor() {
            PREF pref = new PREF(activity);
            binding.offerUnderline.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        }
    }

    public class Category extends RecyclerView.ViewHolder {
        ItemHomeCategorySectionLayoutBinding binding;

        public Category(ItemHomeCategorySectionLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            changeColor();
        }

        public void changeColor() {
            PREF pref = new PREF(activity);
            binding.ourCategoryUnderline.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pref.getCategoryColor())));
        }
    }

    public static class PromoCodes extends RecyclerView.ViewHolder {
        ItemHomePromoOffersSectionLayoutBinding binding;

        public PromoCodes(ItemHomePromoOffersSectionLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}

