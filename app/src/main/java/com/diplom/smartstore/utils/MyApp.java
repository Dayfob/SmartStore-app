package com.diplom.smartstore.utils;

import android.app.Application;

import com.stripe.android.PaymentConfiguration;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_51KvcFqIwdOpAH80orn1UPBSKFJVPcMgeGyMwnFLHaHuLyKxj9eN7g9O4Uyvd98GxJ8EYvmS3E3GyHeIG6bvgwPhH00Pp4nzyHQ"
        );
    }
}
