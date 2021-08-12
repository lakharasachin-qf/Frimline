package com.app.frimline.screens;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.frimline.BaseActivity;
import com.app.frimline.Common.CONSTANT;
import com.app.frimline.Common.FRIMLINE;
import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.ObserverActionID;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.adapters.MyCartAdapter;
import com.app.frimline.databinding.ActivityMyCartBinding;
import com.app.frimline.models.HomeFragements.ProductModel;

import java.util.ArrayList;

public class MyCartActivity extends BaseActivity {
    private ActivityMyCartBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_my_cart);
        makeStatusBarSemiTranspenret(binding.toolbarNavigation.toolbar);

        binding.boottomFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefManager.isLogin())
                    HELPER.SIMPLE_ROUTE(act, BillingAddressActivity.class);
                else
                    HELPER.SIMPLE_ROUTE(act, LoginActivity.class);
            }
        });
        binding.toolbarNavigation.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.ON_BACK_PRESS_ANIM(act);
            }
        });

        binding.toolbarNavigation.title.setText("Cart");
        changeColor();


        binding.promoCodeEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    int scrollTo = ((View) v.getParent().getParent()).getTop() + v.getTop();
                    binding.scrollView.scrollTo(0, scrollTo);
                }
            }
        });

        binding.applyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.promoCodeContainer.setVisibility(View.GONE);
                binding.appliedCodeSuccess.setVisibility(View.VISIBLE);
            }
        });
        binding.removePromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.promoCodeContainer.setVisibility(View.VISIBLE);
                binding.appliedCodeSuccess.setVisibility(View.GONE);
            }
        });

        if (CONSTANT.API_MODE) {
            loadData();
        } else {
            setAdapter();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        prefManager = new PREF(act);
    }

    MyCartAdapter cartAdapter;
    ArrayList<ProductModel> cartItemList;

    public void loadData() {
        int count = db.productEntityDao().getAll().size();
        if (count != 0) {
            cartItemList = HELPER.getCartList(db.productEntityDao().getAll());
            cartAdapter = new MyCartAdapter(cartItemList, act);
            cartAdapter.setActionsListener(new MyCartAdapter.setActionsListener() {
                @Override
                public void onDeleteAction(int position, ProductModel model) {
                    for (int i = 0; i < cartItemList.size(); i++) {
                        if (model.getId().equalsIgnoreCase(cartItemList.get(i).getId())) {
                            db.productEntityDao().deleteProduct(model.getId());
                            cartItemList.remove(i);

                            FRIMLINE.getInstance().getObserver().setValue(ObserverActionID.CART_COUNTER_UPDATE);
                            cartAdapter.notifyItemRemoved(position);
                            cartAdapter.notifyItemRangeChanged(position, cartItemList.size());

                            setState();
                            if (cartItemList.size() == 0) {
                                setNoDataFound();
                            }
                            break;
                        }
                    }
                }
            });

            binding.cartRecycler.setLayoutManager(new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false));
            binding.cartRecycler.setNestedScrollingEnabled(false);
            binding.cartRecycler.setAdapter(cartAdapter);


            setState();

        } else {
            setNoDataFound();
        }
    }

    public void setState() {
        if (cartItemList.size() == 1)
            binding.itemCount.setText(cartItemList.size() + " Item");
        else
            binding.itemCount.setText(cartItemList.size() + " Items");


        double MRP = 0.00;
        for (int i = 0; i < cartItemList.size(); i++) {
            MRP = MRP + Double.parseDouble(cartItemList.get(i).getCalculatedAmount());
        }
        binding.totalMRP.setText("Total : " + act.getString(R.string.Rs) + MRP);
    }

    public void setAdapter() {
        ArrayList<ProductModel> arrayList = new ArrayList<>();
        ProductModel homeModel = new ProductModel();
        homeModel.setName("0");
        arrayList.add(homeModel);
        homeModel = new ProductModel();
        homeModel.setName("1");
        arrayList.add(homeModel);
        homeModel = new ProductModel();
        homeModel.setName("2");
        arrayList.add(homeModel);

        homeModel = new ProductModel();
        homeModel.setName("3");
        arrayList.add(homeModel);


        MyCartAdapter shopFilterAdapter = new MyCartAdapter(arrayList, act);
        shopFilterAdapter.setActionsListener(new MyCartAdapter.setActionsListener() {
            @Override
            public void onDeleteAction(int position, ProductModel model) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (model.getName().equalsIgnoreCase(arrayList.get(i).getName())) {
                        arrayList.remove(i);
                        shopFilterAdapter.notifyItemRemoved(position);
                        shopFilterAdapter.notifyItemRangeChanged(position, arrayList.size());
                        if (arrayList.size() == 0) {
                            setNoDataFound();
                        }
                        break;
                    }
                }
            }
        });

        binding.cartRecycler.setLayoutManager(new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false));
        binding.cartRecycler.setNestedScrollingEnabled(false);
        binding.cartRecycler.setAdapter(shopFilterAdapter);



    }


    public void setNoDataFound() {
        binding.boottomFooter.setVisibility(View.GONE);
        binding.scrollView.setVisibility(View.GONE);
        binding.NoDataFound.setVisibility(View.VISIBLE);

    }

    public void changeColor() {
        binding.boottomText.setTextColor((Color.parseColor(new PREF(act).getThemeColor())));
        HELPER.backgroundTint(act, binding.applyCode, true);
        HELPER.backgroundTint(act, binding.removePromo, true);
        HELPER.backgroundTint(act, binding.bottomSlider, true);
        HELPER.backgroundTint(act, binding.scrollView, true);


    }
}