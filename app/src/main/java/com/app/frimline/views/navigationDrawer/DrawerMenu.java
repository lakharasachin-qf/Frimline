package com.app.frimline.views.navigationDrawer;


import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;

import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.fragments.BlogsFragment;
import com.app.frimline.fragments.CategoryProfileFragment;
import com.app.frimline.fragments.CategoryRootFragment;
import com.app.frimline.fragments.CommonFragment;
import com.app.frimline.fragments.HomeFragment;
import com.app.frimline.fragments.MyAccountFragment;
import com.app.frimline.fragments.OrderHistoryFragment;
import com.app.frimline.fragments.ShopFragment;
import com.app.frimline.screens.BillingAddressActivity;
import com.app.frimline.screens.LoginActivity;
import com.app.frimline.screens.MyCartActivity;
import com.app.frimline.screens.SearchActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DrawerMenu {
    private Activity activity;
    private ExpandableListAdapter expandableListAdapter;
    private ExpandableListView expandableListView;
    //    private Animation animationUp, animationDown;
    private List<MenuModel> headerList = new ArrayList<>();
    private HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    private ImageView proflie_img_navigation;
    DrawerLayout drawer;

    public static int CATEGORY_ROOT_FRAGMENT = 0;
    public static int SHOP_FRAGMENT = 2;
    public static int HOME_FRAGMENT = 1;
    private int defaultFragmentFlag = HOME_FRAGMENT;

    public DrawerMenu(Activity activity, int flag) {
        this.activity = activity;
        drawer = activity.findViewById(R.id.drawer_layout);
        expandableListView = activity.findViewById(R.id.expandableListView);
        defaultFragmentFlag = flag;
        loadDefaultFragment();
    }

    public void setDefaultFragment(int flag) {
        defaultFragmentFlag = flag;
    }

    public void loadDefaultFragment() {
        if (defaultFragmentFlag == HOME_FRAGMENT)
            addFragment(homeFragment);
        else if (defaultFragmentFlag == SHOP_FRAGMENT) {
            addFragment(shopFragment);
            currentMenuItem = "Shop";
        }
    }


    public void prepareMenuData() {
        //first menu
        MenuModel menuModel = new MenuModel("Dashboard", true, false, "", ContextCompat.getDrawable(activity, R.drawable.ic_dashboard_icon)); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        menuModel = new MenuModel("Home", true, false, "", ContextCompat.getDrawable(activity, R.drawable.ic_menu_home)); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        menuModel = new MenuModel("Shop", true, false, "", ContextCompat.getDrawable(activity, R.drawable.ic_menu_shop)); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);


        menuModel = new MenuModel("Checkout", true, false, "", ContextCompat.getDrawable(activity, R.drawable.ic_menu_checkout)); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);
        menuModel = new MenuModel("About us", true, false, "", ContextCompat.getDrawable(activity, R.drawable.ic_menu_about_us)); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);
        menuModel = new MenuModel("Blogs", true, false, "", ContextCompat.getDrawable(activity, R.drawable.ic_blog)); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        menuModel = new MenuModel("Contact us", true, false, "", ContextCompat.getDrawable(activity, R.drawable.ic_menu_call)); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);
        menuModel = new MenuModel("Privacy Policy", true, false, "", ContextCompat.getDrawable(activity, R.drawable.ic_privacy_policy)); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);
        menuModel = new MenuModel("Shipping Policy", true, false, "", ContextCompat.getDrawable(activity, R.drawable.ic_ic_menu_shipping_policy)); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);
    }

    RelativeLayout HomePageLayout;
    TextView titleTxt;
    LinearLayout orderHistoryTab;
    LinearLayout myAccountTab;
    LinearLayout myCartTab;
    ImageView searchAction;
    ImageView searchAction2;

    RelativeLayout profileView;
    RelativeLayout cartActionLayout;
    RelativeLayout OtherScreenLayout;

    public void populateExpandableList() {
        CategoryProfileFragment.OnNavClick onNavClick = new CategoryProfileFragment.OnNavClick() {
            @Override
            public void onNavigationDrawerClick() {
                drawer.openDrawer(GravityCompat.START);
            }

            @Override
            public void GoToStore() {
                onBackPressed();
            }

        };
        MyAccountFragment.OnDrawerAction onDrawerAction = new MyAccountFragment.OnDrawerAction() {
            @Override
            public void changeFragment() {
                HomePageLayout.setVisibility(View.GONE);
                titleTxt.setText("Orders");
                OtherScreenLayout.setVisibility(View.VISIBLE);


                currentMenuItem = "Order History";
                Fragment fragmentSelected = orderHistoryFragment;
                replaceFragment(fragmentSelected);
                drawer.closeDrawer(GravityCompat.START);
            }
        };
        myAccountFragment.setDrawerAction(onDrawerAction);
        profileFragment.setOnNavClick(onNavClick);
        OtherScreenLayout = activity.findViewById(R.id.OtherScreenLayout);
        cartActionLayout = activity.findViewById(R.id.cartActionLayout);
        profileView = activity.findViewById(R.id.profileView);
        HomePageLayout = activity.findViewById(R.id.HomePageLayout);
        searchAction = activity.findViewById(R.id.searchAction);
        searchAction2 = activity.findViewById(R.id.searchAction2);

        myCartTab = activity.findViewById(R.id.myCartTab);
        titleTxt = activity.findViewById(R.id.titleTxt);
        myAccountTab = activity.findViewById(R.id.myAccountTab);
        orderHistoryTab = activity.findViewById(R.id.orderHistoryTab);

        expandableListAdapter = new ExpandableListAdapter(activity, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //parent.smoothScrollToPosition(groupPosition);
                Fragment fragmentSelected = null;
                Intent intent;
                Toolbar toolbar_Navigation = activity.findViewById(R.id.toolbar_Navigation);
                toolbar_Navigation.setVisibility(View.VISIBLE);
                if (headerList.get(groupPosition).isGroup) {

                    if (!headerList.get(groupPosition).hasChildren) {
                        switch (headerList.get(groupPosition).menuName) {
                            case "Dashboard":
                                drawer.closeDrawer(GravityCompat.START);
                                ((AppCompatActivity) activity).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                HELPER.ON_BACK_PRESS(activity);
                                break;
                            case "Home":
                                fragmentSelected = homeFragment;
                                HomePageLayout.setVisibility(View.VISIBLE);
                                OtherScreenLayout.setVisibility(View.GONE);
                                break;
                            case "Shop":
//                                fragmentSelected = shopFragment;
//                                HomePageLayout.setVisibility(View.VISIBLE);
//                                OtherScreenLayout.setVisibility(View.GONE);
//                                searchActionSecond.setVisibility(View.GONE);
                                searchAction2.setVisibility(View.VISIBLE);
                                fragmentSelected = shopFragment;
                                HomePageLayout.setVisibility(View.GONE);
                                OtherScreenLayout.setVisibility(View.VISIBLE);
                                titleTxt.setText("Shop");
                                currentMenuItem = "Shop";

                                break;
                            case "Checkout":
                                // fragmentSelected = blogsFragment;
                                drawer.closeDrawer(GravityCompat.START);
                                HELPER.SIMPLE_ROUTE(activity, BillingAddressActivity.class);
                                break;
                            case "About us":
                                fragmentSelected = commonFragment;
                                HomePageLayout.setVisibility(View.VISIBLE);
                                OtherScreenLayout.setVisibility(View.GONE);
                                break;

                            case "Contact us":
                                fragmentSelected = commonFragment;
                                HomePageLayout.setVisibility(View.VISIBLE);
                                OtherScreenLayout.setVisibility(View.GONE);
                                break;
                            case "Privacy Policy":
                                fragmentSelected = commonFragment;
                                HomePageLayout.setVisibility(View.VISIBLE);
                                OtherScreenLayout.setVisibility(View.GONE);
                                break;
                            case "Shipping Policy":
                                fragmentSelected = commonFragment;
                                HomePageLayout.setVisibility(View.VISIBLE);
                                OtherScreenLayout.setVisibility(View.GONE);
                                break;
                            case "Blogs":
                                fragmentSelected = blogsFragment;
                                HomePageLayout.setVisibility(View.GONE);
                                OtherScreenLayout.setVisibility(View.VISIBLE);
                                titleTxt.setText("Blogs");
                                searchAction2.setVisibility(View.VISIBLE);
                                currentMenuItem = "Blogs";
                                break;
                        }
                    }
                    if (fragmentSelected != null) {
                        currentMenuItem = headerList.get(groupPosition).menuName;
                        replaceFragment(fragmentSelected);
                        drawer.closeDrawer(GravityCompat.START);
                    }
                }
                return true;
            }
        });


        orderHistoryTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePageLayout.setVisibility(View.GONE);
                OtherScreenLayout.setVisibility(View.VISIBLE);
                searchAction2.setVisibility(View.GONE);
                titleTxt.setText("Orders");
                currentMenuItem = "Order History";
                Fragment fragmentSelected = orderHistoryFragment;
                replaceFragment(fragmentSelected);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        myAccountTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toolbar toolbar_Navigation = activity.findViewById(R.id.toolbar_Navigation);
                toolbar_Navigation.setVisibility(View.VISIBLE);
                HomePageLayout.setVisibility(View.GONE);
                searchAction2.setVisibility(View.GONE);
                OtherScreenLayout.setVisibility(View.VISIBLE);
                titleTxt.setText("My Account");
                currentMenuItem = "Account";
                Fragment fragmentSelected = myAccountFragment;
                replaceFragment(fragmentSelected);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        myCartTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                HELPER.SIMPLE_ROUTE(activity, MyCartActivity.class);
            }
        });
        cartActionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.SIMPLE_ROUTE(activity, MyCartActivity.class);
            }
        });
        searchAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                HELPER.SIMPLE_ROUTE(activity, SearchActivity.class);
            }
        });
        searchAction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                HELPER.SIMPLE_ROUTE(activity, SearchActivity.class);
            }
        });


        TextView userNameTxt = activity.findViewById(R.id.userNameTxt);
        userNameTxt.setText("Sign In");
        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                if (new PREF(activity).isLogin()){
                    Toolbar toolbar_Navigation = activity.findViewById(R.id.toolbar_Navigation);
                    toolbar_Navigation.setVisibility(View.VISIBLE);
                    HomePageLayout.setVisibility(View.GONE);
                    OtherScreenLayout.setVisibility(View.VISIBLE);
                    titleTxt.setText("My Account");
                    currentMenuItem = "Account";
                    Fragment fragmentSelected = myAccountFragment;
                    replaceFragment(fragmentSelected);
                    drawer.closeDrawer(GravityCompat.START);
                }else {
                    HELPER.SIMPLE_ROUTE(activity, LoginActivity.class);
                }
            }
        });

    }


    private AppBarConfiguration mAppBarConfiguration;
    private Fragment fragmentCurrent;
    private HomeFragment homeFragment = new HomeFragment();
    private ShopFragment shopFragment = new ShopFragment();
    private BlogsFragment blogsFragment = new BlogsFragment();
    private OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();
    private MyAccountFragment myAccountFragment = new MyAccountFragment();
    private CategoryProfileFragment profileFragment = new CategoryProfileFragment();
    private CategoryRootFragment categoryRootFragment = new CategoryRootFragment();
    private CommonFragment commonFragment = new CommonFragment();
    public String currentMenuItem;

    private void addFragment(Fragment fragment) {
        fragmentCurrent = fragment;
        ((AppCompatActivity) activity).getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment, fragment).commit();
    }

    private void replaceFragment(Fragment fragment) {
        fragmentCurrent = fragment;
        if(!activity.isFinishing()) {

            ((AppCompatActivity) activity).getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment)
                    .addToBackStack(null).commit();
        }
    }

    public void onBackPressed() {
        DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (fragmentCurrent.equals(homeFragment)) {
                ((AppCompatActivity) activity).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                HELPER.ON_BACK_PRESS(activity);
            } else {

                ((AppCompatActivity) activity).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                replaceFragment(homeFragment);
                HomePageLayout.setVisibility(View.GONE);
                currentMenuItem = "Home";
                OtherScreenLayout.setVisibility(View.VISIBLE);

                Toolbar toolbar_Navigation = activity.findViewById(R.id.toolbar_Navigation);
                toolbar_Navigation.setVisibility(View.VISIBLE);
                HomePageLayout.setVisibility(View.VISIBLE);
                OtherScreenLayout.setVisibility(View.GONE);

            }
        }
    }

    public void onBackPressedForCategoryRoot() {
        DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            ((AppCompatActivity) activity).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            replaceFragment(categoryRootFragment);
            HomePageLayout.setVisibility(View.GONE);
            currentMenuItem = "Home";
            OtherScreenLayout.setVisibility(View.VISIBLE);

            Toolbar toolbar_Navigation = activity.findViewById(R.id.toolbar_Navigation);
            toolbar_Navigation.setVisibility(View.VISIBLE);
            HomePageLayout.setVisibility(View.VISIBLE);
            OtherScreenLayout.setVisibility(View.GONE);


        }
    }

    public void performCategoryProfile() {
        Toolbar toolbar_Navigation = activity.findViewById(R.id.toolbar_Navigation);
        toolbar_Navigation.setVisibility(View.GONE);
        currentMenuItem = "Category Profile";
        Fragment fragmentSelected = profileFragment;
        replaceFragment(fragmentSelected);
        drawer.closeDrawer(GravityCompat.START);
    }
}



