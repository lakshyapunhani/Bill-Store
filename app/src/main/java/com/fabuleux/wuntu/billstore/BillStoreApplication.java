package com.fabuleux.wuntu.billstore;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;
import androidx.appcompat.app.AppCompatDelegate;

import com.crashlytics.android.Crashlytics;
import com.fabuleux.wuntu.billstore.Manager.SessionManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Dell on 17-Feb-18.
 */

public class BillStoreApplication extends Application
{

    SessionManager sessionManager;

    @Override
    public void onCreate()
    {
        super.onCreate();

        sessionManager = new SessionManager(getApplicationContext());

        Fabric.with(this, new Crashlytics());

        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("default.realm")
                .migration(new Migration())
                .schemaVersion(1)
                .build();

        Realm.setDefaultConfiguration(config);

        if (sessionManager.isDarkThemeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

//        Stetho.initialize(
//                    Stetho.newInitializerBuilder(this)
//                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
//                            .enableWebKitInspector(RealmInspectorModulesProvider
//                                    .builder(this)
//                                    .withLimit(100000)
//                                    .build())
//                            .build());

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
