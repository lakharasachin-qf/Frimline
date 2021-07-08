package com.app.frimline;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.frimline.ui.gallery.GalleryFragment;
import com.app.frimline.ui.home.HomeFragment;
import com.app.frimline.ui.slideshow.SlideshowFragment;
import com.app.frimline.views.navigationDrawer.DrawerMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class HomeActivity extends BaseNavDrawerActivity   {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpToolbar();
        setTheme(R.style.Theme_Frimline_GREENSHADE);

    }



    @Override
    public void onBackPressed() {
        drawerMenu.onBackPressed();
    }

    DrawerMenu drawerMenu;
    private void setUpToolbar() {
        FrameLayout frameLayout = findViewById(R.id.content_frame);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_home, null, false);
        frameLayout.addView(activityView);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(HomeActivity.this);
        drawerMenu = new DrawerMenu(HomeActivity.this,DrawerMenu.HOME_FRAGMENT);
        drawerMenu.prepareMenuData();
        drawerMenu.populateExpandableList();
    }

}