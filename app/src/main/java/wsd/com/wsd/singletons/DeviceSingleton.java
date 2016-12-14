package wsd.com.wsd.singletons;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import wsd.com.wsd.comparators.UserDeviceComparator;
import wsd.com.wsd.models.UserDevice;

@Log
public class DeviceSingleton {
    private static DeviceSingleton ourInstance = new DeviceSingleton();

    @Getter
    private UserDevice selfUserDevice;
    @Getter
    private boolean isCoordinator;
    @Getter
    private boolean isLastDevice;
    @Getter
    private UserDevice nextDevice;
    @Getter
    private UserDevice cordinatorDevice;

    @Getter
    private UserDevice lastDevice;

    public Set<UserDevice> devicesInNetwork = new TreeSet<>(new UserDeviceComparator());

    public static DeviceSingleton getInstance() {
        return ourInstance;
    }

    private DeviceSingleton() {
    }

    public void updateInfo(Collection<UserDevice> userDevicesNetwork, UserDevice selfUserDevice){

        log.info(String.format("user device info: %s network info%s", selfUserDevice.toString(), userDevicesNetwork.toString()));

        this.selfUserDevice = selfUserDevice;
        this.devicesInNetwork.addAll(userDevicesNetwork);



        int lastDeviceId = -1;

        for(Iterator<UserDevice> iterator = userDevicesNetwork.iterator(); iterator.hasNext();){
            UserDevice curentDevice = iterator.next();

//            set coordinator
            if(lastDeviceId==-1){
                cordinatorDevice = curentDevice;
            }
//            set next device
            if(curentDevice.getDeviceId()==selfUserDevice.getDeviceId()+1){
                nextDevice = curentDevice;
            }
            lastDeviceId = curentDevice.getDeviceId();
            lastDevice = curentDevice;
        }

        if(selfUserDevice.getDeviceId()==1){
            this.isCoordinator = true;
        }
        if(!isCoordinator && lastDeviceId==selfUserDevice.getDeviceId()){
            this.isLastDevice = true;
        }
    }

}
