package com.calenaur.pandemic.app;

import android.app.Application;

import com.calenaur.pandemic.api.API;

public class PandemicApplication extends Application {

    private API api;

    @Override
    public void onCreate() {
        super.onCreate();
        this.api = new API(getApplicationContext());
    }

    public API getAPI() {
        return api;
    }

}
