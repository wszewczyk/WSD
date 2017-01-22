package wsd.com.wsd.app;


import android.app.Application;
import android.os.Handler;

import io.realm.Realm;
import wsd.com.wsd.comunication.BluetoothCommunicationService;
import wsd.com.wsd.singletons.DeviceSingleton;

/**
 * Created by wmsze on 11.12.2016.
 */

public class WSD extends Application {

    private static BluetoothCommunicationService bcs;
    private static Handler messageHandler;

    @Override
    public void onCreate() {
        Realm.init(this);
        bcs = new BluetoothCommunicationService();
        bcs.start();
        DeviceSingleton.getInstance();
        super.onCreate();
    }

    public static BluetoothCommunicationService getBcs() {
        return bcs;
    }

    public static Handler getMessageHandler() {
        return messageHandler;
    }

    public static void setMessageHandler(Handler handler) {
        messageHandler = handler;
        bcs.setHandler(messageHandler);
    }
}