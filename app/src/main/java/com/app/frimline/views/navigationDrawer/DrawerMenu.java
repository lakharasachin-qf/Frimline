package com.app.frimline.views.navigationDrawer;


import android.app.Activity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;

import com.app.frimline.Common.HELPER;
import com.app.frimline.R;
import com.app.frimline.fragments.CategoryProfileFragment;
import com.app.frimline.fragments.CategoryRootFragment;
import com.app.frimline.screens.orders.OrderHistoryFragment;
import com.app.frimline.screens.profile.MyAccountFragment;
import com.app.frimline.ui.gallery.GalleryFragment;
import com.app.frimline.ui.home.HomeFragment;
import com.app.frimline.ui.slideshow.SlideshowFragment;

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
        else if (defaultFragmentFlag == CATEGORY_ROOT_FRAGMENT) {
            addFragment(categoryRootFragment);
            currentMenuItem = "Category Root";
        }
    }


    public void prepareMenuData() {
        //first menu
        MenuModel menuModel = new MenuModel("Home", true, false, "", activity.getResources().getDrawable(R.drawable.ic_menu_home)); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        menuModel = new MenuModel("Shop", true, false, "", activity.getResources().getDrawable(R.drawable.ic_menu_shop)); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);


        menuModel = new MenuModel("Checkout", true, false, "", activity.getResources().getDrawable(R.drawable.ic_menu_checkout)); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);


        menuModel = new MenuModel("About us", true, false, "", activity.getResources().getDrawable(R.drawable.ic_menu_about_us)); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);
        menuModel = new MenuModel("Contact us", true, false, "", activity.getResources().getDrawable(R.drawable.ic_menu_call)); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);
        menuModel = new MenuModel("Privacy Policy", true, false, "", activity.getResources().getDrawable(R.drawable.ic_privacy_policy)); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);
        menuModel = new MenuModel("Shipping Policy", true, false, "", activity.getResources().getDrawable(R.drawable.ic_ic_menu_shipping_policy)); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);
    }

    RelativeLayout HomePageLayout;
    TextView titleTxt;
    LinearLayout orderHistoryTab;
    LinearLayout myAccountTab;

    public void populateExpandableList() {

        HomePageLayout = activity.findViewById(R.id.HomePageLayout);

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
                if (headerList.get(groupPosition).isGroup) {

                    if (!headerList.get(groupPosition).hasChildren) {
                        switch (headerList.get(groupPosition).menuName) {
                            case "Home":
                                fragmentSelected = homeFragment;
                                HomePageLayout.setVisibility(View.VISIBLE);
                                titleTxt.setVisibility(View.GONE);
                                break;
                            case "Shop":
                                fragmentSelected = galleryFragment;
                                HomePageLayout.setVisibility(View.VISIBLE);
                                titleTxt.setVisibility(View.GONE);
                                break;
                            case "Checkout":
                                fragmentSelected = slideshowFragment;
                                break;
                            case "About us":

                                break;

                            case "Contact us":

                                break;
                            case "Privacy Policy":

                                break;
                            case "Shipping Policy":

                                break;

                        }
                    }
                    currentMenuItem = headerList.get(groupPosition).menuName;
                    replaceFragment(fragmentSelected);
                    drawer.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });


        orderHistoryTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePageLayout.setVisibility(View.GONE);
                titleTxt.setVisibility(View.VISIBLE);
                currentMenuItem = "Order History";
                Fragment fragmentSelected = orderHistoryFragment;
                replaceFragment(fragmentSelected);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        myAccountTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePageLayout.setVisibility(View.GONE);
                titleTxt.setVisibility(View.VISIBLE);
                titleTxt.setText("My Account");
                currentMenuItem = "Account";
                Fragment fragmentSelected = myAccountFragment;
                replaceFragment(fragmentSelected);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

    }


    private AppBarConfiguration mAppBarConfiguration;
    private Fragment fragmentCurrent;
    private HomeFragment homeFragment = new HomeFragment();
    private GalleryFragment galleryFragment = new GalleryFragment();
    private SlideshowFragment slideshowFragment = new SlideshowFragment();
    private OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();
    private MyAccountFragment myAccountFragment = new MyAccountFragment();
    private CategoryProfileFragment profileFragment = new CategoryProfileFragment();
    private CategoryRootFragment categoryRootFragment = new CategoryRootFragment();
    public String currentMenuItem;

    private void addFragment(Fragment fragment) {
        fragmentCurrent = fragment;
        ((AppCompatActivity) activity).getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment, fragment).commit();
    }

    private void replaceFragment(Fragment fragment) {
        fragmentCurrent = fragment;
        ((AppCompatActivity) activity).getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null).commit();
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
                titleTxt.setVisibility(View.VISIBLE);

                Toolbar toolbar_Navigation = activity.findViewById(R.id.toolbar_Navigation);
                toolbar_Navigation.setVisibility(View.VISIBLE);
                HomePageLayout.setVisibility(View.VISIBLE);
                titleTxt.setVisibility(View.GONE);

            }
        }
    }

    public void onBackPressedForCategoryRoot() {
        DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (fragmentCurrent.equals(categoryRootFragment)) {
                ((AppCompatActivity) activity).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                HELPER.ON_BACK_PRESS(activity);
            } else {

                ((AppCompatActivity) activity).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                replaceFragment(categoryRootFragment);
                HomePageLayout.setVisibility(View.GONE);
                currentMenuItem = "Home";
                titleTxt.setVisibility(View.VISIBLE);

                Toolbar toolbar_Navigation = activity.findViewById(R.id.toolbar_Navigation);
                toolbar_Navigation.setVisibility(View.VISIBLE);
                HomePageLayout.setVisibility(View.VISIBLE);
                titleTxt.setVisibility(View.GONE);

            }
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



