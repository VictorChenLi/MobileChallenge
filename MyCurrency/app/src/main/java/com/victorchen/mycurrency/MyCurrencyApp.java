package com.victorchen.mycurrency;

import android.app.Application;
import android.content.Intent;

import com.victorchen.mycurrency.network.api.fixerio.FixerApiService;


public class MyCurrencyApp extends Application {
    public static MyCurrencyApp App;

    public MyCurrencyApp() {
        App = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, FixerApiService.class));
    }
}
