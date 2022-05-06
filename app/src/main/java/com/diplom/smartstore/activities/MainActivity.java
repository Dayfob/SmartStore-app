package com.diplom.smartstore.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;

import com.diplom.smartstore.R;
import com.diplom.smartstore.fragments.Account;
import com.diplom.smartstore.fragments.Cart;
import com.diplom.smartstore.fragments.Catalog;
import com.diplom.smartstore.fragments.Home;
import com.diplom.smartstore.fragments.Login;
import com.diplom.smartstore.fragments.WishList;
import com.diplom.smartstore.model.Category;
import com.diplom.smartstore.utils.Constants;
import com.diplom.smartstore.utils.LocalStorage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.Serializable;
import java.util.List;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.PaymentConfiguration;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView titleToolbar;
    BottomNavigationView navigation;
    ImageView backButton;
    String subCategoryTitle = null;
    List<Category> subcategories;
    LocalStorage localStorage;
    PaymentSheet paymentSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create global configuration and initialize ImageLoader with this config
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .build();
                .showImageForEmptyUri(R.drawable.ic_empty_image)
                .showImageOnFail(R.drawable.ic_empty_image)
                .resetViewBeforeLoading(true).cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
                .cacheInMemory(true)
//                .displayer(new RoundedBitmapDisplayer(50))
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
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backButtonClick();
            }
        });

        // initialize bottom navigation view
        navigation = findViewById(R.id.BottomNavigationView);
        navigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener);

        callHomeFragment();
    }

    // обработка нажатий ктагорий меню
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

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
                        if (!fm.findFragmentByTag("HOME").isVisible()) {
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
                        titleToolbar.setText("Войти");
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
//        Fragment productFragment = (Fragment)getSupportFragmentManager().findFragmentByTag("product");
//        Fragment userDataFragment = (Fragment)getSupportFragmentManager().findFragmentByTag("userData");
//        Fragment subcategoryProductsFragment = (Fragment)getSupportFragmentManager().findFragmentByTag("subcategoryProducts");
//        Fragment ordersFragment = (Fragment)getSupportFragmentManager().findFragmentByTag("orders");
//        Fragment registrationFragment = (Fragment)getSupportFragmentManager().findFragmentByTag("registration");
//        Fragment createOrderFragment = (Fragment)getSupportFragmentManager().findFragmentByTag("createOrder");
//        if (productFragment != null && productFragment.isVisible() ||
//                userDataFragment != null && userDataFragment.isVisible() ||
//                subcategoryProductsFragment != null && subcategoryProductsFragment.isVisible() ||
//                ordersFragment != null && ordersFragment.isVisible() ||
//                registrationFragment != null && registrationFragment.isVisible() && createOrderFragment.isVisible()) {
//            // add your code here
//            backButton.setVisibility(View.VISIBLE);
//        }

// old code
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        try {
//            if (fragmentManager.findFragmentByTag(Constants.FRAGMENT_PRODUCTS).isVisible()) {
//                // add bundle arguments
//                Bundle bundle = new Bundle();
//                bundle.putString(Constants.TITLE, subCategoryTitle);
//                bundle.putSerializable(Constants.CATEGORY_KEY, (Serializable) subcategories);
//
////                Subcategories subcategories = new Subcategories();
////                subcategories.setArguments(bundle);
////
////                fragmentTransaction.replace(R.id.content, subcategories, Constants.FRAGMENT_SUBCATEGORY);
//                fragmentTransaction.commit();
//                return;
//            }
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            if (fragmentManager.findFragmentByTag(Constants.FRAGMENT_SUBCATEGORY).isVisible()) {
//                fragmentTransaction.replace(R.id.content, new Catalog());
//                fragmentTransaction.commit();
//                titleToolbar.setText(R.string.TitleCatalog);
//                backButton.setVisibility(View.INVISIBLE);
//            }
//        } catch (NullPointerException e) {
//            super.onBackPressed();
//        }
    }

//    @Override
//    public void onBackPressed() {
//
//        int count = getSupportFragmentManager().getBackStackEntryCount();
//
//        if (count == 0) {
//            super.onBackPressed();
//            //additional code
//        } else {
//            getSupportFragmentManager().popBackStack();
//        }
//
//    }
}