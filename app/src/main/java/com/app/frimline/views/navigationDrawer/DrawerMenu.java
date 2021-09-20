package com.app.frimline.views.navigationDrawer;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;

import com.app.frimline.Common.HELPER;
import com.app.frimline.Common.PREF;
import com.app.frimline.R;
import com.app.frimline.databaseHelper.CartRoomDatabase;
import com.app.frimline.databinding.DialogDiscardImageBinding;
import com.app.frimline.fragments.BlogsFragment;
import com.app.frimline.fragments.CategoryProfileFragment;
import com.app.frimline.fragments.CategoryRootFragment;
import com.app.frimline.fragments.CommonFragment;
import com.app.frimline.fragments.HomeFragment;
import com.app.frimline.fragments.MyAccountFragment;
import com.app.frimline.fragments.OrderHistoryFragment;
import com.app.frimline.fragments.ShopFragment;
import com.app.frimline.fragments.WishlistFragment;
import com.app.frimline.screens.BillingAddressActivity;
import com.app.frimline.screens.CategoryRootActivity;
import com.app.frimline.screens.LoginActivity;
import com.app.frimline.screens.MyCartActivity;
import com.app.frimline.screens.SearchActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DrawerMenu {
    public static int CATEGORY_ROOT_FRAGMENT = 0;
    public static int SHOP_FRAGMENT = 2;
    public static int HOME_FRAGMENT = 1;
    public static int ORDER_HISTORY = 3;
    public String currentMenuItem;
    DrawerLayout drawer;
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
    private final Activity activity;
    private ExpandableListAdapter expandableListAdapter;
    private final ExpandableListView expandableListView;
    //    private Animation animationUp, animationDown;
    private final List<MenuModel> headerList = new ArrayList<>();
    private final HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    private ImageView proflie_img_navigation;
    private int defaultFragmentFlag = HOME_FRAGMENT;
    private AppBarConfiguration mAppBarConfiguration;
    private Fragment fragmentCurrent;
    private final HomeFragment homeFragment = new HomeFragment();
    private final ShopFragment shopFragment = new ShopFragment();
    private final BlogsFragment blogsFragment = new BlogsFragment();
    private final OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();
    private final MyAccountFragment myAccountFragment = new MyAccountFragment();
    private final CategoryProfileFragment profileFragment = new CategoryProfileFragment();
    private final CategoryRootFragment categoryRootFragment = new CategoryRootFragment();
    private final CommonFragment commonFragment = new CommonFragment();
    private final WishlistFragment wishlistFragment = new WishlistFragment();

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
        } else if (defaultFragmentFlag == ORDER_HISTORY) {
            HomePageLayout = activity.findViewById(R.id.HomePageLayout);
            searchAction = activity.findViewById(R.id.searchAction);
            searchAction2 = activity.findViewById(R.id.searchAction2);
            titleTxt = activity.findViewById(R.id.titleTxt);
            OtherScreenLayout = activity.findViewById(R.id.OtherScreenLayout);
            addFragment(orderHistoryFragment);
            Toolbar toolbar_Navigation = activity.findViewById(R.id.toolbar_Navigation);

            toolbar_Navigation.setVisibility(View.VISIBLE);
            HomePageLayout.setVisibility(View.GONE);
            searchAction2.setVisibility(View.GONE);
            OtherScreenLayout.setVisibility(View.VISIBLE);
            titleTxt.setText("Orders");
            currentMenuItem = "Order History";


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

        if (new PREF(activity).isLogin()) {
            menuModel = new MenuModel("Checkout", true, false, "", ContextCompat.getDrawable(activity, R.drawable.ic_menu_checkout)); //Menu of Android Tutorial. No sub menus
            headerList.add(menuModel);
        }

        if (new PREF(activity).isLogin()) {
            menuModel = new MenuModel("Wishlist", true, false, "", ContextCompat.getDrawable(activity, R.drawable.ic_menu_wishlist)); //Menu of Android Tutorial. No sub menus
            headerList.add(menuModel);
        }
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

        if (new PREF(activity).isLogin()) {
            menuModel = new MenuModel("Logout", true, false, "", ContextCompat.getDrawable(activity, R.drawable.ic_logout_black_24dp)); //Menu of Android Tutorial. No sub menus
            headerList.add(menuModel);
            isLogined = true;
        }
    }

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
                if (headerList.get(groupPosition).isGroup) {

                    if (!headerList.get(groupPosition).hasChildren) {
                        switch (headerList.get(groupPosition).menuName) {
                            case "Dashboard":
                                drawer.closeDrawer(GravityCompat.START);
                                if (defaultFragmentFlag == ORDER_HISTORY) {
                                    Intent i = new Intent(activity, CategoryRootActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    activity.startActivity(i);
                                } else {
                                    ((AppCompatActivity) activity).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    HELPER.ON_BACK_PRESS(activity);
                                }
                                break;
                            case "Home":
                                currentMenuItem = "Home";
                                fragmentSelected = homeFragment;
                                HomePageLayout.setVisibility(View.VISIBLE);
                                OtherScreenLayout.setVisibility(View.GONE);
                                toolbar_Navigation.setVisibility(View.VISIBLE);

                                break;
                            case "Shop":
                                currentMenuItem = "Shop";
                                searchAction2.setVisibility(View.VISIBLE);
                                fragmentSelected = shopFragment;
                                HomePageLayout.setVisibility(View.GONE);
                                OtherScreenLayout.setVisibility(View.VISIBLE);
                                titleTxt.setText("Shop");
                                currentMenuItem = "Shop";
                                toolbar_Navigation.setVisibility(View.VISIBLE);

                                break;
                            case "Checkout":
//                                if (currentMenuItem != null && currentMenuItem.equalsIgnoreCase("Category Profile")) {
//                                    toolbar_Navigation.setVisibility(View.GONE);
//                                }
                                drawer.closeDrawer(GravityCompat.START);
                                if (CartRoomDatabase.getAppDatabase(activity).productEntityDao().getAll().size() != 0) {
                                    HELPER.SIMPLE_ROUTE(activity, BillingAddressActivity.class);

                                } else {
                                    checkoutProcess();

                                }
                                break;
                            case "About us":
                            case "Shipping Policy":
                            case "Privacy Policy":
                            case "Contact us":
                                currentMenuItem = "About us";
                                fragmentSelected = commonFragment;
                                HomePageLayout.setVisibility(View.VISIBLE);
                                OtherScreenLayout.setVisibility(View.GONE);
                                toolbar_Navigation.setVisibility(View.VISIBLE);

                                break;
                            case "Blogs":
                                fragmentSelected = blogsFragment;
                                HomePageLayout.setVisibility(View.GONE);
                                OtherScreenLayout.setVisibility(View.VISIBLE);
                                titleTxt.setText("Blogs");
                                searchAction2.setVisibility(View.VISIBLE);
                                currentMenuItem = "Blogs";
                                toolbar_Navigation.setVisibility(View.VISIBLE);

                                break;

                            case "Wishlist":
                                fragmentSelected = wishlistFragment;
                                HomePageLayout.setVisibility(View.GONE);
                                OtherScreenLayout.setVisibility(View.VISIBLE);
                                titleTxt.setText("Wishlist");
                                searchAction2.setVisibility(View.VISIBLE);
                                currentMenuItem = "Wishlist";
                                toolbar_Navigation.setVisibility(View.VISIBLE);
                                break;

                            case "Logout":
                                drawer.closeDrawer(GravityCompat.START);

                                if (currentMenuItem != null && currentMenuItem.equalsIgnoreCase("Category Profile")) {
                                    toolbar_Navigation.setVisibility(View.GONE);
                                }
                                confirmationDialog();
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
//                HomePageLayout.setVisibility(View.GONE);
//                OtherScreenLayout.setVisibility(View.VISIBLE);
//                searchAction2.setVisibility(View.GONE);
//                titleTxt.setText("Orders");
//                currentMenuItem = "Order History";
//                Fragment fragmentSelected = orderHistoryFragment;
//                replaceFragment(fragmentSelected);
//                drawer.closeDrawer(GravityCompat.START);

                Toolbar toolbar_Navigation = activity.findViewById(R.id.toolbar_Navigation);
                toolbar_Navigation.setVisibility(View.VISIBLE);
                HomePageLayout.setVisibility(View.GONE);
                searchAction2.setVisibility(View.GONE);
                OtherScreenLayout.setVisibility(View.VISIBLE);
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
                if (new PREF(activity).isLogin()) {
                    Toolbar toolbar_Navigation = activity.findViewById(R.id.toolbar_Navigation);
                    toolbar_Navigation.setVisibility(View.VISIBLE);
                    HomePageLayout.setVisibility(View.GONE);
                    OtherScreenLayout.setVisibility(View.VISIBLE);
                    titleTxt.setText("My Account");
                    currentMenuItem = "Account";
                    Fragment fragmentSelected = myAccountFragment;
                    replaceFragment(fragmentSelected);
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    HELPER.SIMPLE_ROUTE(activity, LoginActivity.class);
                }
            }
        });

    }

    private void addFragment(Fragment fragment) {
        fragmentCurrent = fragment;
        if (((AppCompatActivity) activity).getSupportFragmentManager().getFragments().size() > 0) {
            for (Fragment cf : ((AppCompatActivity) activity).getSupportFragmentManager().getFragments()) {
                ((AppCompatActivity) activity).getSupportFragmentManager().beginTransaction().remove(cf);
            }
        }
        ((AppCompatActivity) activity).getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment, fragment).commit();
    }

    private void replaceFragment(Fragment fragment) {
        fragmentCurrent = fragment;
        if (!activity.isDestroyed() && !activity.isFinishing()) {
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

    boolean isLogined = false;

    public void addLogoutBtn() {
        if (!isLogined) {
            MenuModel menuModel = new MenuModel("Checkout", true, false, "", ContextCompat.getDrawable(activity, R.drawable.ic_menu_checkout)); //Menu of Android Tutorial. No sub menus
            headerList.add(3, menuModel);

            menuModel = new MenuModel("Wishlist", true, false, "", ContextCompat.getDrawable(activity, R.drawable.ic_menu_wishlist)); //Menu of Android Tutorial. No sub menus
            headerList.add(4,menuModel);

            menuModel = new MenuModel("Logout", true, false, "", ContextCompat.getDrawable(activity, R.drawable.ic_logout_black_24dp)); //Menu of Android Tutorial. No sub menus
            headerList.add(menuModel);

            isLogined = true;
            expandableListAdapter.notifyDataSetChanged();
        }

    }

    public void removeLogout() {
        if (!isLogined) {
            MenuModel menuModel = new MenuModel("Checkout", true, false, "", ContextCompat.getDrawable(activity, R.drawable.ic_menu_checkout)); //Menu of Android Tutorial. No sub menus
            headerList.add(3, menuModel);

            menuModel = new MenuModel("Logout", true, false, "", ContextCompat.getDrawable(activity, R.drawable.ic_logout_black_24dp)); //Menu of Android Tutorial. No sub menus
            headerList.add(menuModel);
            isLogined = true;
            expandableListAdapter.notifyDataSetChanged();
        }

    }


    DialogDiscardImageBinding discardImageBinding;

    public void confirmationDialog() {
        discardImageBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_discard_image, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity, R.style.MyAlertDialogStyle_extend);
        builder.setView(discardImageBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(discardImageBinding.getRoot());
        discardImageBinding.titleTxt.setText("Confirm");
        discardImageBinding.subTitle.setText("Are you really want to logout?");
        discardImageBinding.yesTxt.setText("Logout");
        discardImageBinding.noTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });
        discardImageBinding.yesTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Toast.makeText(activity, "You are Logout!", Toast.LENGTH_SHORT).show();
                isLogined = false;
                new PREF(activity).Logout();
                Intent i = new Intent(activity, CategoryRootActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(i);


            }
        });
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    public void checkoutProcess() {
        discardImageBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_discard_image, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity, R.style.MyAlertDialogStyle_extend);
        builder.setView(discardImageBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(discardImageBinding.getRoot());
        discardImageBinding.titleTxt.setText("Cart");
        discardImageBinding.subTitle.setText("Your cart is empty. Please add products.");
        discardImageBinding.yesTxt.setText("Ok");
        discardImageBinding.noTxt.setVisibility(View.GONE);
        discardImageBinding.noTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        discardImageBinding.yesTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

}



