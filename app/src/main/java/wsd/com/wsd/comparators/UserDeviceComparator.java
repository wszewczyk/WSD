package wsd.com.wsd.comparators;

import java.util.Comparator;

import wsd.com.wsd.models.UserDevice;

public class UserDeviceComparator implements Comparator<UserDevice>{
    @Override
    public int compare(UserDevice userDevice, UserDevice t1) {
        return Integer.compare(userDevice.getDeviceId(), t1.getDeviceId());
    }
}
