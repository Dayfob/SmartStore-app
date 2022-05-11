package com.diplom.smartstore.activities;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.diplom.smartstore.R;
import com.diplom.smartstore.fragments.Account;
import com.diplom.smartstore.fragments.Cart;
import com.diplom.smartstore.fragments.Catalog;
import com.diplom.smartstore.fragments.Home;
import com.diplom.smartstore.fragments.Login;
import com.diplom.smartstore.fragments.WishList;
import com.diplom.smartstore.utils.Constants;
import com.diplom.smartstore.utils.LocalStorage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView titleToolbar;
    BottomNavigationView navigation;
    ImageView backButton;
    LocalStorage localStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create global configuration and initialize ImageLoader with this config
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_empty_image)
                .showImageOnFail(R.drawable.ic_empty_image)
                .resetViewBeforeLoading(true).cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
                .cacheInMemory(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        setContentView(R.layout.activity_main);
        localStorage = new LocalStorage(this);

        // Set Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set Title
        titleToolbar = findViewById(R.id.toolBarTitle);
        titleToolbar.setText(R.string.TitleHome);

        // Back Button
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> backButtonClick());

        // initialize bottom navigation view
        navigation = findViewById(R.id.BottomNavigationView);
        navigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener);

        callHomeFragment();
    }

    // обработка нажатий ктагорий меню
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            // Hide Back Button
            backButton.setVisibility(View.INVISIBLE);

            // Prevent Reload Same Fragment
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            switch (item.getItemId()) {
                case R.id.nav_home: // Home
                    // Prevent Reload
                    try {
                        if (!Objects.requireNonNull(fm.findFragmentByTag("HOME")).isVisible()) {
                            callHomeFragment();
                            titleToolbar.setText(R.string.TitleHome);
                        }
                    } catch (NullPointerException e) {
                        callHomeFragment();
                        titleToolbar.setText(R.string.TitleHome);
                    }
                    return true;

                case R.id.nav_catalog: // Catalog
                    ft.replace(R.id.content, new Catalog());
                    ft.commit();
                    titleToolbar.setText(R.string.TitleCatalog);
                    return true;

                case R.id.nav_cart: // Cart
                    ft.replace(R.id.content, new Cart());
                    ft.commit();
                    titleToolbar.setText(R.string.TitleCart);
                    return true;

                case R.id.nav_wishlist: // Wish List
                    ft.replace(R.id.content, new WishList());
                    ft.commit();
                    titleToolbar.setText(R.string.TitleWishList);
                    return true;

                case R.id.nav_account: // User Account
                    if (!localStorage.getToken().equals("")) {
                        titleToolbar.setText(R.string.account);
                        ft.replace(R.id.content, new Account());
                    } else {
                        titleToolbar.setText("Log in");
                        ft.replace(R.id.content, new Login());
                    }
                    ft.commit();
                    return true;
            }
            return false;
        }
    };


    private void callHomeFragment() {
        Bundle args = new Bundle();
        args.putInt(Constants.CATEGORY_ID_KEY, 0);

        Home home = new Home();
        home.setArguments(args);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content, home, "HOME");
        ft.commit();
    }


    private void backButtonClick() {
    }
}