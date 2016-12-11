package wsd.com.wsd.app;

import android.app.Application;
import android.content.res.Configuration;

/**
 * Created by wmsze on 11.12.2016.
 */

public class WSD extends Application {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}