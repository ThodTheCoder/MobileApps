package com.mirea.elizarovnm.yandexmaps2;

import android.app.Application;

import com.yandex.mapkit.MapKitFactory;

public class App extends Application {
    private final String API_KEY = "f51ffcbd-5e90-4e9e-a3aa-4fefc08d5f4d";

    @Override
    public void onCreate() {
        super.onCreate();
        MapKitFactory.setApiKey(API_KEY);
    }
}
