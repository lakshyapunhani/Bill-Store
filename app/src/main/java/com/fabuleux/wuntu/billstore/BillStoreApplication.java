package com.fabuleux.wuntu.billstore;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;

import com.crashlytics.android.Crashlytics;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;
import com.facebook.stetho.Stetho;

/**
 * Created by Dell on 17-Feb-18.
 */

public class BillStoreApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        //Fabric.with(this, new Crashlytics());


        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("default.realm")
                .migration(new Migration())
                .schemaVersion(1)
                .build();

        Realm.setDefaultConfiguration(config);

        Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(RealmInspectorModulesProvider
                                    .builder(this)
                                    .withLimit(100000)
                                    .build())
                            .build());

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
