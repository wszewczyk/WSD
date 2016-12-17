package wsd.com.wsd.app;


import android.app.Application;

import io.realm.Realm;

/**
 * Created by wmsze on 11.12.2016.
 */

public class WSD extends Application {
    @Override
    public void onCreate() {
        Realm.init(this);
        super.onCreate();
    }
}